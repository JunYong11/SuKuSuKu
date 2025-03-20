package com.web.sukusuku.service;

import java.util.List;

import com.web.sukusuku.dto.CommentCreateDto;
import com.web.sukusuku.dto.CommentUpdateDto;
import com.web.sukusuku.model.Comment;

public interface CommentService {
    Comment saveComment(CommentCreateDto commentCreateDto);

	void deleteCommentByUser(Long commentId, String username);

	List<Comment> getCommentsByPostId(Long postId);
	
	Comment updateComment(Long commentId, CommentUpdateDto dto, String username);


}
