package com.web.sukusuku.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.Calendar;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.repository.CalendarRepository;
import com.web.sukusuku.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyPageService {
	
	private final ProjectRepository projectRepository;
	private final CalendarRepository calendarRepository;
	
	// project 저장
	public Project saveProject(Project project) {
		Project SaveProject = projectRepository.save(project);
		return SaveProject;
	}
	
	// 유저네임으로 프로젝트 조회
	public List<Project> findByUserName_Project(String username) {
		
		return projectRepository.findByUser_Username(username);
	}
	
	// 유저네임으로 캘린더 조회
	public List<Calendar> findByUserName_Calendar(String username) {
		
		return calendarRepository.findByUser_Username(username);
	}

	// 프로젝트 삭제
	public void removeProject(Project project) {
		projectRepository.delete(project);
		
	}

	// id 로 프러젝트 조회
	public Optional<Project> findByProjectId(Long project_id) {
		// TODO Auto-generated method stub
		
		return projectRepository.findById(project_id);
	}
	
	// 캘린더 저장
	public Calendar saveCalendar(Calendar calendar) {
		// TODO Auto-generated method stub

		return calendarRepository.save(calendar);
	}

	// 스케줄 삭제
	public void removeCalendar(Long calendarId) {
		Optional<Calendar> optionalCalenar = calendarRepository.findById(calendarId);
		Calendar calendar = optionalCalenar.get();
		
		calendarRepository.delete(calendar);
	}

	 public boolean updateCompletionStatus(Long calendarId, boolean completed) {
	        Optional<Calendar> optionalCalendar = calendarRepository.findById(calendarId);
	        
	        if (optionalCalendar.isPresent()) {
	            Calendar calendar = optionalCalendar.get();
	            calendar.setCompleted(completed); // 완료 상태 변경
	            calendarRepository.save(calendar);
	            return true;
	        }
	        return false;
	    }

	
}