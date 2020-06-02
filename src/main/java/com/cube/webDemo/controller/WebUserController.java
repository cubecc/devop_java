package com.cube.webDemo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.cube.webDemo.dao.WebUserDao;
import com.cube.webDemo.entity.WebUser;

import antlr.collections.List;

@Controller
public class WebUserController {
	@Autowired
	Environment env;
	
	@Autowired
    private WebUserDao webUserDao;
		
	@GetMapping("/login")
	public String showLoginPage(Model model) {
		model.addAttribute("page_info", env.getProperty("env.name"));
		//model.addAttribute("users", webUserDao.findAll());
		//model.addAttribute("error", "invalid losssgin");
		return "login";
	}
	
	@PostMapping("/login")
	public String login(Model model, @RequestParam(value = "login") String login, @RequestParam(value = "password") String password) {
		
		ArrayList<WebUser> users = (ArrayList)webUserDao.findByLoginAndPassword(login, password);
		
		if(users.size()>0){			
			return "redirect:getWebUserList";
		}else{
			model.addAttribute("error", "Invalid login");
			return showLoginPage(model);
		}
		
		
	}
	
	@RequestMapping("/getWebUserList")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		//model.addAttribute("name", name);
		model.addAttribute("users", webUserDao.findAll());
		
		return "users";
	}
}
