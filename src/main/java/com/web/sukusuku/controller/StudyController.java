package com.web.sukusuku.controller;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.model.Level;
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
// ✅ 전체 레벨 목록 + 기본 화면
@GetMapping("/studies/levelChoice")
public String moveLevelChoicePage(@RequestParam(required = false) Integer levelId, Model model) {
	log.info("컨트롤러 levelId={}", levelId);
	List<Level> levels = studyService.getAllLevels();
	log.info("컨트롤러 levels: {}", levels);
	model.addAttribute("levels", levels);

	if (levelId != null) {
		log.info("컨트롤러 levelId={}", levelId);
		List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId);
		log.info("chapterDto={}", chapterDto);

//		model.addAttribute("selectedLevelId", levelId);
		model.addAttribute("chapters", chapterDto);
	}

	return "studies/levelChoice";
}


//	@PostMapping("/levelChoice")
//	public String levelChoice(@RequestParam Integer levelId, Model model) {
//		log.info("컨트롤러 levelId={}", levelId);
//
//		List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId);
//		log.info("chapterDto={}", chapterDto);
//
////		model.addAttribute("chapters", chapterDto);  // 뷰에 넘겨주기!
//
//		return "studies/levelChoice"; // templates/studies/levelChoice.html
//	}


//	@GetMapping("/levelChoice/{levelId}")
//	public String getChapters(@RequestParam Integer levelId,
//										Model model) {
////		String username = principal.getName();  // 로그인한 유저 아이디
//		List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId);
//		model.addAttribute("chapters", chapterDto);
//
//		 return "/levelChoice";
//	}

//	@GetMapping("/levelChocie")
//	public String levelChocie() {
//		return "/levelChocie";
//	}
//
 	// 스터디에서 사용하면 될듯
//	@GetMapping("/levelChoice/{levelId}/{chapterId}/words")
//	public List<Word> getWords(@PathVariable Integer levelId,
//							   @PathVariable Integer chapterId) {
//		log.info("컨트롤러 levelId: {}, chapterId: {}", levelId, chapterId);
//
//		return studyService.getWordsByLevelAndChapter(levelId, chapterId);
//	}
}



	

