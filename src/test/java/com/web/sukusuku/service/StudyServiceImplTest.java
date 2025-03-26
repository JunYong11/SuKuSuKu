package com.web.sukusuku.service;

import com.web.sukusuku.repository.ChapterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class StudyServiceImplTest {
    @InjectMocks
    private StudyServiceImpl studyService;

    @Mock
    private ChapterRepository chapterRepository;

    @Test
    void testGetStartChapterId_NormalCase() {
        // given
        int levelId = 1;
        int chapterId = 10;
        int chapterRange = 5;
        int minChapterId = 1;

        when(chapterRepository.findMinChapterIdByLevel(levelId)).thenReturn(minChapterId);

        // when
        int result = studyService.getStartChapterId(levelId, chapterId, chapterRange);

        // then
        Assertions.assertEquals(6, result);  // 10 - (5 - 1) = 6
    }

    @Test
    void testGetStartChapterId_LowerBoundCase() {
        // given
        int levelId = 1;
        int chapterId = 3;
        int chapterRange = 5;
        int minChapterId = 1;

        when(chapterRepository.findMinChapterIdByLevel(levelId)).thenReturn(minChapterId);

        // when
        int result = studyService.getStartChapterId(levelId, chapterId, chapterRange);

        // then
        Assertions.assertEquals(1, result);  // 3 - (5 - 1) = -1 -> minChapterId(1)이 반환됨
    }

    @Test
    void testGetStartChapterId_NoMinChapterId_ThrowsException() {
        // given
        int levelId = 1;
        int chapterId = 10;
        int chapterRange = 5;

        when(chapterRepository.findMinChapterIdByLevel(levelId)).thenReturn(null);

        // when & then
        Assertions.assertThrows(RuntimeException.class, () -> {
            studyService.getStartChapterId(levelId, chapterId, chapterRange);
        });
    }

}
