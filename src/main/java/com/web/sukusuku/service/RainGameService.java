package com.web.sukusuku.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.GameWord;
import com.web.sukusuku.model.ReviewQueue;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.RainGameRepository;
import com.web.sukusuku.repository.ReviewQueueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RainGameService {

    private final RainGameRepository rainGameRepository;
    private final ReviewQueueRepository reviewQueueRepository;

    // 레벨에 맞는 단어 가져오기
    public List<GameWord> getWordsByLevel(int levelId) {
            List<GameWord> words = rainGameRepository.findByLevelId(levelId);
            return new ArrayList<>(new HashSet<>(words));
        }


    // 부분일치 처리 (입력값과 의미를 비교)
    public boolean isCorrectAnswer(String input, String correctMeaning) {
        return correctMeaning.contains(input); // 의미에 입력값이 포함되면 정답 처리
    }
    
    
    
	public List<GameWord> getUsername(User user) {
	
	List<ReviewQueue> reviewUser = reviewQueueRepository.findByUser_Username(user.getUsername());
	log.info("user: {}",reviewUser);
	List<GameWord> gamewords = new ArrayList<>();
	for(int i=0;i<reviewUser.size();i++) {
		GameWord gameword = new GameWord();
		gameword.setKanji(reviewUser.get(i).getWord().getKanji());
		gameword.setMeaning(reviewUser.get(i).getWord().getMeaning());
		
		gamewords.add(gameword);
	}
	return gamewords;
	}
}