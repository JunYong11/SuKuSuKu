package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "studyprogress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyProgress {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "progress_id", nullable = false)
   private Integer progressId;

   @ManyToOne
   @JoinColumn(name = "username", nullable = false)
   private User user;

   @Column(name = "id2", nullable = false)
   private Integer chapterId;

   @Enumerated(EnumType.STRING)
   private Status status;

   @Column(name = "review_count", nullable = false)
   private Integer reviewCount;

//   @Column(name = "last_reviewed_at", nullable = false)
   private LocalDateTime lastReviewedAt;

   public enum Status {
      대기,
      진행_중,
      완료
   }
}
