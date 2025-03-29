package com.web.sukusuku.controller;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.dto.WordDto;
import com.web.sukusuku.dto.WordProgressRequestDto;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.User;
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

@Slf4j
@RequiredArgsConstructor
@Controller
public class StudyController {

    private final StudyService studyService;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final StudyWordProgressRepository studyWordProgressRepository;

    // ✅ 초기 페이지 렌더링 (레벨 선택 + 기본 챕터)
    @GetMapping("/studies/levelChoice")
    public String moveLevelChoicePage(Model model, HttpSession session) {
        List<Level> levels = studyService.getAllLevels();
        model.addAttribute("levels", levels);

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("로그인된 유저 없음!");
        }

        Integer defaultLevelId = 1;
        List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(defaultLevelId, loginUser);
        model.addAttribute("selectedLevelId", defaultLevelId);
        model.addAttribute("chapters", chapterDto);

        log.info("[컨]moveLevelChoicePage 끝");
        return "studies/levelChoice";
    }

    // ✅ JS 호출용 API (선택한 레벨 기준 챕터 목록 반환)
    @GetMapping("/studies/levelChoice/api")
    @ResponseBody
    public List<ChapterDto> levelChoiceApi(@RequestParam("levelId") Integer levelId, HttpSession session) {
        log.info("📥 API 요청 들어옴: levelId={}", levelId);

        User user = userRepository.findById("user01")
                .orElseThrow(() -> new RuntimeException("유저 없음!"));
        session.setAttribute("loginUser", user);

        User loginUser = (User) session.getAttribute("loginUser");
        log.info("👤 로그인 유저: {}", loginUser.getUsername());

        List<ChapterDto> chapterDto = studyService.getChaptersByLevelId(levelId, loginUser);
        log.info("📤 API 응답 데이터: {}", chapterDto); // 여기 중요!!!

        return chapterDto;
    }


    // ✅ 회독 시작
    @GetMapping("/studies/startStudy/{levelId}/{chapterId}")
    public String startStudy(@PathVariable("levelId") Integer levelId,
                             @PathVariable("chapterId") Integer chapterId,
                             HttpSession session,
                             Model model) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("세션에 로그인된 유저 없음!");
        }

        log.info("컨:startStudy levelId={}, chapterId={}", levelId, chapterId);

        studyService.startStudy(loginUser, levelId, chapterId);
        List<WordDto> words = studyService.getRemainingWordsByChapter(loginUser, chapterId);
        int knownWordsCount = studyWordProgressRepository.countKnownWords(loginUser, chapterId);

        int startChapterId = studyService.getStartChapterId(levelId, chapterId, 5);
        int cumulativeWords = wordRepository.countWordsBetweenChapters(levelId, startChapterId, chapterId);

        model.addAttribute("words", words);
        model.addAttribute("chapterId", chapterId);
        model.addAttribute("knownWordsCount", knownWordsCount);
        model.addAttribute("cumulativeWords", cumulativeWords);

        return "studies/study";
    }

    // ✅ 남은 단어 조회
    @GetMapping("/studies/remainingWords")
    @ResponseBody
    public List<WordDto> getRemainingWords(@RequestParam Integer chapterId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("세션에 로그인된 유저 없음!");
        }

        return studyService.getRemainingWordsByChapter(loginUser, chapterId);
    }

    // ✅ 단어 회독 상태 업데이트
    @PostMapping("/studies/wordProgress")
    @ResponseBody
    public int updateWordProgress(@RequestBody WordProgressRequestDto dto, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("세션에 로그인된 유저 없음!");
        }

        return studyService.updateWordProgress(loginUser, dto);
    }

    // ✅ 챕터 리셋
    @PostMapping("/studies/resetChapter")
    @ResponseBody
    public ResponseEntity<String> resetChapter(@RequestParam Integer chapterId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new RuntimeException("세션에 로그인된 유저 없음!");
        }

        studyService.resetChapterProgress(loginUser, chapterId);
        return ResponseEntity.ok("챕터가 성공적으로 리셋되었습니다");
    }
}
