package com.web.sukusuku.controller;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.dto.WordDto;
import com.web.sukusuku.dto.WordProgressRequestDto;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.StudyProgress;
import com.web.sukusuku.model.User;
import com.web.sukusuku.model.Word;
import com.web.sukusuku.repository.UserRepository;
import com.web.sukusuku.service.StudyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RequiredArgsConstructor // 자동 주입
@Controller
//@RestController//(@Controller + @ResponseBody)
//@RequestMapping("studies")
public class StudyController {
	// 인터페이스 기준으로 선언!
	private final StudyService studyService;
	// user 임의 삽입
	private final UserRepository userRepository;

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
// ============= user 임의 삽입
//@GetMapping("/test-user")
//@ResponseBody
//public String testUser(HttpSession session) {
//	User user = userRepository.findById("user01")
//			.orElseThrow(() -> new RuntimeException("유저 없음!"));
//
//	session.setAttribute("loginUser", user);
//
//	return "세션에 user01 저장 완료!";
//}

// ==============================    study ====================================

	@GetMapping("/studies/startStudy/{levelId}/{chapterId}")
	public String startStudy(@PathVariable Integer levelId,
							 @PathVariable Integer chapterId,
							 HttpSession session,
							 Model model) {
	log.info("컨:startStudy={}", levelId);
		// 테스트 유저정보

		// 1️⃣ 임의의 User 객체 가져오기 (DB에서)
		User user = userRepository.findById("user01")
				.orElseThrow(() -> new RuntimeException("유저 없음!"));

		// 2️⃣ 세션에 저장
		session.setAttribute("loginUser", user);
		// ------------- 임시 끝


		User loginUser = (User) session.getAttribute("loginUser");
		log.info("컨(startStudy):loginUser={}", loginUser);

		studyService.startStudy(loginUser, levelId, chapterId);
		List<WordDto> words = studyService.getRemainingWordsByChapter(loginUser, chapterId);
		log.info("컨(startStudy):words={}",words);
		model.addAttribute("words", words);
		model.addAttribute("chapterId", chapterId);

		return "studies/study"; // 타임리프 템플릿
	}

	@GetMapping("/studies/remainingWords")
	@ResponseBody
	public List<WordDto> getRemainingWords(@RequestParam Integer chapterId, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		log.info("컨(getRemainingWords)loginUser={}", loginUser);
		return studyService.getRemainingWordsByChapter(loginUser, chapterId);
	}

	@PostMapping("/studies/wordProgress")
	@ResponseBody
	public String updateWordProgress(@RequestBody WordProgressRequestDto dto, HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		log.info("컨(updateWordProgress)loginUser={}", loginUser);

		studyService.updateWordProgress(loginUser, dto);

		return "OK";
	}
	}




	

