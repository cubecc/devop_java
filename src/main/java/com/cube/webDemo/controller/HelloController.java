package com.cube.webDemo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
	@Autowired
	Environment env;
	
	@RequestMapping("/")
	public String index() {
		System.out.println("Request incoming");
        for (String profileName : env.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        } 
		return "Hello Hello, current profile : " + env + ", config name : " +  env.getProperty("env.name") + ", secret : "  + env.getProperty("secret.password");		
	}

}