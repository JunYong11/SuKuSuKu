package com.web.sukusuku.service;

import com.web.sukusuku.dto.*;
import com.web.sukusuku.model.*;

import java.util.List;
import java.util.Optional;

public interface StudyService {

    // 모든 레벨 가지고 오기
    List<Level> getAllLevels();
    //레벨에 맞는 챕터 목록을 가져오는 메서드(레벨id로 챕터 목록 갖고오기)
    List<ChapterDto> getChaptersByLevelId(Integer levelId, User user);

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

    // 한 챕터에서 단어수 == 안다 수 가 아니면 실행(챕터 중간에 나갔다가 들어와서 다시 공부할떄 or 학습 중 다 안다를 안 했을 때 모르다 했더 ㄴ단어 불러옴)
    List<WordDto> getRemainingWordsByChapter(User user,Integer chapterId);
    // 단어 안다,모른다 정보
    int updateWordProgress(User user, WordProgressRequestDto dto);
    // 누적 단어 갯수 구하기 위해 첫번째 챕터id 찾기
    int getStartChapterId(int levelId, int chapterId, int chapterRange);
    // 챕터 리셋
    void resetChapterProgress(User user, Integer chapterId);
    //복습 챕터 단어 갖고 오기(모른다 누른 횟수 1이상)
    List<WordDto> reviewStudy(User user);
    //복습 챕터에서 제거하기 버튼누르면 그 단어 테이블에서 삭제
    void reviewUpdate(User user, int wordId);

    }