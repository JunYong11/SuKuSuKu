package com.web.sukusuku.posts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageNavigationDto {
    private int currentPage;     // 현재 페이지
    private int pagePerGroup;    // 페이지 그룹당 보여줄 수
    private int startPageGroup;  // 시작 페이지
    private int endPageGroup;    // 끝 페이지
    private int totalPageCount;  // 전체 페이지 수
}