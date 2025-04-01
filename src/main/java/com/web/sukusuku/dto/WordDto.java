package com.web.sukusuku.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordDto {
    private Integer wordId;
    private String kanji;
    private String hiragana;
    private String meaning;
}
