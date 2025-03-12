package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "level")
@Getter
@Setter
@NoArgsConstructor
public class Level {

    @Id
    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "level_name")
    private String levelName;
}
