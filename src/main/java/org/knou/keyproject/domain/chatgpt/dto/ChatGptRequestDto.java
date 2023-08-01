package org.knou.keyproject.domain.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

// 2023.8.1(화) 13h40

/**
 * ChatGPT에 요청할 DTO
 */
@Getter
@NoArgsConstructor
public class ChatGptRequestDto implements Serializable {
    private String model; // required
    private String prompt; // required

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature; // default = 1
//    @JsonProperty("top_p")
//    private Double topP; // default = 1

    private Boolean stream;
    private Integer logprobs; // 2023.8.1(화) 17h50 API 문서에서 보고 궁금해서 postman에서 테스트해봤는데 400 bad request 뜸
    private List<ChatGptMessage> messages;

    @Getter
//    @Setter
    @Builder
//    @NoArgsConstructor
    public static class ChatGptMessage {
//        private String user;
        private String role;
        private String content;

//        @Builder
//        public ChatGptMessage(String user, String content) {
//            this.user = user;
//            this.content = content;
//        }
    }

    @Builder
    public ChatGptRequestDto(String model, String prompt, Integer maxTokens, Double temperature, Boolean stream, Integer logprobs, List<ChatGptMessage> messages/*, Double topP*/) {
        this.model = model;
        this.prompt = prompt;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.logprobs = logprobs;
        this.messages = messages;
//        this.topP = topP;
    }

    // 2023.8.1(화) 20h5 service 작성하다 추가
    public ChatGptRequestDto(String model, Integer maxTokens, Double temperature, Boolean stream, List<ChatGptMessage> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
    }
}
