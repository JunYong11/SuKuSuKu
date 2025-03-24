package com.web.sukusuku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.sukusuku.model.GameWord;

public interface RainGameRepository extends JpaRepository<GameWord, Integer> {

    // 레벨에 맞는 단어들을 찾아오는 쿼리
    List<GameWord> findByLevelId(int levelId);
}
