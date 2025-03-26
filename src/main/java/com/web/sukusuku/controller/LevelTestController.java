package com.web.sukusuku.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.sukusuku.model.User;
import com.web.sukusuku.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/leveltest")
@RequiredArgsConstructor
public class LevelTestController {

    private final UserService userService;

    @GetMapping("/level")
    public String getMethodLevel() {
       return "leveltest/leveltest";
    }

    @GetMapping("/problem")
    public String getMethod() {
       return "leveltest/problem";
    }

    @GetMapping("/result")
    public String getMethodResult() {
       return "leveltest/result";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    // 레벨 업데이트 POST
    @PostMapping("/updateLevel")
    @ResponseBody
    public String updateUserLevel(@RequestBody Map<String, String> requestData, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            log.warn("레벨 업데이트 실패: 로그인 유저 없음");
            return "로그인 필요";
        }

        String level = requestData.get("level");

        if (level == null || level.isEmpty()) {
            log.warn("레벨 업데이트 실패: 레벨 값 없음");
            return "레벨 값 없음";
        }

        log.info("레벨 업데이트 - 유저: {}, 새로운 레벨: {}", loginUser.getUsername(), level);

        loginUser.setLevel(level);
        loginUser.setUpdateDate(LocalDateTime.now());

        userService.updateUser(loginUser);

        return "성공";
    }
}
