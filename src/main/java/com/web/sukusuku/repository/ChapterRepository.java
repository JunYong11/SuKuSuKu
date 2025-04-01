package com.web.sukusuku.repository;

import com.web.sukusuku.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    Optional<Chapter> findByChapterId(Integer chapterId); // chapterId에 해당하는 챕터 가져오기
    List<Chapter> findByLevelLevelId(Integer levelId); // levelId에 해당하는 챕터들을 가져오는 메서드
    @Query("SELECT c.level.levelId FROM Chapter c WHERE c.chapterId = :chapterId")
    Integer findLevelIdByChapterId(@Param("chapterId") Integer chapterId); // chpaterId로 레벨 찾기
    //레벨ID 기준으로 가장 작은 chapterId
    @Query("SELECT MIN(c.chapterId) FROM Chapter c WHERE c.level.levelId = :levelId")
    Integer findMinChapterIdByLevel(@Param("levelId") Integer levelId);

}