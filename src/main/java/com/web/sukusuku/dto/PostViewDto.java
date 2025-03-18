package com.web.sukusuku.dto;

import java.time.LocalDateTime;

import com.web.sukusuku.model.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostViewDto {
    private Long id;
    private Category category;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int views;
    private boolean isAuthor;
}
