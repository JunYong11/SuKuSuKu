package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.web.sukusuku.model.User;


@Controller
public class homeController {

	
	@GetMapping("/")
	public String getMethodName() {
		return "home";
	}
	@GetMapping("/level")
	public String getMethodLevel() {
		return "leveltest/leveltest";
	}
	@GetMapping("/problem")
	public String getMethod() {
		return "leveltest/problem";
	}
	@GetMapping("/result")
	public String getMethodResult() {
		return "leveltest/result";
	}
	@GetMapping("/register")
	public String showRegisterPage(Model model) {
	    model.addAttribute("user", new User());
	    return "user/register";
	}

	
}
