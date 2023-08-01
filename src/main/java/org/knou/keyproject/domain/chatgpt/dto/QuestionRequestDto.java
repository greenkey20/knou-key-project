package org.knou.keyproject.domain.chatgpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 2023.8.1(화) 12h55
/**
 * front단에서 요청하는 DTO
 */
@Getter
@NoArgsConstructor
public class QuestionRequestDto implements Serializable {
    private String question;
}
