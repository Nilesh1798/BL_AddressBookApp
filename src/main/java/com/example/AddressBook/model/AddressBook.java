package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "address_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook implements Serializable { // ✅ Implement Serializable

    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phoneNumber;
    private String city;
    private String state;
    private Long pincode;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;
}
