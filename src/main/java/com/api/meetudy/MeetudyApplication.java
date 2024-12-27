package com.api.meetudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MeetudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetudyApplication.class, args);
	}

}
