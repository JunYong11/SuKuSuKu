package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leveltest")
@Data
@NoArgsConstructor
@ToString(exclude = {"user", "level"})
public class LevelTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testId;

    private Integer score;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "level_id", insertable = false, updatable = false)
    private Level level;
}
