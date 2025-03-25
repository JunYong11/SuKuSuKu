package com.web.sukusuku.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class ChapterDto {
    private Integer levelId;
    private Integer chapterId;
    private String chapterName;
    private Integer cumulativeWords; //누적 단어 갯수
    private Integer reviewCount; // 회독 횟수

    public ChapterDto(Integer levelId, Integer chapterId, String chapterName, Integer cumulativeWords, Integer reviewCount) {
        this.levelId = levelId;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.cumulativeWords = cumulativeWords;
        this.reviewCount = reviewCount;
    }

    // Getter, Setter methods...
}
