package com.example.AddressBook.dto;

import lombok.Data;

@Data
public class AddressBookDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String city;
    private String state;
    private Long pincode;
    private Long userId; // ✅ Corrected from `user_id`
}
