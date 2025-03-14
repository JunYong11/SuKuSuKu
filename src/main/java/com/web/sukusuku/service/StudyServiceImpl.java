package com.web.sukusuku.service;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.model.*;
import com.web.sukusuku.repository.*;
import com.web.sukusuku.repository.StudyRepository; // db에서 데이터 갖고 와지는지 테스트용 (성공)
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Builder
@Slf4j
@Transactional // 트렌젝셔널 어노테이션 :비즈니스 로직이 수행되는 Service 계층에 쓰는 게 원칙
@RequiredArgsConstructor// final 생성자 주입
@Service // 서비스 단 class
public class StudyServiceImpl implements StudyService {

    private final LevelRepository levelRepository;
    private final ChapterRepository chapterRepository;
    private final WordRepository wordRepository;
// ========== all =======================
    @Override
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @Override
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @Override
    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    /** ✅ level choice  ===================================================
     *
     */
    // 특정 levelId에 맞는 챕터 목록을 가져오는 메서드
    @Override
    public List<ChapterDto> getChaptersByLevelId(Integer levelId) {
        log.info("서비스Impl(챕터조회): levelId={}", levelId);

        List<Chapter> chapters = chapterRepository.findByLevelLevelId(levelId);
        List<ChapterDto> chapterDtos = new ArrayList<>();
        int cumulativeWords = 0; // 누적 초기화

        for (Chapter chapter : chapters) {
            int wordCount = chapter.getWords().size();
            cumulativeWords += wordCount;

            // 이건 지금은 0 으로 하기
//            int reviewCount = studyProgressRepository
//                    .findByUserUsernameAndId2(username, chapter.getChapterId())
//                    .map(StudyProgress::getReviewCount)
//                    .orElse(0);

            ChapterDto dto = ChapterDto.builder()
                    .levelId(levelId)
                    .chapterId(chapter.getChapterId())
                    .chapterName(chapter.getChapterName())
                    .cumulativeWords(cumulativeWords)
                    .reviewCount(0)
                    .build();

            chapterDtos.add(dto);
        }

        return chapterDtos;
    }



    // 레벨 ID와 챕터 ID로 단어 리스트를 조회
    @Override
    public List<Word> getWordsByLevelAndChapter(Integer levelId, Integer chapterId) {
//        log.info("서비스I(단어 조회) : levelId: " + levelId + ", chapterId: " + chapterId);
        // 단어 갯수(누적 단어 숫자)
        /*1. 챕터 1의 누적 : 챕터1 단어 개수 , 챕터2의 누적 개수: 챕터1 + 챕터2 단어 개수,  챕터3의 누적 단어 개수 : 챕터1 + 챕터2 + 챕터3의 단어개수*/
        return wordRepository.findWordsByLevelAndChapter(levelId, chapterId);  // 레벨과 챕터에 맞는 단어 조회
    }




    /** ✅ study ============================================
     *
     */
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
