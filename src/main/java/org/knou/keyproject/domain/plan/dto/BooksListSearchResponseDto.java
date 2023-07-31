package org.knou.keyproject.domain.plan.dto;

import lombok.*;

import java.util.List;

// 2023.7.31(월) 18h50
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksListSearchResponseDto {
    // 책 제목
    // 저자
    // 출판사
    // 출간년도
    // isbn

    // 응답 예시 e.g. http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbgreenkey201608001&Query=%EC%BB%B4%ED%8C%8C%EC%9D%BC%EB%9F%AC&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101
    private String version; // "20131101"
//    private String logo;
    private String title; // "알라딘 검색결과 - 컴파일러"
    private String link;
    private String pubDate; // 이 요청 보낸 날짜 및 시각
    private Integer totalResults;
    private Integer startIndex;
    private Integer itemsPerPage;
    private String query; // 검색어 e.g. 컴파일러
    private Integer searchCategoryId; // 0
    private String searchCategoryName; // 전체
    private List<Item> item;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String title; // 책 제목/
        private String author; // 지은이 + 옮긴이/
        private String pubDate; // 출간일자/
        private String description; // 간단 책 소개
        private String isbn13; // 13자리짜리 isbn/
        private String cover; // 책 표지 이미지/
        private String publisher; // 출판사명/
        private SubInfo subInfo;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SubInfo {
            private Integer itemPage; // 상품의 쪽수/
        }
    }
}
