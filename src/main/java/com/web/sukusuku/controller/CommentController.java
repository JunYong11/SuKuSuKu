package com.web.sukusuku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.CommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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

    // 댓글 수정 폼 이동
    @GetMapping("/edit/{id}")
    public String editCommentForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        Comment comment = commentService.getCommentById(id);
        if (!comment.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list"; // 권한 없음
        }

        model.addAttribute("comment", comment);
        return "comments/edit"; // 수정 페이지로 이동
    }

    // 댓글 수정 처리
    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable("id") Long id,
                                @RequestParam("content") String content,
                                HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            return "redirect:/posts/list?error=commentNotFound";
        }

        // 권한 확인
        if (!comment.getAuthor().equals(loginUser.getUsername())) {
            return "redirect:/posts/list?error=permissionDenied";
        }

        commentService.updateComment(id, content, loginUser.getUsername());

        // 해당 게시글 상세보기로 이동
        return "redirect:/posts/view/" + comment.getPost().getId();
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
