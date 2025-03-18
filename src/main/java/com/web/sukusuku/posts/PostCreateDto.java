package com.web.sukusuku.posts;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class PostCreateDto {
	@NotBlank(message="제목필수")
	@Size(min = 1, message = "제목은 1자 이상 200자 이하로 입력해 주세요")
	private String title;		// 제목
	@NotBlank(message="내용필수")
	@Size(min = 1)
	private String content;		// 내용
	
    private String category;   // 자유게시판 / 질문게시판 / 자료게시판

    private Boolean secret = false;

    public Post toEntity(String author) {
        try {
            Category cat = Category.valueOf(this.category);  // Enum 매핑 조심!
            return Post.builder()
                    .title(this.title)
                    .content(this.content)
                    .category(cat)
                    .secret(this.secret != null ? this.secret : false)
                    .views(0)
                    .author(author)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("카테고리 값이 잘못되었습니다: " + this.category);
        }
    }
    }
