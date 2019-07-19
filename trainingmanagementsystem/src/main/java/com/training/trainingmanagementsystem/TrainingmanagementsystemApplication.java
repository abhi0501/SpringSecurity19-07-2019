package com.training.trainingmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TrainingmanagementsystemApplication {

	public static void main(String[] args) {
		System.out.println("start");
		SpringApplication.run(TrainingmanagementsystemApplication.class, args);
		System.out.println("end");
	}

	
}
