package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.web.sukusuku.dto.userCreateDto;
import com.web.sukusuku.service.userService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class userController {
	userService userservice = new userService();
	
	@GetMapping("register")
	public String getMethodName() {
		return "user/register";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute userCreateDto usercreatedto ) {
		
		log.info("user:{}",usercreatedto);
		userservice.saveUser(usercreatedto);
		return "redirect:/";
	}

}
