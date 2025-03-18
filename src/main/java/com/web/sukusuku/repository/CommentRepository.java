package com.web.sukusuku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.sukusuku.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 기본 CRUD는 JpaRepository가 다 해줌

    // 게시글 기준으로 댓글 찾기
	List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

}
