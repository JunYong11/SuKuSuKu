package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.web.sukusuku.model.User;
import org.springframework.web.bind.annotation.RequestParam;



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
	@GetMapping("/game/rain")
	public String getMethodrain() {
		return "game/rain";
	}
	@GetMapping("/game")
	public String getMethodGame() {
		return "game/main";
	}
	

	
}
