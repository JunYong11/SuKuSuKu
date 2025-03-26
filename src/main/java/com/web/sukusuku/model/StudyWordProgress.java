package com.web.sukusuku.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "study_word_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username", "word_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyWordProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime lastReviewedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.lastReviewedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastReviewedAt = LocalDateTime.now();
    }

    public enum Status {
        안다,
        모른다
    }
}
