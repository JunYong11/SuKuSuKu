package com.web.sukusuku.repository;

import com.web.sukusuku.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Integer> {

    List<Word> findBywordIdBetween(int start, int end); // 테스트용 ( 나중에 삭제)
    List<Word> findByChapterChapterId(Integer chapterId);
//    List<Word> getWordsByLevelAndChapter(Integer levelId, Integer chapterId);
@Query("SELECT w FROM Word w WHERE w.chapter.level.levelId = :levelId AND w.chapter.chapterId = :chapterId")
List<Word> findWordsByLevelAndChapter(@Param("levelId") Integer levelId, @Param("chapterId") Integer chapterId);

}
