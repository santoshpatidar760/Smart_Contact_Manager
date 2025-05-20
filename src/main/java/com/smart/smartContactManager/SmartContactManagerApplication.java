package com.smart.smartContactManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.smart", "com.smart.smartContactManager"})
public class SmartContactManagerApplication {

	public static void main(String[] args) {

		SpringApplication.run(SmartContactManagerApplication.class, args);
	}

}
