package com.aprec.tristan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class TristanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TristanApplication.class, args);
	}

}
