package com.web.sukusuku.dto;

import lombok.Data;

@Data
public class UserCreateDto {
	
	private String username;
	private String password;
	private String name;
	private String email;
	private String level;
}
