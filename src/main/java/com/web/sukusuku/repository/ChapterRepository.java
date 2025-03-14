package com.web.sukusuku.repository;

import com.web.sukusuku.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    Optional<Chapter> findByChapterId(Integer chapterId); // chapterId에 해당하는 챕터 가져오기
    List<Chapter> findByLevelLevelId(Integer levelId); // levelId에 해당하는 챕터들을 가져오는 메서드

}