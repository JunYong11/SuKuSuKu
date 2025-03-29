package com.web.sukusuku.service;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.dto.StudyDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@Transactional // 트렌젝셔널 어노테이션 :비즈니스 로직이 수행되는 Service 계층에 쓰는 게 원칙
@RequiredArgsConstructor// final 생성자 주입
@Service // 서비스 단 class
public class StudyServiceImpl implements StudyService {
    private static final int DEFAULT_CHAPTER_RANGE = 5; // 챕터 누적 범위 기본값
    private final LevelRepository levelRepository;
    private final ChapterRepository chapterRepository;
    private final WordRepository wordRepository;
    private final StudyProgressRepository studyProgressRepository;
    private final StudyWordProgressRepository studyWordProgressRepository;

    /**✅ 메서드 ============================================*/
    // 챕터별 단어 누적
    @Override
    public int getStartChapterId(int levelId, int chapterId, int chapterRange) {
        Integer minChapterId = chapterRepository.findMinChapterIdByLevel(levelId);
        if (minChapterId == null) {
            throw new RuntimeException("레벨 " + levelId + "에 최소 챕터가 없습니다.");
        }

        return Math.max(chapterId - (chapterRange - 1), minChapterId);
    }


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
    public List<ChapterDto> getChaptersByLevelId(Integer levelId, User user) {
        log.info("📘 서비스 실행 - levelId: {}", levelId);
     // ✅ 레벨 존재 확인용 로그
        Optional<Level> level = levelRepository.findById(levelId);
        log.info("✅ 레벨 존재 여부: {}", level.isPresent());
        List<Chapter> chapters = chapterRepository.findByLevelLevelId(levelId);
        log.info("📚 조회된 챕터 수: {}", chapters.size());

        if (chapters.isEmpty()) {
            log.warn("⚠️ 해당 레벨의 챕터가 없습니다.");
        }

        // ✅ 요거 추가!
        List<ChapterDto> chapterDtos = new ArrayList<>();

        for (Chapter chapter : chapters) {
            log.info("▶️ 챕터ID: {}, 챕터이름: {}", chapter.getChapterId(), chapter.getChapterName());

            int startChapterId = getStartChapterId(levelId, chapter.getChapterId(), DEFAULT_CHAPTER_RANGE);
            int cumulativeWords = wordRepository.countWordsBetweenChapters(
                    levelId, startChapterId, chapter.getChapterId()
            );

            Optional<StudyProgress> progressOpt = studyProgressRepository.findByUserAndChapterId(user, chapter.getChapterId());
            int reviewCount = progressOpt.map(StudyProgress::getReviewCount).orElse(0);

            chapterDtos.add(
                    ChapterDto.builder()
                            .levelId(levelId)
                            .chapterId(chapter.getChapterId())
                            .chapterName(chapter.getChapterName())
                            .cumulativeWords(cumulativeWords)
                            .reviewCount(reviewCount)
                            .build()
            );
        }

        return chapterDtos;
    }


