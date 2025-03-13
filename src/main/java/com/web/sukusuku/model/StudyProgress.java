package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "studyprogress")
@Data
@NoArgsConstructor
@ToString(exclude = {"user"})
public class StudyProgress {

   @Id
   @Column(name = "progress_id")
   private Integer progressId;

   @Enumerated(EnumType.STRING)
   private Status status;

   @Column(name = "review_count", nullable = false)
   private Integer reviewCount;

   @Column(name = "last_reviewed_at")
   private LocalDateTime lastReviewedAt;

   @ManyToOne
   @JoinColumn(name = "username")
   private User user;

   @Column(name = "id2", nullable = false)
   private Integer id2;

   public enum Status {
      대기,
      진행_중,
      완료
   }
}
