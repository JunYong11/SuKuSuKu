package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_word")
public class GameWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_word_id")
    private Integer gameWordId;

    private String kanji;
    private String meaning;
    
    @Column(name = "level_id")
    private Integer levelId;  // N1=1, N2=2, N3=3, N4=4, N5=5
}
