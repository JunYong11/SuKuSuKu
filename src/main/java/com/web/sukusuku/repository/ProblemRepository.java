package com.web.sukusuku.repository;

import com.web.sukusuku.model.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {

	@Query(value = "SELECT * FROM problem WHERE level_id = :levelId ORDER BY RAND() LIMIT 4", nativeQuery = true)
	List<Problem> findRandomProblemsByLevel(@Param("levelId") int levelId);


}
