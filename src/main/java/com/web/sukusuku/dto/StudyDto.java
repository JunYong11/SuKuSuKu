package com.web.sukusuku.dto;

import com.web.sukusuku.model.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
// 아마 사용 하는 곳 없음(보고 나중에 삭제하기)
public class StudyDto {
    private Integer levelId;
//    private String levelName;
    private Integer chapterId;
//    private String chapterName;
    private List<Word> words; // 단어 목록
    private Integer cumulativeWords;// 챕터 전체 단어 개수
    private Integer collect; // 정답 갯수

}