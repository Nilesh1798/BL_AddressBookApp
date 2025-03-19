package com.example.AddressBook.service.implimentation;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.exceptions.AddressBookException;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.service.IAddressBookService;
import com.example.AddressBook.service.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl implements IAddressBookService {

    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private RabbitMQSender messagePublisher;

    @Autowired
    private UserRepository authUserRepository;

    @Override
    @Cacheable(value = "addressBookCache", key = "'allEntries'")
    public List<AddressBook> getAllEntries() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "addressBookCache", key = "#id")
    public AddressBook getEntryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AddressBookException("Entry not found"));
    }

    @Override
    @CacheEvict(value = "addressBookCache", allEntries = true)
    public AddressBook addEntry(AddressBookDTO dto) {
        if (dto.getUserId() == null || dto.getUserId() <= 0) {
            throw new AddressBookException("Invalid User ID: " + dto.getUserId());
        }

        AuthUser user = authUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AddressBookException("User not found with ID: " + dto.getUserId()));

        AddressBook entry = new AddressBook();
        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());
        entry.setUser(user);

        AddressBook savedEntry = repository.save(entry);

        // 🔥 Debugging output
        System.out.println("Saved AddressBook ID: " + savedEntry.getId());

        messagePublisher.sendMessage("address.book.queue", "New contact added: " + entry.getName());

        return savedEntry;
    }

    @Override
    @CachePut(value = "addressBookCache", key = "#id")
    public AddressBook updateEntry(Long id, AddressBookDTO dto) {
        AddressBook entry = getEntryById(id);
        entry.setName(dto.getName());
        entry.setAddress(dto.getAddress());
        entry.setPhoneNumber(dto.getPhoneNumber());

        AuthUser user = authUserRepository.findById(dto.getUserId())
                .orElseThrow(() -> new AddressBookException("User not found with ID: " + dto.getUserId()));

        entry.setUser(user);
        return repository.save(entry);
    }

    @Override
    @CacheEvict(value = "addressBookCache", key = "#id")
    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }
}
