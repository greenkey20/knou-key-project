package org.knou.keyproject.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.board.entity.BoardType;

import java.time.LocalDate;

// 2023.8.7(월) 0h10
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardListResponseDto {
    private Long boardId; // 글번호
    private BoardType boardType; // 상태
    private String title; // 제목
    private LocalDate createdAt; // 작성일
    private Integer readCount; // 조회 수

    private Long planId;
    private String object; // 활동명

    private Long memberId;
    private String nickname; // 작성자
}
