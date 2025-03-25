package com.web.sukusuku.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.web.sukusuku.model.Name;
import com.web.sukusuku.repository.NameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NameService {

    private final NameRepository nameRepository;

    public List<Name> getRandomKanji(int count) {
        List<Name> allKanji = nameRepository.findAll();

        Collections.shuffle(allKanji);
        
        return allKanji.subList(0, count);
    }
}
