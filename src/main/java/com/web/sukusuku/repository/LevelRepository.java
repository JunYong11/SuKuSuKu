package com.web.sukusuku.repository;

import com.web.sukusuku.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    Optional<Level> findByLevelId(Integer levelId);

}
