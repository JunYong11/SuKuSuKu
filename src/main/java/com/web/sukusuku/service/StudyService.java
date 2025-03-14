package com.web.sukusuku.service;

import com.web.sukusuku.dto.ChapterDto;
import com.web.sukusuku.model.Word;
import com.web.sukusuku.model.Level;
import com.web.sukusuku.model.Chapter;

import  com.web.sukusuku.repository.*;

import java.util.List;
import java.util.Optional;

public interface StudyService {
// -------------- All
    // 모든 레벨을 가져오는 메서드
    List<Level> getAllLevels();
    // 모든 챕터를 가져오는 메서드
    List<Chapter> getAllChapters();
    // 모든 단어를 가져오는 메서드
    List<Word> getAllWords();
// -------------------------------------

    // 특정 레벨에 맞는 챕터 목록을 가져오는 메서드
    //!! 나중에 회독 횟수도 할려면 다시 chapterDto 만들어야할듯 (user 정보가 필요함)
        //    List<ChapterDto> getChaptersByLevelId(Integer levelId, String username);
    List<ChapterDto> getChaptersByLevelId(Integer levelId); // 레벨에 속한 챕터들을 가져오는 메서드



    // 레벨 ID와 챕터 ID로 단어 리스트를 조회
    List<Word> getWordsByLevelAndChapter(Integer levelId, Integer chapterId);

}
