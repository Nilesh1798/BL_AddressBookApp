package com.example.AddressBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ComponentScan(basePackages = {"com.example.AddressBook", "com.example.security"}) // Ensure all components are scanned
public class AddressBookApplication {
	private static final Logger log = LoggerFactory.getLogger(AddressBookApplication.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AddressBookApplication.class, args);

		String environment = context.getEnvironment().getProperty("environment", "default");
		String dbUser = context.getEnvironment().getProperty("spring.datasource.username", "unknown");

		log.info("Address Book application is running in {} environment", environment);
		log.info("Address Book DB user is {}", dbUser);
	}
}
