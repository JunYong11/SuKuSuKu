package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
	
	@GetMapping("/mypage")
	public String getMethodMypage() {
		return "mypage/mypage";
	}
}
