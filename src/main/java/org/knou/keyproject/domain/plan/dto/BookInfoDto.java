package org.knou.keyproject.domain.plan.dto;

import lombok.*;

// 2023.7.31(월) 20h15
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookInfoDto {
    private String title; // 책 제목/
    private String author; // 지은이 + 옮긴이/
    private String pubDate; // 출간일자/
    private String description; // 간단 책 소개
    private String isbn13; // 13자리짜리 isbn/
    private String cover; // 책 표지 이미지/
    private String publisher; // 출판사명/
    private Integer numOfPages; // 페이지 수/

    @Override
    public String toString() {
        return "BookInfoDto{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", description='" + description + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                ", cover='" + cover + '\'' +
                ", publisher='" + publisher + '\'' +
                ", numOfPages=" + numOfPages +
                '}';
    }
}
