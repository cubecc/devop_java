package com.cube.webDemo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cube.webDemo.dao.WebUserDao;
import com.cube.webDemo.entity.WebUser;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
	@Autowired
	Environment env;
	
	
	@Autowired
    private WebUserDao webUserDao;
	
	
	@RequestMapping("/")
	public String index() {
		System.out.println("Request incoming");
        for (String profileName : env.getActiveProfiles()) {
            System.out.println("Currently active profile - " + profileName);
        } 
		return "Hello Hello, current profile : " + env + ", config name : " +  env.getProperty("env.name") + ", secret : "  + env.getProperty("secret.password");		
	}

	
	@RequestMapping("/getWebUser")
	public WebUser getWebUser(int id) {
		WebUser u = webUserDao.findById(id).orElse(new WebUser());
		return u;
	}
	

}