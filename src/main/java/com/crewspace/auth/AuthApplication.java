package com.crewspace.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class AuthApplication {

	public static final String APPLICATION_LOCATIONS =
		"spring.config.location="
			+ "classpath:application.yml,"
			+ "classpath:env.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthApplication.class)
			.properties(APPLICATION_LOCATIONS)
			.run(args);
	}

}
