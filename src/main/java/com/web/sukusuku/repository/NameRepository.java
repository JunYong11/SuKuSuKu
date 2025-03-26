package com.web.sukusuku.repository;

import com.web.sukusuku.model.Name;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NameRepository extends JpaRepository<Name, Long> {

}
