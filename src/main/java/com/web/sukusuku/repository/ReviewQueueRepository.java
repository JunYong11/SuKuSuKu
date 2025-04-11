package com.web.sukusuku.repository;

import com.web.sukusuku.model.ReviewQueue;
import com.web.sukusuku.model.User;
import com.web.sukusuku.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewQueueRepository extends JpaRepository<ReviewQueue, Long> {
    Optional<ReviewQueue> findByUserAndWord(User user, Word word);

	void deleteByUserAndWord_WordId(User user, int wordId);

	List<Integer> findWordIdsByUserAndFailCount(User user, int unknownCount);
}