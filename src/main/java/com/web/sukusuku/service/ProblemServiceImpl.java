package com.web.sukusuku.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.sukusuku.model.Problem;
import com.web.sukusuku.repository.ProblemRepository;

@Service
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;

    @Autowired
    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    public List<Problem> getRandomProblems() {
        List<Problem> result = new ArrayList<>();

        int[] levelIds = {5, 4, 3, 2, 1};

        for (int levelId : levelIds) {
            List<Problem> levelProblems = problemRepository.findRandomProblemsByLevel(levelId);
            result.addAll(levelProblems);
        }

        return result;
    }


}
