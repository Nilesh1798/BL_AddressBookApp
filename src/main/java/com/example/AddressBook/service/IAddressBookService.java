package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import java.util.List;

public interface IAddressBookService {
    AddressBook addEntry(AddressBookDTO addressBookDTO);
    List<AddressBook> getAllEntries();
    AddressBook getEntryById(Long id);
    AddressBook updateEntry(Long id, AddressBookDTO addressBookDTO);
    void deleteEntry(Long id);

}

