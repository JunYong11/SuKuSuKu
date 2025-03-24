package com.web.sukusuku.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.web.sukusuku.model.Calendar;
import com.web.sukusuku.model.CalendarCreateDto;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.MyPageService;
import com.web.sukusuku.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/mypage/api")
@RequiredArgsConstructor // 롬복 생성자 주입 어노테이션
@RestController
public class MyPageRestController {
	
	private final MyPageService myPageService;
	
	// CalendarController.java
	@PostMapping("/savecalendar")
	public ResponseEntity<Map<String, String>> calendarCreate(
			@RequestBody CalendarCreateDto calendarCreateDto,
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {

		Calendar calendar = new Calendar();	
		
		Optional<Project> optionalProject = myPageService.findByProjectId(calendarCreateDto.getProjectId());
		
		Project findProject = optionalProject.get();
		
		calendar.setUser(loginUser);
		calendar.setProject(findProject);
		calendar.setStartDate(LocalDateTime.now());
		calendar.setEndDate(calendarCreateDto.getSelectDate().atTime(23,59,59));
		calendar.setMemo(calendarCreateDto.getMemo());
		calendar.setSchedule(calendarCreateDto.getSchedule());
		
		
		Calendar resultCalendar = myPageService.saveCalendar(calendar);
		
		log.info("resultCalendar:{}",resultCalendar);

		// 데이터를 처리하고 응답을 반환
		Map<String, String> response = new HashMap<>();
		response.put("redirectUrl", "/mypage");

		return ResponseEntity.ok(response);
	}
	

	
}
