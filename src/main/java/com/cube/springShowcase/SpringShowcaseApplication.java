package com.cube.springShowcase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class SpringShowcaseApplication {

	public static void main(String[] args) {		
		SpringApplication.run(SpringShowcaseApplication.class, args);
	}

}
