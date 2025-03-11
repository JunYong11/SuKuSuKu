package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {

    @Id
    private String username;

    private String password;
    private String name;
    private String email;
    private String level;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Calendar> calendars = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<StudyProgress> studyProgresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReviewQueue> reviewQueues = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<LevelTest> levelTests = new ArrayList<>();
}


