package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	
	@GetMapping("/")
	public String getMethodName() {
<<<<<<< HEAD:src/main/java/com/web/sukusuku/controller/HomeController.java
		return "home";
=======
		log.info("home controller");
		return "index";	
>>>>>>> youngjae:src/main/java/com/web/sukusuku/controller/homeController.java
	}
	
}
