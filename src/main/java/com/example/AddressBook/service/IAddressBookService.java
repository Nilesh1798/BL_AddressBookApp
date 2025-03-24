package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;

import java.util.List;

public interface IAddressBookService {
    List<AddressBook> getAllEntries();
    AddressBook getEntryById(Long id);
    AddressBook addEntry(AddressBookDTO addressBookDTO);
    AddressBook updateEntry(Long id, AddressBookDTO addressBookDTO);
    void deleteEntry(Long id);
}
