package com.web.sukusuku.repository;


import com.web.sukusuku.model.ReviewFailLog;
import com.web.sukusuku.model.User;
import com.web.sukusuku.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewFailLogRepository extends JpaRepository<ReviewFailLog, Long> {
    List<ReviewFailLog> findByUserAndWordOrderByFailedAtAsc(User user, Word word);
}