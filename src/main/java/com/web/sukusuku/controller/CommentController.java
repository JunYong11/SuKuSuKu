package com.web.sukusuku.controller;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.CommentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/create")
    public String createComment(@RequestParam("postId") Long postId,
                                @RequestParam("content") String content,
                                HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/users/login";
        }

        commentService.createComment(postId, content, loginUser.getUsername());
        return "redirect:/posts/view/" + postId;
    }

    // 대댓글 생성
    @PostMapping("/reply")
    public String createReply(@RequestParam("postId") Long postId,
                              @RequestParam("parentId") Long parentId,
                              @RequestParam("content") String content,
                              HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/users/login";
        }

        commentService.createReply(postId, parentId, content, loginUser.getUsername());
        return "redirect:/posts/view/" + postId;
    }
    @PostMapping("/edit-api/{id}")
    @ResponseBody
    public ResponseEntity<?> updateCommentApi(@PathVariable("id") Long id,
                                              @RequestParam("content") String content,
                                              HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return ResponseEntity.status(404).body("댓글이 존재하지 않습니다.");
        }

        if (!comment.getAuthor().equals(loginUser.getUsername())) {
            return ResponseEntity.status(403).body("수정 권한이 없습니다.");
        }

        commentService.updateComment(id, content, loginUser.getUsername());
        return ResponseEntity.ok().build(); // 200 OK
    }

    // 댓글 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/users/login";  // 로그인 안 돼있으면 튕기기
        }

        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return "redirect:/posts/list?error=commentNotFound";
        }

        if (!comment.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list?error=permissionDenied";
        }

        commentService.deleteComment(id);

        return "redirect:/posts/view/" + comment.getPost().getId();
    }
}
