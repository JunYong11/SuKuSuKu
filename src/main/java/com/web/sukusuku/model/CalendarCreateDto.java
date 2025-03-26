package com.web.sukusuku.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarCreateDto {
	
	private LocalDate selectDate;
	private Long projectId;
	private String schedule;
	private String memo;
	
}
