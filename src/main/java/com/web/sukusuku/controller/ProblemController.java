package com.web.sukusuku.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.sukusuku.model.Problem;
import com.web.sukusuku.service.ProblemService;

@RestController
@RequestMapping("/leveltest")
public class ProblemController {

    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/problem")
    public List<Problem> getRandomProblems() {
        return problemService.getRandomProblems();
    }
}
