package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.dto.ResponseDto;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.service.IAddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addressbook")  // ✅ Updated API path
public class AddressBookController {

    @Autowired
    private IAddressBookService service;

    @PostMapping
    public ResponseEntity<ResponseDto> addEntry(@Valid @RequestBody AddressBookDTO dto, Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        AddressBook entry = service.addEntry(dto, user);
        return ResponseEntity.ok(new ResponseDto("Entry added successfully", entry));
    }

    @GetMapping
    public ResponseEntity<List<AddressBook>> getAllEntries(Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        List<AddressBook> userEntries = service.getAllEntries().stream()
                .filter(entry -> entry.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userEntries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getEntryById(@PathVariable Long id, Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        AddressBook entry = service.getEntryById(id);

        if (!entry.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(new ResponseDto("Access denied!", null));
        }

        return ResponseEntity.ok(new ResponseDto("Entry found", entry));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> updateEntry(@PathVariable Long id, @Valid @RequestBody AddressBookDTO dto, Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        AddressBook entry = service.getEntryById(id);

        if (!entry.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(new ResponseDto("Access denied!", null));
        }

        return ResponseEntity.ok(new ResponseDto("Entry updated successfully", service.updateEntry(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteEntry(@PathVariable Long id, Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        AddressBook entry = service.getEntryById(id);

        if (!entry.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(new ResponseDto("Access denied!", null));
        }

        service.deleteEntry(id);
        return ResponseEntity.ok(new ResponseDto("Entry deleted successfully", null));
    }
}
