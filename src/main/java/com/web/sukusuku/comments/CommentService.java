package com.web.sukusuku.comments;

import java.util.List;

public interface CommentService {
    Comment saveComment(CommentCreateDto commentCreateDto);

	void deleteCommentByUser(Long commentId, String username);

	List<Comment> getCommentsByPostId(Long postId);
	
	Comment updateComment(Long commentId, CommentUpdateDto dto, String username);


}
