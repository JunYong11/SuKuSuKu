package com.web.sukusuku.dto;

import com.web.sukusuku.model.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data

public class StudyDto {
    private Integer levelId;
    private String levelName;
    private Integer chapterId;
    private String chapterName;
    private List<Word> words; // 단어 목록
}