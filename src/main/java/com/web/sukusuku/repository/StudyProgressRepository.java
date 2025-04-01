package com.web.sukusuku.repository;

import com.web.sukusuku.model.StudyProgress;
import com.web.sukusuku.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface StudyProgressRepository extends JpaRepository<StudyProgress, Integer> {
    Optional<StudyProgress> findByUserAndChapterId(User user, Integer chapterId);
    List<StudyProgress> findByUser_Username(String username);
}
