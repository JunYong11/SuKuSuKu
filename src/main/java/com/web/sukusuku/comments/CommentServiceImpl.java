package com.web.sukusuku.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment saveComment(CommentCreateDto commentCreateDto) {
        Comment comment = commentCreateDto.toEntity(); // DTO -> 엔티티 변환
        commentRepository.save(comment); // DB에 저장
        log.info("댓글 저장 완료! ID: {}", comment.getCommentId());
		return comment;
    }

	@Override
	public void deleteCommentByUser(Long commentId, String username) {
	    Comment comment = commentRepository.findById(commentId)
	            .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

	        if (!comment.getUsername().equals(username)) {
	            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
	        }

	        commentRepository.delete(comment);
	}

	@Override
	public List<Comment> getCommentsByPostId(Long postId) {
		  return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
	}

	// 수정과 저장만 담당
	@Transactional
	@Override
	public Comment updateComment(Long commentId, CommentUpdateDto dto, String username) {
	    Comment comment = commentRepository.findById(commentId)
	        .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

	    // 작성자 검증
	    if (!comment.getUsername().equals(username)) {
	        throw new RuntimeException("댓글 수정 권한이 없습니다.");
	    }

	    // 내용 수정 + 수정 시간 업데이트
	    comment.setUpdatedAt(LocalDateTime.now());
	    comment.setContent(dto.getContent());
	    // 수정된 걸 리턴
	    return commentRepository.save(comment);
	}

}
