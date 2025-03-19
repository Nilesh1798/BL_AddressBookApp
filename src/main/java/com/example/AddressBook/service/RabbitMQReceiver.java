package com.example.AddressBook.service;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    public void receiveMessage(String message) {
        System.out.println("📥 Received message: " + message);
    }
}
