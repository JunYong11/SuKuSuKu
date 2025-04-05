package com.web.sukusuku.controller;

import com.web.sukusuku.model.GameWord;
import com.web.sukusuku.model.User;
import com.web.sukusuku.service.RainGameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rainGame")
@RequiredArgsConstructor
public class RainGameController {

    private final RainGameService rainGameService;

    // 쉬움(N5, N4): 5, 4
    @GetMapping("/easy")
    public List<GameWord> getEasyWords() {
        return rainGameService.getWordsByLevel(5); // N5
    }

    // 보통(N3): 3
    @GetMapping("/normal")
    public List<GameWord> getNormalWords() {
        return rainGameService.getWordsByLevel(3);
    }

    // 어려움(N1, N2): 1, 2
    @GetMapping("/hard")
    public List<GameWord> getHardWords() {
        return rainGameService.getWordsByLevel(1); // N1
    }

    // 복습: 6
    @GetMapping("/review")
    public List<GameWord> getReview(@SessionAttribute(name = "loginUser", required = false) User loginUser ) {
    	return rainGameService.getUsername(loginUser); // 복습단어
    }
}
