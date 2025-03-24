package com.web.sukusuku.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.sukusuku.model.Name;
import com.web.sukusuku.service.NameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/name")
@RequiredArgsConstructor
public class NameController {

    private final NameService nameService;

    @GetMapping("/puzzle")
    public List<Name> getPuzzleWords(@RequestParam("level") int level) {
        int count;

        switch (level) {
            case 1: // 초급
                count = 16;
                break;
            case 2: // 중급
                count = 36;
                break;
            case 3: // 고급
                count = 64;
                break;
            default:
                throw new IllegalArgumentException("오류");
        }

        return nameService.getRandomKanji(count);
    }
}
