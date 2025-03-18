package com.web.sukusuku.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.sukusuku.dto.CommentCreateDto;
import com.web.sukusuku.dto.CommentResponseDto;
import com.web.sukusuku.dto.CommentUpdateDto;
import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.CommentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    //댓글 리스트 조회
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
    		@PathVariable Long postId,
    		@RequestBody CommentCreateDto dto,
    		HttpSession session) {
        
        // 테스트용 임시 로그인 유저 추가 (나중에 삭제하고 실제 로그인으로)
        User tempUser = new User();
        tempUser.setUsername("테스트유저");
        session.setAttribute("loginUser", tempUser);

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // username, postId 주입!
        dto.setPostId(postId);
        dto.setUsername(loginUser.getUsername());
        
    	Comment comment = commentService.saveComment(dto);
        CommentResponseDto response = new CommentResponseDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getUsername(),
                comment.getPostId(),
                comment.getCreatedAt()
        );
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
    		@PathVariable Long commentId,
    		@PathVariable Long postId,
    		HttpSession session) {

        // 임시 로그인 유저
        User tempUser = new User();
        tempUser.setUsername("테스트유저");
        session.setAttribute("loginUser", tempUser);

//        User loginUser = (User) session.getAttribute("loginUser");
//
//        if (loginUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다!");
//        }
//
//        commentService.deleteCommentByUser(commentId, loginUser.getUsername());
        commentService.deleteCommentByUser(commentId, tempUser.getUsername());

        return ResponseEntity.ok("댓글 삭제 완료!");
    }
    // 댓글 업데이트
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
    		@PathVariable Long commentId,
    		@PathVariable Long postId,
    		@RequestBody CommentUpdateDto dto,                               
    		HttpSession session) {
        // 테스트용 임시 로그인 유저 추가
        User tempUser = new User();
        tempUser.setUsername("테스트유저");
        session.setAttribute("loginUser", tempUser);

//    	// 로그인 유저 확인
//        User loginUser = (User) session.getAttribute("loginUser");
//
//        if (loginUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요!");
//        }

        // 수정 후 댓글 다시 조회
//        Comment updatedComment = commentService.updateComment(commentId, dto, loginUser.getUsername());
        Comment updatedComment = commentService.updateComment(commentId, dto, tempUser.getUsername());
        // DTO로 변환해서 응답
        CommentResponseDto response = new CommentResponseDto(
                updatedComment.getCommentId(),
                updatedComment.getContent(),
                updatedComment.getUsername(),
                updatedComment.getPostId(),
                updatedComment.getUpdatedAt() // 업데이트된 시간 반환
        );
        return ResponseEntity.ok(response);
    }

    
}
