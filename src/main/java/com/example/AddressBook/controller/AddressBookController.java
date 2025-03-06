package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.dto.ResponseDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/addressbook")
public class AddressBookController {
    @Autowired
    private IAddressBookService service;

    @PostMapping
    public ResponseEntity<ResponseDto> addEntry(@Valid @RequestBody AddressBookDTO dto) {
        return ResponseEntity.ok(new ResponseDto("Entry added successfully", service.addEntry(dto)));
    }

    @GetMapping
    public ResponseEntity<List<AddressBook>> getAllEntries() {
        return ResponseEntity.ok(service.getAllEntries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getEntryById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseDto("Entry found", service.getEntryById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateEntry(@PathVariable Long id, @Valid @RequestBody AddressBookDTO dto) {
        return ResponseEntity.ok(new ResponseDto("Entry updated successfully", service.updateEntry(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteEntry(@PathVariable Long id) {
        service.deleteEntry(id);
        return ResponseEntity.ok(new ResponseDto("Entry deleted successfully", null));
    }
}
