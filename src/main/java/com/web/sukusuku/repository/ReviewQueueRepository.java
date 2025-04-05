package com.web.sukusuku.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.web.sukusuku.model.ReviewQueue;
import com.web.sukusuku.model.User;
import com.web.sukusuku.model.Word;

public interface ReviewQueueRepository extends JpaRepository<ReviewQueue, Long> {
    
//    @Query("SELECT rq.word.wordId FROM ReviewQueue rq")
//    List<Integer> findAllWordIds();
    Optional<ReviewQueue> findByUserAndWord(User user, Word word);
    // fail_count 에 따라 wordId 갖고 오기
    @Query("SELECT rq.word.wordId FROM ReviewQueue rq WHERE rq.user = :user AND rq.failCount >= :failCount")
    List<Integer> findWordIdsByUserAndFailCount(@Param("user") User user, @Param("failCount") int failCount);
    // 제거하기 버튼 누르면 wordId랑 일치하는 것 삭제
    void deleteByUserAndWord_WordId(User user, Integer wordId);
    
    //List<Integer> findWordIdByUserName(String user);
    List<ReviewQueue> findByUser_Username(String username);

}