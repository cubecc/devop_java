package com.cube.springShowcase.controller;

import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
	Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	Environment env;
	
	@RequestMapping("/")
	public String index() {
		logger.info("Request incoming");
        for (String profileName : env.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        } 
		return "Hello Hello, current profile : " + env + ", config name : " +  env.getProperty("env.name") + ", secret : "  + env.getProperty("secret.password");		
	}

}