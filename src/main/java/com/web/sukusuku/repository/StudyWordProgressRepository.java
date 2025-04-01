package com.web.sukusuku.repository;


import com.web.sukusuku.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyWordProgressRepository extends JpaRepository<StudyWordProgress, Long> {
    //특정 단어 1개 학습 기록 조회
    Optional<StudyWordProgress> findByUserAndChapterIdAndWord(User user, Integer chapterId,Word word);
    // 챕터 아이디로 학습 기록 조회(챕터 내 모든 단어 학습 기록 조회,챕터 리셋할 때 전체 진행 조회)
    @Query("SELECT swp FROM StudyWordProgress swp WHERE swp.user = :user AND swp.word.chapter.chapterId = :chapterId")
    List<StudyWordProgress> findByUserAndChapterId(@Param("user") User user, @Param("chapterId") Integer chapterId);
    //챕터 별 안다고 표시된 단어 개수 계산
    @Query("SELECT COUNT(swp) FROM StudyWordProgress swp WHERE swp.user = :user AND swp.chapterId= :chapterId AND swp.status = '안다'")
    int countKnownWords(@Param("user") User user, @Param("chapterId") Integer chapterId);
    // 챕터 범위 내의 모든 "안다" 단어 개수 계산
    @Query("SELECT COUNT(swp) FROM StudyWordProgress swp " +
            "JOIN swp.word w " +
            "WHERE swp.user = :user " +
//            "AND w.level.levelId = :levelId " +
            "AND w.chapter.chapterId BETWEEN :startChapterId AND :endChapterId " +
            "AND swp.status = '안다'")
    int countKnownWordsBetweenChapters(@Param("user") User user, 
//                                      @Param("levelId") int levelId,
                                      @Param("startChapterId") int startChapterId, 
                                      @Param("endChapterId") int endChapterId);
}
