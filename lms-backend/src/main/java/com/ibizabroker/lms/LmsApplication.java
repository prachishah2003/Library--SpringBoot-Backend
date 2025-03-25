package com.ibizabroker.lms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ibizabroker.lms.service.UserService;

/**
 * Main application class for the Library Management System.
 * This class serves as the entry point for the Spring Boot application.
 * It enables scheduling capabilities and implements CommandLineRunner for initialization tasks.
 *
 * @author codematrix
 * @version 1.0
 */
@SpringBootApplication
@EnableScheduling
public class LmsApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	/**
	 * Executes after the application context is loaded.
	 * Performs initialization tasks such as creating the admin user if it doesn't exist.
	 *
	 * @param args Command line arguments passed to the application
	 */
	@Override
	public void run(String... args) {
		userService.createAdminUserIfNotExists(); // ✅ Auto-create admin user
	}
}
