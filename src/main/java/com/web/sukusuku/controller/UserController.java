package com.web.sukusuku.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.sukusuku.model.User;
import com.web.sukusuku.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    // 회원가입 폼으로 이동
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); 
        return "user/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {

    	log.info("user:{}",user);
        // 중복 아이디 검사
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다.");
            return "user/register";  // 그대로 register 페이지로 이동하면서 메시지 전달
        }

        // 성공 처리
        userService.register(user);
        return "redirect:/";  // 성공하면 로그인 페이지로 이동
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        HttpServletRequest request) {

        HttpSession session = request.getSession(true); // 기존 세션이 있으면 사용, 없으면 새로 생성

        if (session.getAttribute("loginUser") != null) {
            return "redirect:/home";
        }

        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            model.addAttribute("loginError", "존재하지 않는 아이디입니다.");
            return "home";
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {
            model.addAttribute("loginError", "비밀번호가 틀렸습니다.");
            return "home";
        }

        // 기존 세션 무효화 후 새 세션 생성
        session.invalidate();
        session = request.getSession(true);
        session.setAttribute("loginUser", user);

        return "redirect:/";
    }




    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 날리기
        return "redirect:/"; // 홈으로 이동
    }


}
