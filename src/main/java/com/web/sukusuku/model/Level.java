package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "level")
@ToString(exclude = {"chapters", "levelTests"})
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
