package com.web.sukusuku.service;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.dto.WordDto;
import com.web.sukusuku.dto.WordProgressRequestDto;
import com.web.sukusuku.model.*;
import com.web.sukusuku.repository.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional // 트렌젝셔널 어노테이션 :비즈니스 로직이 수행되는 Service 계층에 쓰는 게 원칙
@RequiredArgsConstructor// final 생성자 주입
@Service // 서비스 단 class
public class StudyServiceImpl implements StudyService {

    private final LevelRepository levelRepository;
    private final ChapterRepository chapterRepository;
    private final WordRepository wordRepository;
    private final StudyProgressRepository studyProgressRepository;
    private final StudyWordProgressRepository studyWordProgressRepository;

    /** ✅ level choice  ===================================================
     *
     */
    // 모든 레벨 조회
    @Override
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }
    // 레벨 id로 챕터 및 단어 정보(ChapterDto 만들기) -> 이거 levelChoice.html 에 띄우기
    @Override
    public List<ChapterDto> getChaptersByLevelId(Integer levelId) {
        log.info("서비스Impl(챕터조회): levelId={}", levelId);
        // 챕터 리스트 + 누적 단어 수
        return chapterRepository.findByLevelLevelId(levelId)
                .stream().map(chapter -> ChapterDto.builder()
                        .levelId(levelId)
                        .chapterId(chapter.getChapterId())
                        .chapterName(chapter.getChapterName())
                        .cumulativeWords(chapter.getWords().size())
                        .reviewCount(0) // 임시값
                        .build()
                ).toList();
    }
    /** ✅ study ============================================
     *
     */
// --------------------- study(회독) ----------------------------------------------
@Override
public void startStudy(User user, Integer levelId, Integer chapterId) {
    Optional<StudyProgress> existingProgress = studyProgressRepository.findByUserAndChapterId(user, chapterId);

    if (existingProgress.isEmpty()) {
        StudyProgress progress = StudyProgress.builder()
                .user(user)
                .chapterId(chapterId)
                .status(StudyProgress.Status.진행_중)
                .reviewCount(0)
                .lastReviewedAt(LocalDateTime.now())
                .build();

        studyProgressRepository.save(progress);

        List<Word> allWords = wordRepository.findByChapterChapterId(chapterId);

        for (Word word : allWords) {
            studyWordProgressRepository.save(
                    StudyWordProgress.builder()
                            .user(user)
                            .word(word)
                            .status(StudyWordProgress.Status.모른다)
                            .build()
            );
        }
    }
}

    @Override
    public List<WordDto> getRemainingWordsByChapter(User user, Integer chapterId) {
        List<StudyWordProgress> progresses = studyWordProgressRepository.findByUserAndChapterId(user, chapterId);

        return progresses.stream()
                .filter(p -> p.getStatus() == StudyWordProgress.Status.모른다)
                .map(p -> {
                    Word word = p.getWord();
                    return new WordDto(
                            word.getWordId(),
                            word.getKanji(),
                            word.getHiragana(),
                            word.getMeaning()
                    );
                })
                .toList();
    }

    @Override
    public void updateWordProgress(User user, WordProgressRequestDto dto) {
        Word word = wordRepository.findById(dto.getWordId())
                .orElseThrow(() -> new RuntimeException("단어 없음"));

        StudyWordProgress progress = studyWordProgressRepository.findByUserAndWord(user, word)
                .orElseThrow(() -> new RuntimeException("학습 기록 없음"));

        progress.setStatus(StudyWordProgress.Status.valueOf(dto.getStatus()));
        studyWordProgressRepository.save(progress);

        int knownWordsCount = studyWordProgressRepository.countKnownWords(user, dto.getChapterId());
        int totalWordsCount = wordRepository.findByChapterChapterId(dto.getChapterId()).size();

        if (knownWordsCount == totalWordsCount) {
            StudyProgress studyProgress = studyProgressRepository.findByUserAndChapterId(user, dto.getChapterId())
                    .orElseThrow(() -> new RuntimeException("챕터 진행 없음"));

            studyProgress.setStatus(StudyProgress.Status.완료);
            studyProgress.setReviewCount(studyProgress.getReviewCount() + 1);
            studyProgress.setLastReviewedAt(LocalDateTime.now());

            studyProgressRepository.save(studyProgress);

            resetStudyWordProgress(user, dto.getChapterId());
        }
    }

    private void resetStudyWordProgress(User user, Integer chapterId) {
        List<StudyWordProgress> progresses = studyWordProgressRepository.findByUserAndChapterId(user, chapterId);

        progresses.forEach(p -> {
            p.setStatus(StudyWordProgress.Status.모른다);
            studyWordProgressRepository.save(p);
        });
    }


    
    // ---------- 0317 이전 레벨 쳅터 성공 --------
//    // 특정 levelId에 맞는 챕터 목록을 가져오는 메서드
//    @Override
//    public List<ChapterDto> getChaptersByLevelId(Integer levelId) {
//        log.info("서비스Impl(챕터조회): levelId={}", levelId);
//
//        List<Chapter> chapters = chapterRepository.findByLevelLevelId(levelId);
//        List<ChapterDto> chapterDtos = new ArrayList<>();
//        int cumulativeWords = 0; // 누적 초기화
//
//        for (Chapter chapter : chapters) {
//            int wordCount = chapter.getWords().size();
//            cumulativeWords += wordCount;
//
//            // 이건 지금은 0 으로 하기
////            int reviewCount = studyProgressRepository
////                    .findByUserUsernameAndId2(username, chapter.getChapterId())
////                    .map(StudyProgress::getReviewCount)
////                    .orElse(0);
//
//            ChapterDto dto = ChapterDto.builder()
//                    .levelId(levelId)
//                    .chapterId(chapter.getChapterId())
//                    .chapterName(chapter.getChapterName())
//                    .cumulativeWords(cumulativeWords)
//                    .reviewCount(0)
//                    .build();
//
//            chapterDtos.add(dto);
//        }
//
//        return chapterDtos;
//    }









    //레벨 ID와 챕터 ID로 단어 리스트를 조회
//    @Override
//    public List<Word> getWordsByLevelAndChapter(Integer levelId, Integer chapterId) {
//
//        // 1. 레벨 존재하는지 확인
//        Level level = levelRepository.findByLevelId(levelId)
//                .orElseThrow(() -> new RuntimeException("레벨이 존재하지 않습니다."));
//        log.info("레벨:{}", level);
//
//        // 2. 챕터가 해당 레벨에 속하는지 검증
//        Chapter chapter = chapterRepository.findByChapterId(chapterId)
//                .orElseThrow(() -> new RuntimeException("챕터가 존재하지 않습니다."));
//        log.info("챕터:{}", chapter);
//
//        if (!chapter.getLevel().getLevelId().equals(level.getLevelId())) {
//            throw new RuntimeException("챕터가 해당 레벨에 속하지 않습니다.");
//        }
//
//        // 3. 챕터에 속한 단어 리스트 반환
//        return wordRepository.findByChapterChapterId(chapterId);
//    }

// 아직 사용 할 일 없음 (나중에도 필요없으면 servie 에서 삭제해야함-> 아니면 오류)
}
