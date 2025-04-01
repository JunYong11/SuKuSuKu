package com.web.sukusuku.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class WordProgressRequestDto {
    private Integer wordId;      // 단어 ID
    private String status;       // 안다 / 모른다
    private Integer chapterId;   // 챕터 ID
    private Integer cumulativeWords; // 전체 단어 갯수
}
