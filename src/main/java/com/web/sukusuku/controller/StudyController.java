package com.web.sukusuku.controller;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.dto.WordDto;
import com.web.sukusuku.dto.WordProgressRequestDto;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.StudyProgress;
import com.web.sukusuku.model.User;
import com.web.sukusuku.model.Word;
import com.web.sukusuku.repository.ChapterRepository;
import com.web.sukusuku.repository.StudyWordProgressRepository;
import com.web.sukusuku.repository.UserRepository;
import com.web.sukusuku.repository.WordRepository;
import com.web.sukusuku.service.StudyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
   private final WordRepository wordRepository;
   private final StudyWordProgressRepository studyWordProgressRepository;

   // =========== 레벨 초이스 ===============
// ✅ 기본 렌더링 (초기 로드)
@GetMapping("/studies/levelChoice")
public String moveLevelChoicePage(Model model,HttpSession session) {
   List<Level> levels = studyService.getAllLevels();
   model.addAttribute("levels", levels);

   User loginUser = (User) session.getAttribute("loginUser");

   // 기본값: levelId 1 (N1)
   Integer defaultLevelId = 1;
   List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(defaultLevelId , loginUser);
   model.addAttribute("selectedLevelId", defaultLevelId);
   model.addAttribute("chapters", chapterDto);


   log.info("[컨]moveLevelChoicePage 끝");
   return "studies/levelChoice";
}

   // ✅ JS 비동기 호출용 (챕터 데이터 JSON 반환)
   @GetMapping("/studies/levelChoice/api")
   @ResponseBody
   public List<ChapterDto> levelChoiceApi(@RequestParam(name = "levelId") Integer levelId,
                                 HttpSession session) {
      log.info("API levelId={}", levelId);

      User loginUser = (User) session.getAttribute("loginUser");

      List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId,loginUser);
      log.info("[컨]levelChoiceApi 끝");
      return chapterDto;
   }



// ==============================    study ====================================

   @GetMapping("/studies/startStudy/{levelId}/{chapterId}")
   public String startStudy(@PathVariable(name = "levelId") Integer levelId,
                      @PathVariable(name = "chapterId") Integer chapterId,
                      HttpSession session,
                      Model model) {
   log.info("컨:startStudy={}", levelId);

      User loginUser = (User) session.getAttribute("loginUser");
//      log.info("컨(startStudy):loginUser={}", loginUser);

      studyService.startStudy(loginUser, levelId, chapterId);
      List<WordDto> words = studyService.getRemainingWordsByChapter(loginUser,chapterId);

      log.info("컨(startStudy):words={}",words);
      // 안다고 한 단어
      int knownWordsCount = studyWordProgressRepository.countKnownWords(loginUser, chapterId);
      log.info("[컨:startStudy]안다 카운트={}",knownWordsCount);

      // 챕터별 누적 단어 갯수
      // ✅ 누적 단어 수 구하기 (레벨ID, 챕터ID 기준 누적 범위 사용)
      int startChapterId = studyService.getStartChapterId(levelId, chapterId, 5);  // 범위는 DEFAULT_CHAPTER_RANGE 값
      int cumulativeWords = wordRepository.countWordsBetweenChapters(levelId, startChapterId, chapterId);

      model.addAttribute("words", words);
      model.addAttribute("chapterId", chapterId);
      model.addAttribute("knownWordsCount", knownWordsCount);
      model.addAttribute("cumulativeWords",cumulativeWords);

      return "studies/study"; // 타임리프 템플릿
   }

   @GetMapping("/studies/remainingWords")
   @ResponseBody
   public List<WordDto> getRemainingWords(@RequestParam(name = "chapterId") Integer chapterId,
                                 HttpSession session) {
      User loginUser = (User) session.getAttribute("loginUser");
      log.info("컨(getRemainingWords)loginUser={} chpater={}", loginUser,chapterId);
      return studyService.getRemainingWordsByChapter(loginUser,chapterId);
   }

   @PostMapping("/studies/wordProgress")
   @ResponseBody
   public int updateWordProgress(@RequestBody WordProgressRequestDto dto, HttpSession session) {
      User loginUser = (User) session.getAttribute("loginUser");
//      log.info("컨(updateWordProgress)loginUser={}", loginUser);


      return studyService.updateWordProgress(loginUser, dto);
   }

   // 새로운 리셋 엔드포인트 추가
   @PostMapping("/studies/resetChapter")
   @ResponseBody
   public ResponseEntity<String> resetChapter(@RequestParam(name = "chapterId")    Integer chapterId,
                                   HttpSession session) {
      User loginUser = (User) session.getAttribute("loginUser");
      studyService.resetChapterProgress(loginUser, chapterId);
      return ResponseEntity.ok("챕터가 성공적으로 리셋되었습니다");
      }
   }