    /** ✅ study ============================================
     *
     */
// --------------------- study(회독) ----------------------------------------------
// 레벨초이스에서 챕터선택하면 작동 1(처음 공부)
@Override
public void startStudy(User user, Integer levelId, Integer chapterId) {
    log.info("서I(startStudy) 시작");
    // 해당 사용자와 챕터의 학습 진행 상태(StudyProgress)가 있는지 확인
    Optional<StudyProgress> existingProgress = studyProgressRepository.findByUserAndChapterId(user, chapterId);
    log.info("서I(startStudy):existingProgress={}", existingProgress);
    //진행 상태가 없으면 새로운 학습 진행 데이터 생성 및 저장
    if (existingProgress.isEmpty()) { // empty이거나 status가 진행중이 아니면 갖고 와야할 거 같은데?!
        StudyProgress progress = StudyProgress.builder()
                .user(user)
                .chapterId(chapterId)
                .status(StudyProgress.Status.진행_중)
                .reviewCount(0)
                .lastReviewedAt(LocalDateTime.now())
                .build();

        studyProgressRepository.save(progress);
        // 여기서 챕터안의 단어 조절하기(위의 getStartChapterId 메서드에서)
        int startChapterId = getStartChapterId(levelId, chapterId, DEFAULT_CHAPTER_RANGE);
        log.info("챕터 범위: {} ~ {}", startChapterId, chapterId);

        // 범위 내 단어 가져오기
        List<Word> allWords = wordRepository.findWordsBetweenChapters(levelId, startChapterId, chapterId);

        //단어 랜덤으로 섞기
        Collections.shuffle(allWords);
        // 해당 챕터의 모든 단어를 가져와서 StudyWordProgress에 '모른다' 상태로 초기화하여 저장
        for (Word word : allWords) {
            // ✅ 중복 체크 후 저장
            boolean exists = studyWordProgressRepository.findByUserAndWord(user, word).isPresent();
            if (!exists) {
                // 학습 기록이 없을 때만 저장
                studyWordProgressRepository.save(
                        StudyWordProgress.builder()
                                .user(user)                                     // 사용자 정보 설정
                                .word(word)                                     // 단어 정보 설정
                                .status(StudyWordProgress.Status.모른다)       // 초기 상태를 '모른다'로 설정
                                .build()
                );
            } else {
                // 이미 존재하는 경우 로그 출력 (선택사항)
                log.info("이미 존재하는 단어입니다: wordId={}, user={}", word.getWordId(), user.getUsername());
            }
        }
        log.info("A단어 등록 완료 - 등록된 단어 개수: {}", allWords.size());
    }
}
    // 레벨초이스에서 챕터선택하면 작동 2(학습 이력이 있을 때)
    @Override
    public List<WordDto> getRemainingWordsByChapter(User user, Integer chapterId) {

        List<StudyWordProgress> progresses = studyWordProgressRepository.findByUserAndChapterId(user, chapterId);

        List<WordDto> remainingWords = new ArrayList<>();

        for (StudyWordProgress progress : progresses) {
            if (progress.getStatus() == StudyWordProgress.Status.모른다) {
                Word word = progress.getWord();
                remainingWords.add(new WordDto(
                        word.getWordId(),
                        word.getKanji(),
                        word.getHiragana(),
                        word.getMeaning()
                ));
            }
        }

        Collections.shuffle(remainingWords);

        log.info("남은 단어 개수: {}", remainingWords.size());
        return remainingWords;
    }
    // 안다,모른다 버튼 누르면 작동
    @Override
    public int updateWordProgress(User user, WordProgressRequestDto dto) {
        Word word = wordRepository.findById(dto.getWordId())
                .orElseThrow(() -> new RuntimeException("단어 없음"));

        StudyWordProgress progress = studyWordProgressRepository.findByUserAndWord(user, word)
                .orElseThrow(() -> new RuntimeException("학습 기록 없음"));
        // ✅ 상태 변경 전 로그
        log.info("[서:updateWordProgress] 유저: {}, 단어 ID: {}, 기존 상태: {}", user.getUsername(), word.getWordId(), progress.getStatus());

        progress.setStatus(StudyWordProgress.Status.valueOf(dto.getStatus()));
        studyWordProgressRepository.save(progress);
        
        // 이 챕터에서 "안다"로 표시된 단어 수 계산
        int knownWordsCount = studyWordProgressRepository.countKnownWords(user, dto.getChapterId());
        int cumulativeWords = dto.getCumulativeWords(); // 전체 단어 갯수

    // ----------------------------------- 원래 있던것
        log.info("[서:updateWordProgress] 유저: {}, 챕터 ID: {}, 안다 개수: {}, 총 단어 수: {}",
                user.getUsername(), dto.getChapterId(), knownWordsCount, cumulativeWords);
            // 한챕터의 모든 단어를 안다고 하면
        if (knownWordsCount == cumulativeWords) {
            StudyProgress studyProgress = studyProgressRepository.findByUserAndChapterId(user, dto.getChapterId())
                    .orElseThrow(() -> new RuntimeException("챕터 진행 없음"));
            //  완료 상태로 변경 및 리뷰 카운트 증가
            studyProgress.setStatus(StudyProgress.Status.완료);
            studyProgress.setReviewCount(studyProgress.getReviewCount() + 1);
            studyProgress.setLastReviewedAt(LocalDateTime.now());
            // ✅ 리뷰 카운트 증가 로그
            log.info("[서:updateWordProgress] 유저: {}, 챕터 ID: {}, 학습 완료! 리뷰 카운트 증가: {}",
                    user.getUsername(), dto.getChapterId(), studyProgress.getReviewCount());

            studyProgressRepository.save(studyProgress);
            // 다시 학습을 위해 모른다로 초기화하는 로직 ----------------
            // resetStudyWordProgress(user, dto.getChapterId());
            resetChapterProgress(user, dto.getChapterId());

            log.info("[서:updateWordProgress] 유저: {}, 챕터 ID: {} 완료 - 리뷰 카운트 증가", user.getUsername(), dto.getChapterId());


//            return ; // 프론트에 학습 완료 신호 보내기

        }
        return knownWordsCount;
    }

    // (아래의)리셋 기능을 별도의 퍼블릭 메서드로 노출
    @Override
    public void resetChapterProgress(User user, Integer chapterId) {
        resetStudyWordProgress(user, chapterId);
    }

    // 모든 단어 상태를 "모른다"로 리셋
    private void resetStudyWordProgress(User user, Integer chapterId) {
        List<StudyWordProgress> progresses = studyWordProgressRepository.findByUserAndChapterId(user, chapterId);

        progresses.forEach(p -> {
            p.setStatus(StudyWordProgress.Status.모른다);
            studyWordProgressRepository.save(p);
        });
        log.info("[서]resetStudyWordProgress: 단어 상태 초기화 완료 - chapterId: {}", chapterId);
    }


}
