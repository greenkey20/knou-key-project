package org.knou.keyproject.global.utils.paging;

import lombok.*;

// 2023.8.20(일) 3h50
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageInfo {
    private int listCount; // 총 게시물 갯수
    private int currentPage; // 현재 페이지
    private int pageLimit; // 1페이지당 보여질 페이징 바 수
    private int boardLimit; // 1페이지당 보여질 게시판 리스트/게시글 수

    private int maxPage; // 최대 페이징 바 수
    private int startPage; // 1페이지당 시작 페이징 바
    private int endPage; // 1페이지당 끝 페이징 바
}
