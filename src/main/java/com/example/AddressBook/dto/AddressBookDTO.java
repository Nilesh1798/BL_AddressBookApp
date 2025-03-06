package com.example.AddressBook.dto;



import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class AddressBookDTO {
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
