package com.web.sukusuku.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.sukusuku.model.UploadFile;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {
	
	List<UploadFile> findByPostId(Long postId);

}
