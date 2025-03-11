package com.web.sukusuku.service;

import com.web.sukusuku.model.Word;
import com.web.sukusuku.repository.StudyRepository;
import com.web.sukusuku.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true) // 클레스에 해당하는 모든 메서드들이 이 걸 수행함
@RequiredArgsConstructor// final 생성자 주입
@Service // 서비스 단 class
public class StudyService {
    private final StudyRepository studyRepository;
    //test용
    private final WordRepository wordRepository;

//    // 단어 전체 조회
//    public Word getPostById(Integer word_id) {
//        // 자동 repository
//        Optional<Word> findWord = studyRepository.findById(word_id);
//        return findWord.orElse(null);
//    }
    //    @Test

    public void WordRepositoryTest() {
        // 이미 저장되어 있는 데이터를 활용하거나 테스트 데이터를 추가할 수 있습니다.
        // 아래는 기존 데이터에서 word_id가 0부터 10 사이인 단어들을 조회하는 예제입니다.
        int start = 0;
        int end = 10;
        List<Word> words = wordRepository.findBywordIdBetween(start, end);

        // 로그로 출력하여 단어 개수와 상세 내용을 확인
//        log.info("Fetched {} words with word_id between 0 and 10: {}", words.size(), words);
        log.info("서비스 로그 확인");
        log.info(" words with word_id between 0 and 10: {}", words);

//			// 각 단어의 word_id가 0~10 범위에 있는지 검증
//			for (Word word : words) {
//				assertThat(word.getWord_id()).isBetween(0, 10);
//			}

    }
}
