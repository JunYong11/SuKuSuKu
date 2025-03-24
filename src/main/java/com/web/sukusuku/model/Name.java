package com.web.sukusuku.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "kanji_word")
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String kanji;

    private String answer;

    private String meaning;
    
    public Name(String kanji, String answer) {
        this.kanji = kanji;
        this.answer = answer;
    }
}
