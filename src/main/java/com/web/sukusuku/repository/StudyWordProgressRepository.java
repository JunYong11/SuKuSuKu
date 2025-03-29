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

    Optional<StudyWordProgress> findByUserAndWord(User user, Word word);

    @Query("SELECT swp FROM StudyWordProgress swp WHERE swp.user = :user AND swp.word.chapter.chapterId = :chapterId")
    List<StudyWordProgress> findByUserAndChapterId(@Param("user") User user, @Param("chapterId") Integer chapterId);

    @Query("SELECT COUNT(swp) FROM StudyWordProgress swp WHERE swp.user = :user AND swp.word.chapter.chapterId = :chapterId AND swp.status = '안다'")
    int countKnownWords(@Param("user") User user, @Param("chapterId") Integer chapterId);
}
