package com.web.sukusuku.controller;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.Word;
import com.web.sukusuku.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RestController//(@Controller + @ResponseBody)
//@RequestMapping("studies")
public class StudyController {
	// 인터페이스 기준으로 선언!
	private final StudyService studyService;

// =========== 레벨 초이스 ===============
// ✅ 기본 렌더링 (초기 로드)
@GetMapping("/studies/levelChoice")
public String moveLevelChoicePage(Model model) {
	List<Level> levels = studyService.getAllLevels();
	model.addAttribute("levels", levels);

	// 기본값: levelId 1 (N1)
	Integer defaultLevelId = 1;
	List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(defaultLevelId);
	model.addAttribute("selectedLevelId", defaultLevelId);
	model.addAttribute("chapters", chapterDto);

	return "studies/levelChoice";
}

	// ✅ JS 비동기 호출용 (챕터 데이터 JSON 반환)
	@GetMapping("/studies/levelChoice/api")
	@ResponseBody
	public List<ChapterDto> levelChoiceApi(@RequestParam Integer levelId) {
		log.info("API levelId={}", levelId);
		List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId);
		return chapterDto;
	}



// //✅ 전체 레벨 목록 + 기본 화면 (타임리프)
//@GetMapping("/studies/levelChoice")
//public String moveLevelChoicePage(@RequestParam(required = false) Integer levelId, Model model) {
//	log.info("컨트롤러 levelId={}", levelId);
//	List<Level> levels = studyService.getAllLevels();
//	log.info("컨트롤러 levels: {}", levels);
//	model.addAttribute("levels", levels);
//
//	if (levelId != null) {
//		log.info("컨트롤러 levelId={}", levelId);
//		List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId);
//		log.info("chapterDto={}", chapterDto);
//
//		model.addAttribute("selectedLevelId", levelId);
//		model.addAttribute("chapters", chapterDto);
//	}
//
//	return "studies/levelChoice";
//}



 	// 스터디에서 사용하면 될듯
	@GetMapping("/studies/startStudy/{levelId}/{chapterId}")
	public String getWords(@PathVariable Integer levelId,
							   @PathVariable Integer chapterId,
						   Model model) {
		log.info("컨트롤러 levelId: {}, chapterId: {}", levelId, chapterId);
		List<Word> words = studyService.getWordsByLevelAndChapter(levelId, chapterId);
		model.addAttribute("words", words);
		return "studies/study";
	}
}



	

