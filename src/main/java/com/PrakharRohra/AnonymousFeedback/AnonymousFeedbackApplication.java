package com.PrakharRohra.AnonymousFeedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class AnonymousFeedbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnonymousFeedbackApplication.class, args);
	}

}
