package com.example.AddressBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class AddressBookApplication {
	private static final Logger log = LoggerFactory.getLogger(AddressBookApplication.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AddressBookApplication.class, args);
		log.info("Address Book application is running in {} Environment",
				context.getEnvironment().getProperty("environment"));
		log.info("Address Book Db user is {} ",context.getEnvironment().getProperty("spring.datasource.username"));
	}

}