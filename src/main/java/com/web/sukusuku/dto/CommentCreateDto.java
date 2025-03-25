package com.web.sukusuku.dto;

import java.time.LocalDateTime;

import com.web.sukusuku.model.Comment;
import com.web.sukusuku.model.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDto {

    private String content;

    private Long postId;

    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public Comment toEntity(Post post) {
        return Comment.builder()
                .content(this.content)
                .post(post)  // ✅ Post 객체를 넘겨줘야 함
                .author(this.username)
                .build();
    }

}