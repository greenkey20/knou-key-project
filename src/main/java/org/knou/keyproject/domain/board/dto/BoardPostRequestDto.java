package org.knou.keyproject.domain.board.dto;

import lombok.*;

// 2023.8.6(일) 20h30
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostRequestDto {
    private Long planId;
    private Long memberId;
    private String title;
    private String content;
    private String boardType;
}
