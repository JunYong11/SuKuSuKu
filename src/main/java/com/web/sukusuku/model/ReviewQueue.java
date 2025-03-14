package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review_queue")
@Data
@NoArgsConstructor
@ToString(exclude = {"user"})
public class ReviewQueue {

    @Id
    @Column(name = "queue_id")
    private Integer queueId;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status")
    private ReviewStatus reviewStatus;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(name = "id2", nullable = false)
    private Integer id2;

    public enum ReviewStatus {
        대기,
        진행_중,
        완료
    }
}
