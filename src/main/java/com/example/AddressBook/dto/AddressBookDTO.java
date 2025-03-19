package com.example.AddressBook.dto;

import lombok.Data;

@Data
public class AddressBookDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private Long userId; // ✅ Changed from `id` to `userId`
}
