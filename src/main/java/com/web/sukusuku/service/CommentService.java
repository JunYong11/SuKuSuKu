package com.web.sukusuku.service;

import java.util.List;

import com.web.sukusuku.dto.CommentCreateDto;
import com.web.sukusuku.dto.CommentUpdateDto;
import com.web.sukusuku.model.Comment;

public interface CommentService {

	List<Comment> getCommentsByPostId(Long postId);

	void createReply(Long postId, Long parentId, String content, String author);

	void createComment(Long postId, String content, String author);

	Comment getCommentById(Long id);

	void deleteComment(Long id);

	void updateComment(Long id, String content, String username);
	


}
