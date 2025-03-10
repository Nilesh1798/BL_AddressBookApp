package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address_book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phoneNumber;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // ✅ Foreign key linking to AuthUser
    private AuthUser user;

    @Override
    public String toString() {
        return String.format("AddressBook[id=%d, name='%s', address='%s', phone='%s']", id, name, address, phoneNumber);
    }
}
