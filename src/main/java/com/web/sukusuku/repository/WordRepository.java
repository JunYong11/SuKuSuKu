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
    // level과 chapter id로 단어 불러오기(순서대로) : 그 챕터에 있는 단어만
@Query("SELECT w FROM Word w WHERE w.chapter.level.levelId = :levelId AND w.chapter.chapterId = :chapterId")
List<Word> findWordsByLevelAndChapter(@Param("levelId") Integer levelId, @Param("chapterId") Integer chapterId);
    // 챕터 범위(chapterRange = 5;) 별로 단어 불러오기
    @Query("SELECT w FROM Word w " +
            "WHERE w.chapter.level.levelId = :levelId " +
            "AND w.chapter.chapterId BETWEEN :minChapterId AND :maxChapterId")
    List<Word> findWordsBetweenChapters(@Param("levelId") Integer levelId,
                                        @Param("minChapterId") Integer minChapterId,
                                        @Param("maxChapterId") Integer maxChapterId);
    // 위의 것을 단어개 수로 불러오는  쿼리(레벨초이스에서 사용)
    @Query("SELECT COUNT(w) FROM Word w WHERE w.chapter.level.levelId = :levelId AND w.chapter.chapterId BETWEEN :minChapterId AND :maxChapterId")
    int countWordsBetweenChapters(@Param("levelId") Integer levelId,
                                  @Param("minChapterId") Integer minChapterId,
                                  @Param("maxChapterId") Integer maxChapterId);

}
