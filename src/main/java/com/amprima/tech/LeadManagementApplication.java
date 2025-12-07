package com.amprima.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeadManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeadManagementApplication.class, args);
		System.out.println("✅ Amprima Tech Backend API is running!");
		System.out.println("📝 API Docs: http://localhost:8080/api/leads/health");

	}

}
