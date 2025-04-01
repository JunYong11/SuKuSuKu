package com.web.sukusuku.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.Calendar;
import com.web.sukusuku.model.Chapter;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.model.StudyProgress;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.CalendarRepository;
import com.web.sukusuku.repository.ChapterRepository;
import com.web.sukusuku.repository.ProjectRepository;
import com.web.sukusuku.repository.StudyProgressRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class MyPageService {
	
	private final ProjectRepository projectRepository;
	private final CalendarRepository calendarRepository;
	private final StudyProgressRepository studyProgressRepository;
	private final ChapterRepository chapterRepository;
	
	// 프로젝트 저장
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

	// 1번 차트 데이터 찾기 및 계산하기
	public List<Map<String, Object>> SearchStudyData1(User loginUser) {
		// TODO Auto-generated method stub
		List<StudyProgress> findStudyProgressData = studyProgressRepository.findByUser_Username(loginUser.getUsername());
		log.info("findStudyProgressData:{}",findStudyProgressData);

		int[] levelCount = new int[5];
		
		for(int i = 0;i<findStudyProgressData.size();i++) {
			Optional<Chapter> optionalChapter = chapterRepository.findById(findStudyProgressData.get(i).getChapterId());
			Chapter chapter = optionalChapter.get();

			switch(chapter.getLevel().getLevelId()) {
			case 1:
				levelCount[0] += findStudyProgressData.get(i).getReviewCount();
				break;
			case 2:
				levelCount[1] += findStudyProgressData.get(i).getReviewCount();
				break;
			case 3:
				levelCount[2] += findStudyProgressData.get(i).getReviewCount();
				break;
			case 4:
				levelCount[3] += findStudyProgressData.get(i).getReviewCount();
				break;
			case 5:
				levelCount[4] += findStudyProgressData.get(i).getReviewCount();
				break;
			default:
				break;
			}
		}
		
		// stats 리스트 초기화 후 levelCount 값 적용
		List<Map<String, Object>> stats = new ArrayList<>();
		String[] levelNames = { "N1", "N2", "N3", "N4", "N5" };

		for (int i = 0; i < 5; i++) {
		    stats.add(new HashMap<>(Map.of("value", levelCount[i], "name", levelNames[i])));
		}
		return stats;
	}

	
}