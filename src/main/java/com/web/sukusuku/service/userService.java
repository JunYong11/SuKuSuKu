package com.web.sukusuku.service;

import org.springframework.stereotype.Service;

import com.web.sukusuku.dto.userCreateDto;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.userRepository;

@Service
public class userService {
	
//	@Autowired	// 필드 주입
	private userRepository userrepository;
	
	public void saveUser(userCreateDto usercreatedto) {
		User user = new User();
		user.setName(usercreatedto.getName());
		user.setPassword(usercreatedto.getPassword());
		user.setEmail(usercreatedto.getEmail());
		user.setLevel(usercreatedto.getLevel());
		userrepository.save(user);
	}

	
}
