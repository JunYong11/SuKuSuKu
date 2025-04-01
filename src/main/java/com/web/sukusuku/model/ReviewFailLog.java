package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_fail_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewFailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    private LocalDateTime failedAt;

    @PrePersist
    public void prePersist() {
        this.failedAt = LocalDateTime.now();
    }
}
