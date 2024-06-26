package com.mgmtp.cfu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;


@SpringBootApplication
public class CourseForYouApplication {
	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "h2");
		SpringApplication.run(CourseForYouApplication.class, args);
	}
}
