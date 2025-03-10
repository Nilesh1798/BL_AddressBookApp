package com.example.AddressBook.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponseDTO{
    // Setters (optional if you need to modify the object after creation)
    // Getters
    private String message;
    private String token;

    // Constructor to initialize both fields
    public LoginResponseDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Default constructor (optional, but useful if you need to instantiate without values)
    public LoginResponseDTO() {}

}