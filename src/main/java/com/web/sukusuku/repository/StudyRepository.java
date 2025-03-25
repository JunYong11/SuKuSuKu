package com.web.sukusuku.repository;

import com.web.sukusuku.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Word, Integer> {

}
