package com.web.sukusuku.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.web.sukusuku.model.User;
import com.web.sukusuku.service.UserService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

	private final UserService userService;
	
	@GetMapping("/")
	public String getMethodName(
	        Model model,
	        @SessionAttribute(name = "loginUser", required = false) User loginUser) {
	    
	    if (loginUser != null) {
	        Optional<User> optionalUser = userService.findByUsername(loginUser.getUsername());
	        if (optionalUser.isPresent()) {
	            User user = optionalUser.get();
	            model.addAttribute("user", user);
	        }
	    }
	    
	    return "home"; // 🚀 리다이렉트하지 않고 바로 home.html 반환
	}
	
}
