package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@ToString(exclude = {"posts", "calendars", "studyProgresses", "reviewQueues", "levelTests"})
public class User {

    @Id
    private String username;

    private String password;
    private String name;
    private String email;
    private String level;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;
    
    @Column(name = "profile_image")
    private	String profileImage;

}
