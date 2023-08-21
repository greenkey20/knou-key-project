package org.knou.keyproject.domain.bookchapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 2023.8.21(월) 20h15
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookChapterResponseDto {
    private Long bookChapterId;
    private String bookChapterString;
//    private Long planId;
    private Boolean isDone;
    private String isbn13;
}
