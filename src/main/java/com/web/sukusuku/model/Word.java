package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "word")
@NoArgsConstructor
@ToString(exclude = {"chapter"})
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Integer wordId;

    private String kanji;
    private String hiragana;
    private String meaning;

    @ManyToOne
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;
}
