package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.web.sukusuku.model.User;




@Controller
@RequestMapping("/calendar")
public class MypageController {
	
	@GetMapping("/mypage")
	public String getMethodMypage(
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			Model model) {
		
		model.addAttribute("loginUser",loginUser);
		
		
		return "mypage/mypage";
	}
}
