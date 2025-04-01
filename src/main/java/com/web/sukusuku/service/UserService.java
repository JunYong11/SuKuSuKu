package com.web.sukusuku.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.sukusuku.dto.UserUpdateDto;
import com.web.sukusuku.model.Project;
import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.ProjectRepository;
import com.web.sukusuku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public void updateUser(User user) {
        userRepository.save(user);
    }
    // 회원가입 메서드
    public void register(User user) {
        // 아이디 중복 확인 (있다면 예외)
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        user.setUserRole("USER"); 
        user.setRegisterDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());
        
        // 	회원 가입시 디폴트로 프로필 사진과 프로젝트(분류없음) 추가해주는 코드
        user.setProfileImage("profile1.jpg");
        
        Project project = new Project();
        
        project.setUser(user);
        project.setProjectName("분류없음");
        
        userRepository.save(user);
        projectRepository.save(project);
    }
   
    public boolean existsByUsername(String username) {
        return userRepository.existsById(username);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }
    
    // 프로필 업데이트
	public void updateProfileImage(String username, String profileImage) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUsername(username);
	    user.setProfileImage(profileImage);
	    userRepository.save(user);
	}
	
	@Transactional
	// 유저 정보 수정
	 public void mypageUpdateUser(UserUpdateDto userUpdateDto) {
		 
		 User user = userRepository.findByUsername(userUpdateDto.getUsername());
	     user.setName(userUpdateDto.getName());
	     user.setLevel(userUpdateDto.getLevel());
	     
	 }
    
}
