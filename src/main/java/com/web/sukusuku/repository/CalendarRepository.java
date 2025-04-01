package com.web.sukusuku.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.sukusuku.model.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

	List<Calendar> findByUser_Username(String username);

}
