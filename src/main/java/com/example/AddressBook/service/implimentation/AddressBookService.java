package com.example.AddressBook.service.implimentation;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookService implements IAddressBookService {

    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get all entries
    @Override
    @Cacheable(value = "addressBookAll")
    public List<AddressBook> getAllEntries() {
        System.out.println("Fetching all from DB...");
        return repository.findAll();
    }

    // ✅ Get entry by ID
    @Override
    @Cacheable(value = "addressBook", key = "#id")
    public AddressBook getEntryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address book entry not found with ID: " + id));
    }

    // ✅ Add new entry
    @Override
    @CacheEvict(value = "addressBookAll", allEntries = true) // ✅ Clears the cache after adding a new entry
    public AddressBook addEntry(AddressBookDTO addressBookDTO) {
        AuthUser user = userRepository.findById(addressBookDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + addressBookDTO.getUserId()));

        AddressBook entry = new AddressBook();
        entry.setName(addressBookDTO.getName());
        entry.setAddress(addressBookDTO.getAddress());
        entry.setPhoneNumber(addressBookDTO.getPhoneNumber());
        entry.setCity(addressBookDTO.getCity());
        entry.setState(addressBookDTO.getState());
        entry.setPincode(addressBookDTO.getPincode());
        entry.setUser(user);

        return repository.save(entry); // ✅ Save the new entry and clear old cache
    }

    // ✅ Update entry by ID
    @Override
    @CachePut(value = "addressBook", key = "#id")
    @CacheEvict(value = "addressBookAll", allEntries = true) // ✅ Clears cache on update
    public AddressBook updateEntry(Long id, AddressBookDTO addressBookDTO) {
        AddressBook entry = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address book entry not found with ID: " + id));

        entry.setName(addressBookDTO.getName());
        entry.setAddress(addressBookDTO.getAddress());
        entry.setPhoneNumber(addressBookDTO.getPhoneNumber());

        return repository.save(entry);
    }

    @Override
    @CacheEvict(value = {"addressBook", "addressBookAll"}, allEntries = true) // ✅ Clears all cache on delete
    public void deleteEntry(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Address book entry not found with ID: " + id);
        }
        repository.deleteById(id);
    }


    // ✅ Clear all cached address book entries
    @CacheEvict(value = "addressBookAll", allEntries = true)
    public void clearCache() {
        System.out.println("Clearing all cache...");
    }
}
