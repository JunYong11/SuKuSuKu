package com.web.sukusuku.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.sukusuku.dto.ChapterChartDto;
import com.web.sukusuku.model.Calendar;
import com.web.sukusuku.model.Chapter;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.model.StudyProgress;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.CalendarRepository;
import com.web.sukusuku.repository.ChapterRepository;
import com.web.sukusuku.repository.LevelRepository;
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
	
	// 2번 차트 데이터 찾기 및 계산하기
	public List<Integer> SearchStudyData2(User loginUser) {
		// TODO Auto-generated method stub
		int [] maxLevels = new int[5];
		int [] countLevels = new int[5];
		
		List<Chapter> chapters = chapterRepository.findAll();
		List<ChapterChartDto> chapterChartDtos = new ArrayList<>();
		List<StudyProgress> findStudyProgressData = studyProgressRepository.findByUser_Username(loginUser.getUsername());
		
		for(int i = 0 ;i<chapters.size();i++) {
			ChapterChartDto dto = new ChapterChartDto();
			dto.setChapterId(chapters.get(i).getChapterId());
			dto.setLevel(chapters.get(i).getLevel().getLevelId());
			chapterChartDtos.add(dto);
		}
		
		// 챕터에 해당하는 레벨 아이디를 기준으로 카운트 하여 각 레벨별 챕터의 최대값을 구하는 코드
		for(int i = 0;i<chapterChartDtos.size();i++) {
			switch (chapterChartDtos.get(i).getLevel()) {
			case 1: {
				maxLevels[0]++;
				break;
			}
			case 2: {
				maxLevels[1]++;
				break;
			}
			case 3: {
				maxLevels[2]++;
				break;
			}
			case 4: {
				maxLevels[3]++;
				break;
			}
			case 5: {
				maxLevels[4]++;
				break;
			}
			default:
				continue;
			}
		}
		
		for(int i = 0;i<findStudyProgressData.size();i++) {
			if(findStudyProgressData.get(i).getReviewCount() != 0) {
				for(int j = 0;j<chapterChartDtos.size();j++) {
					if(findStudyProgressData.get(i).getChapterId()==chapterChartDtos.get(j).getChapterId()) {
						switch(chapterChartDtos.get(j).getLevel()) {
						case 1: {
							countLevels[0]++;
							break;
						}
						case 2: {
							countLevels[1]++;
							break;
						}
						case 3: {
							countLevels[2]++;
							break;
						}
						case 4: {
							countLevels[3]++;
							break;
						}
						case 5: {
							countLevels[4]++;
							break;
						}
						default:
							continue;
						}
					}
				}
			}
			else {
				continue;
			}
		}
		int maxValue = Arrays.stream(maxLevels).max().orElse(1);
		
		List<Integer> percentageList = new ArrayList<>();

        // 3. 퍼센트 값 계산 후 리스트에 추가
        for (int i = 0; i < countLevels.length; i++) {
            int percentage = (int) (((double) countLevels[i] / maxValue) * 100);
            percentageList.add(percentage);
        }

		return percentageList;
	}
}