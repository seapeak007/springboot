package com.lexue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("com.lexue")
@EnableScheduling
public class BootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);

	}
}
