package com.web.sukusuku.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.sukusuku.dto.CommentCreateDto;
import com.web.sukusuku.dto.CommentUpdateDto;
import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.repository.CommentRepository;
import com.web.sukusuku.repository.PostRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public void createComment(Long postId, String content, String author) {
        Post post = postRepository.findById(postId).orElseThrow();

        Comment comment = Comment.builder()
                .post(post)
                .content(content)
                .author(author)
                .build();

        commentRepository.save(comment);
    }

    @Override
    public void createReply(Long postId, Long parentId, String content, String author) {
        Post post = postRepository.findById(postId).orElseThrow();
        Comment parent = commentRepository.findById(parentId).orElseThrow();

        Comment reply = Comment.builder()
                .post(post)
                .parent(parent)
                .content(content)
                .author(author)
                .build();

        commentRepository.save(reply);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdAndParentIsNull(postId);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void updateComment(Long id, String content, String username) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getAuthor().equals(username)) {
            throw new RuntimeException("수정 권한 없음");
        }

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }


}
