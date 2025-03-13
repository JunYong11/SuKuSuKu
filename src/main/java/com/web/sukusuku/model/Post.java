package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@ToString(exclude = {"user", "comments", "attachments"})
public class Post {

    @Id
    @Column(name = "post_id")
    private Integer postId;

    private String category;
    private String title;
    private String content;

    @Column(name = "is_secret")
    private Boolean isSecret;

    private Integer views;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Attachment> attachments = new ArrayList<>();
}
