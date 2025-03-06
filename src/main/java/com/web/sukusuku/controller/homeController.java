package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class homeController {

	
	@GetMapping("/")
	public String getMethodName() {
		return "example";
	}
	
}
