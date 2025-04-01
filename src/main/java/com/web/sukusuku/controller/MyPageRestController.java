package com.web.sukusuku.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.web.sukusuku.dto.CompletionStatusRequest;
import com.web.sukusuku.dto.UserAchievementDto;
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
	private final UserService userService;
	
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
		calendar.setCheck(false);
		
		Calendar resultCalendar = myPageService.saveCalendar(calendar);
		
		log.info("resultCalendar:{}",resultCalendar);

		// 데이터를 처리하고 응답을 반환
		Map<String, String> response = new HashMap<>();
		response.put("redirectUrl", "/mypage");

		return ResponseEntity.ok(response);
	}
	
	// 스케줄 상태 체크
	@PostMapping("/updateCompletionStatus")
    public ResponseEntity<Map<String, Object>> updateCompletionStatus(@RequestBody CompletionStatusRequest request) {
        boolean success = myPageService.updateCompletionStatus(request.getCalendarId(), request.isCompleted());

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "업데이트 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
	
	@PostMapping("/updateUserAchievement")
	public ResponseEntity<Map<String, Object>> updateUserAchievement(
	        @RequestBody Map<String, Object> requestData) {
	    
	    String username = (String) requestData.get("username");
	    
	    List<Project> findProjects = myPageService.findByUserName_Project(username);
	    List<Calendar> findCalendars = myPageService.findByUserName_Calendar(username);
	    
	    List<UserAchievementDto> userAchievements = new ArrayList<UserAchievementDto>();
	    
	    for (int i = 0; i < findProjects.size(); i++) {
	        int result_count = 0;
	        int true_count = 0;
	        
	        for (int j = 0; j < findCalendars.size(); j++) {
	            if (findProjects.get(i).getProjectId() == findCalendars.get(j).getProject().getProjectId()) {
	                result_count++;
	                if (findCalendars.get(j).isCheck()) {
	                    true_count++;
	                }
	            }
	        }
	        
	        // 달성률 계산
	        double achievementRate = 0;
	        if (result_count > 0) {
	            achievementRate = (double) true_count / result_count * 100;
	        }

	        // DTO에 설정
	        UserAchievementDto achievement = new UserAchievementDto();
	        achievement.setResultcount((long) result_count);
	        achievement.setTruecount((long) true_count);
	        achievement.setProjectId(findProjects.get(i).getProjectId());
	        achievement.setAchievementRate(achievementRate); // 달성률 추가
	        
	        userAchievements.add(achievement);
	    }
	    
	    log.info("userAchievements: {}", userAchievements);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("userAchievements", userAchievements);
	  
	    return ResponseEntity.ok(response);
	}

	// 프로필 업데이트
	@PostMapping("updateProfileImage")
	public ResponseEntity<?> updateProfileImage(
			@RequestBody Map<String, String> request,
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {
	    String username = loginUser.getUsername();
	    String profileImage = request.get("profileImage");
	    
	    log.info("username:{}",username);
	    log.info("profileImage:{}",profileImage);

	    userService.updateProfileImage(username, profileImage); // DB 업데이트

	    return ResponseEntity.ok(Collections.singletonMap("success", true));
	}
	
	
	//첫번째 차트 업데이트
	@GetMapping("/statistics")
    public List<Map<String, Object>> getStatistics(
    		@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        List<Map<String, Object>> stats = myPageService.SearchStudyData1(loginUser);
        return stats;
    }

	
}
