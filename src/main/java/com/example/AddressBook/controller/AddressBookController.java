package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    // ✅ Get all entries
    @GetMapping
    public List<AddressBook> getAllEntries() {
        return addressBookService.getAllEntries();
    }

    // ✅ Get entry by ID
    @GetMapping("/{id}")
    public AddressBook getEntryById(@PathVariable Long id) {
        return addressBookService.getEntryById(id);
    }

    // ✅ Add new entry
    @PostMapping
    public AddressBook addEntry(@RequestBody AddressBookDTO addressBookDTO) {
        return addressBookService.addEntry(addressBookDTO);
    }

    // ✅ Update entry by ID
    @PutMapping("/{id}")
    public AddressBook updateEntry(@PathVariable Long id, @RequestBody AddressBookDTO addressBookDTO) {
        return addressBookService.updateEntry(id, addressBookDTO);
    }

    // ✅ Delete entry by ID
    @DeleteMapping("/{id}")
    public String deleteEntry(@PathVariable Long id) {
        addressBookService.deleteEntry(id);
        return "Address book entry deleted successfully";
    }
}
