package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {
	@GetMapping("/game/rain")
	public String getMethodrain() {
		return "game/rain";
	}
	@GetMapping("/game")
	public String getMethodGame() {
		return "game/main";
	}
}
