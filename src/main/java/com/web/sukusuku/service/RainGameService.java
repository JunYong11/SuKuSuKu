package com.web.sukusuku.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.GameWord;
import com.web.sukusuku.repository.RainGameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RainGameService {

    private final RainGameRepository rainGameRepository;

    // 레벨에 맞는 단어 가져오기
    public List<GameWord> getWordsByLevel(int levelId) {
        List<GameWord> words = rainGameRepository.findByLevelId(levelId);

        // 중복 제거 (필터링 후 단어 리스트에 저장)
        Set<GameWord> uniqueWords = new HashSet<>(words);
        return new ArrayList<>(uniqueWords); // 중복된 단어는 제거되고 유니크한 단어만 반환
    }

    // 부분일치 처리 (입력값과 의미를 비교)
    public boolean isCorrectAnswer(String input, String correctMeaning) {
        return correctMeaning.contains(input); // 의미에 입력값이 포함되면 정답 처리
    }
}
