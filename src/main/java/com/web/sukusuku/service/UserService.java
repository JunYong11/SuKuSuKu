package com.web.sukusuku.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.User;
import com.web.sukusuku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입 메서드
    public void register(User user) {
        // 아이디 중복 확인 (있다면 예외)
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        user.setUserRole("USER"); 
        user.setRegisterDate(LocalDateTime.now());
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsById(username);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findById(username);
    }
    
}
