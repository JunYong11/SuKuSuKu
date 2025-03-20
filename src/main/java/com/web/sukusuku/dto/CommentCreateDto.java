package com.web.sukusuku.dto;

import java.time.LocalDateTime;

import com.web.sukusuku.model.Comment;

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
    public Comment toEntity() {
        return Comment.builder()
                .content(this.content)
                .postId(this.postId)
                .username(this.username)
                .build();
    }
}