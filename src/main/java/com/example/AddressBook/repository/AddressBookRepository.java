package com.example.AddressBook.repository;

import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook, Long> {
    List<AddressBook> findByUser(AuthUser user);
}
