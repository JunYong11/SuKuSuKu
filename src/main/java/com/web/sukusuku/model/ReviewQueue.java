package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_queue")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id", nullable = false, updatable = false)
    private Long queueId;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    private LocalDateTime addedAt;

    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
    }

    public enum ReviewStatus {
        대기,
        진행_중,
        완료
    }
}
