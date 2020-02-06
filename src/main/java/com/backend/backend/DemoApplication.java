package com.backend.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication extends HiddenApi {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
