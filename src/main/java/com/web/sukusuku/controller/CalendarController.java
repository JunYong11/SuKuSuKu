package com.web.sukusuku.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.web.sukusuku.model.CalendarCreateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor // 롬복 생성자 주입 어노테이션
@RestController
public class CalendarController {

	// CalendarController.java
	@PostMapping("/users/register")
	public ResponseEntity<Map<String, String>> calendarCreate(@RequestBody CalendarCreateDto calendarCreateDto) {
	    log.info("calendar: {}", calendarCreateDto);

	    // 데이터를 처리하고 응답을 반환
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "이벤트가 저장되었습니다.");
	    
	    return ResponseEntity.ok(response);
	}

}
