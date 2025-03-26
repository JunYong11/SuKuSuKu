package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendar")
@Data
@NoArgsConstructor
@ToString(exclude = {"user","project"})
public class Calendar {

    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // project_id가 자동 증가하도록 설정
    private Long calendarId;

    @Column(name = "schedule")
    private String schedule;
    
    @Column(name = "memo")
    private String memo;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "calendar_check", nullable = false)
    private boolean check;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;

    public void setCompleted(boolean check) {
        this.check = check;
    }
}
