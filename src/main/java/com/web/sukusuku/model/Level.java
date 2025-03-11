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
@Table(name = "level")
public class Level {

    @Id
    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "level_name")
    private String levelName;

    @OneToMany(mappedBy = "level")
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "level")
    private List<LevelTest> levelTests = new ArrayList<>();
}

