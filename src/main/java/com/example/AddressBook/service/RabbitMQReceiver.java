package com.example.AddressBook.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQReceiver {

    @RabbitListener(queues = "addressBookQueue")
    public void receiveMessage(String message) {
        System.out.println("📥 Received message: " + message);
    }
}
