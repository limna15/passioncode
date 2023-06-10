package com.passioncode.procurementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PassioncodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassioncodeApplication.class, args);
	}

}
