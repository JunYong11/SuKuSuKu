package com.web.sukusuku.dto;

import java.time.LocalDateTime;

import com.web.sukusuku.model.Category;
import com.web.sukusuku.model.Post;
import com.web.sukusuku.model.User;

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
    private String secretPassword;
    
    public Post toEntity(String author) {
        try {
            // Category가 Enum이기 때문에, 문자열 값을 Enum으로 변환해야 함
            Category cat = Category.valueOf(this.category); // Enum 매핑
            return Post.builder()
                    .title(this.title)
                    .content(this.content)
                    .category(cat)  // 카테고리 값 매핑
                    .secret(this.secret != null ? this.secret : false)  // 기본값 처리
                    .secretPassword(this.secret != null && this.secret ? this.secretPassword : null)
                    .views(0)  // 기본 조회수
                    .author(author)  // 세션에서 받은 작성자 이름
                    .build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("카테고리 값이 잘못되었습니다: " + this.category);
        }
    }
    }
