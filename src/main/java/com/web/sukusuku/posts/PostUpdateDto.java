package com.web.sukusuku.posts;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUpdateDto {
	@NotNull(message = "카테고리를 선택해 주세요.")
    private Category category;
	  
	@NotBlank
    @Size(min = 1, message = "제목은 1자 이상 200자 이하로 입력해 주세요")
    private String title;

    @NotBlank
    @Size(min = 1)
    private String content;

    private boolean secret;
}
