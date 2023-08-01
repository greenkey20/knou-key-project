package org.knou.keyproject.domain.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// 2023.8.1(화) 18h
/**
 * ChatGPT 답변을 담을 DTO
 */
@Getter
@NoArgsConstructor
public class ChatGptResponseDto {
    private String id;
    private String Object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Getter
    @Setter
    public static class Choice {
        private Integer index;
        private ChatGptRequestDto.ChatGptMessage message;

        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        @JsonProperty("total_tokens")
        private Integer totalTokens;
    }

    // id, object, created, model,
    // choices (index, message (role, content), finish_reason),
    // usage (prompt_tokens, completion_tokens, total_tokens)


}
