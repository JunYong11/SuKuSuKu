package com.web.sukusuku.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.web.sukusuku.model.Calendar;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.MyPageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequestMapping("/mypage")
@RequiredArgsConstructor // 롬복 생성자 주입 어노테이션
@Slf4j
@Controller
public class MypageController {
	
	private final MyPageService myPageService;
	
	// 마이페이지 출력
	@GetMapping("")
	public String getMethodMypage(
			@SessionAttribute(name = "loginUser", required = false) User loginUser,
			Model model) {
		
		List<Project> FindProjects = myPageService.findByUserName_Project(loginUser.getUsername());
		List<Calendar> FindCalendars = myPageService.findByUserName_Calendar(loginUser.getUsername());
		
		model.addAttribute("loginUser",loginUser);
		model.addAttribute("projects",FindProjects);
		model.addAttribute("events",FindCalendars);
		
		return "mypage/mypage";
	}
	
	// 프로젝트 추가
	@PostMapping("/project")
	public String projectCreate(
			@ModelAttribute Project project,
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {
			
		project.setUser(loginUser);
		
		
		myPageService.saveProject(project);
		
		
		// 데이터를 처리하고 응답을 반환
		Map<String, String> response = new HashMap<>();
		response.put("message", "이벤트가 저장되었습니다.");
		
		return "redirect:/mypage";
	}
	
	// 프로젝트 삭제
	@PostMapping("/deleteProject")
	public String projeDelet(
			@ModelAttribute Project project,
			@SessionAttribute(name = "loginUser", required = false) User loginUser) {
		
		List<Project> FindProjects = myPageService.findByUserName_Project(loginUser.getUsername());

		for(int i = 0;i< FindProjects.size();i++) {
			if(project.getProjectId() == FindProjects.get(i).getProjectId()) {
				Project deletProject = FindProjects.get(i);
				
				myPageService.removeProject(deletProject);		// 프로젝트 삭제
			}
		}

		return "redirect:/mypage";
	}
	
	// 스케줄 삭제
	@PostMapping("/deleteCalendar")
	public String calendarDelet(
			@ModelAttribute Calendar calendar) {
		
		myPageService.removeCalendar(calendar.getCalendarId());

		return "redirect:/mypage";
	}
	
	// 모달 호출시 프로젝트 불러오기
	@GetMapping("/modal/projects")
    @ResponseBody
    public List<Project> getProjects(
    		@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        return myPageService.findByUserName_Project(loginUser.getUsername());
    }
	
	
	// 캘린더에 스케줄 표시
    @GetMapping("/events")
    @ResponseBody  // JSON 형태로 반환
    public List<Map<String, Object>> getEvents(
    		@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        List<Calendar> calendars = myPageService.findByUserName_Calendar(loginUser.getUsername());  // 로그인한 유저의 일정 가져오기

        return calendars.stream().map(calendar -> {
            Map<String, Object> event = new HashMap<>();
            event.put("title", calendar.getSchedule()); // 캘린더에서 표시될 제목
            event.put("start", calendar.getEndDate().toLocalDate().toString()); // 시작 날짜 종료 날짜랑 동일
            event.put("end", calendar.getEndDate().toLocalDate().toString()); // 종료 날짜
            return event;
        }).collect(Collectors.toList());
    }
	
	
	
}
