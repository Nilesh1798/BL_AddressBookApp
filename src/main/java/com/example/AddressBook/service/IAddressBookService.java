package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;

import java.util.List;

public interface IAddressBookService {
    AddressBook addEntry(AddressBookDTO dto);

    AddressBook addEntry(AddressBookDTO dto, AuthUser user);  // ✅ Updated method signature
    List<AddressBook> getAllEntries();
    AddressBook getEntryById(Long id);
    AddressBook updateEntry(Long id, AddressBookDTO dto);
    void deleteEntry(Long id);
}
