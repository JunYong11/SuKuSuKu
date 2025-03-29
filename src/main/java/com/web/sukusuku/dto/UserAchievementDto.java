package com.web.sukusuku.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserAchievementDto {
	
	private Long projectId;
	private Long resultcount;
	private Long truecount;
	private Double achievementRate; // 달성률 필드 추가

}
