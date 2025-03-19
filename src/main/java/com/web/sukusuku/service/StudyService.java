package com.web.sukusuku.service;

import com.web.sukusuku.dto.*;
import com.web.sukusuku.model.*;

import java.util.List;
import java.util.Optional;

public interface StudyService {

    // 모든 레벨 가지고 오기
    List<Level> getAllLevels();
    //레벨에 맞는 챕터 목록을 가져오는 메서드(레벨id로 챕터 목록 갖고오기)
    List<ChapterDto> getChaptersByLevelId(Integer levelId);

//    //챕터에서 나갔던 그 상태의 정보 불러오기 (user 정보와 챕터Id로 학습하던 정보 불러오기)
//        //!! user 말고 username이면 될거 같은데?!
//    Optional<StudyProgress> getProgress(User user, Integer chapterId);
//
//    //새로운 챕터 시작할 때 저장할 메서드(!! 이 메스드가 맞는지 나중에 다시 확인필요)
//    void createProgress(User user, Integer chapterId);
//
//    //공부 끝낼 때 그 상태를 저장하는 메섣,(!!이 메스드가 맞는지 나중에 다시 확인필요)
//    void updateProgress(User user, WordProgressRequestDto requestDto);
//
//    //챕터에서 나갔던 그 상태의 정보 불러오기(!! 위의 getProgress랑 뭐가 다르지?)
//    List<Word> getRemainingWords(Integer chapterId, StudyProgress progress);
//    // 챕터의 모든 단어 불러오기(챕터 id로 단어 불러오기)
//    List<Word> getAllWordsByChapter(Integer chapterId);
//
//    // 챕터의 단어 갯수
//    int getWordCount(Integer chapterId);

//================================ study =====================================================
    // --------------- 3월 18일
   // 제일 처음 시작 할 때(!! 맞는 지모르겟음)
    void startStudy(User user, Integer levelId, Integer chapterId);
    // 챕터 중간에 나갔다가 들어와서 다시 공부할떄(!! 이것도 맞는지 ㅁ모르겟음 )
    List<WordDto> getRemainingWordsByChapter(User user, Integer chapterId);

    void updateWordProgress(User user, WordProgressRequestDto dto);


}
