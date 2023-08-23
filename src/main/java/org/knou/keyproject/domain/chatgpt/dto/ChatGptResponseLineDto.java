package org.knou.keyproject.domain.chatgpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGptResponseLineDto {
    private Long chatGptResponseLineId;
    private String chatGptResponseLineString;
    private Boolean isDone;
}
