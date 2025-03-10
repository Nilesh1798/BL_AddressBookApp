package com.example.AddressBook.service.implimentation;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.exceptions.AddressBookException;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBookServiceImpl implements IAddressBookService {

    private final AddressBookRepository repository;
    private final UserRepository userRepository;

    public AddressBookServiceImpl(AddressBookRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    private AuthUser getAuthenticatedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public AddressBook addEntry(AddressBookDTO dto) {
        AuthUser user = getAuthenticatedUser();
        AddressBook entry = new AddressBook();
        entry.setId(dto.getId());
        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());
        entry.setUser(user); // Associate with logged-in user
        return repository.save(entry);
    }

    @Override
    public AddressBook addEntry(AddressBookDTO dto, AuthUser user) {
        return null;
    }

    @Override
    public List<AddressBook> getAllEntries() {
        AuthUser user = getAuthenticatedUser();
        if ("ADMIN".equals(Optional.ofNullable(user.getRole()).orElse(""))) {
            return repository.findAll(); // Admin can access all contacts
        }
        return repository.findByUser(user); // Regular users only see their own contacts
    }

    @Override
    public AddressBook getEntryById(Long id) {
        AddressBook entry = repository.findById(id)
                .orElseThrow(() -> new AddressBookException("Entry not found"));

        AuthUser user = getAuthenticatedUser();
        if (!entry.getUser().equals(user) && !"ADMIN".equals(Optional.ofNullable(user.getRole()).orElse(""))) {
            throw new AddressBookException("Unauthorized access!");
        }
        return entry;
    }

    @Override
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        AddressBook entry = getEntryById(id);
        AuthUser user = getAuthenticatedUser();

        if (!entry.getUser().equals(user) && !"ADMIN".equals(Optional.ofNullable(user.getRole()).orElse(""))) {
            throw new AddressBookException("Unauthorized update attempt!");
        }

        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());
        return repository.save(entry);
    }

    @Override
    public void deleteEntry(Long id) {
        AddressBook entry = getEntryById(id);
        AuthUser user = getAuthenticatedUser();

        if (!entry.getUser().equals(user) && !"ADMIN".equals(Optional.ofNullable(user.getRole()).orElse(""))) {
            throw new AddressBookException("Unauthorized delete attempt!");
        }

        repository.deleteById(id);
    }
}
