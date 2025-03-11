package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
//@Entity // db에 저장
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chapter")
public class Chapter {

    @Id
    @Column(name = "chapter_id")
    private Integer chapterId;

    @Column(name = "chapter_name")
    private String chapterName;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @OneToMany(mappedBy = "chapter")
    private List<Word> words = new ArrayList<>();
}

