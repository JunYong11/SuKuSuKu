package com.web.sukusuku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.sukusuku.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	List<Project> findByUser_Username(String username);
	
	

}
