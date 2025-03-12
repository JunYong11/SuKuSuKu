package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class homeController {

	
	@GetMapping("/")
	public String getMethodName() {
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
	
}
