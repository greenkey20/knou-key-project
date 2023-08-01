package org.knou.keyproject.domain.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 2023.8.1(화) 13h40

/**
 * ChatGPT에 요청할 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatGptRequestDto implements Serializable {
    private String model; // required
//    private String prompt; // required -> 현재 ChatGPT에 completions 요청(request)할 때는 이 필드가 필요없는 바, 이걸 이 클래스에 남겨두면 요청 보내기(request processing?)에 실패했다는 오류 메시지가 뜸
    private List<Message> messages;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    private Double temperature; // default = 1

    @Getter
    @Setter
//    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        // 이 클래스의 생성자도 잘 만들어주지 않으면 request processing?에 실패했다는 오류 메시지 뜨며 400(bad request) 에러 발생 (+포스트맨 response에는 500)
        //        private String user;
        private String role;
        private String content;

//        @Builder
//        public Message(String user, String content) {
//            this.user = user;
//            this.content = content;
//        }
    }

    /*
//    @JsonProperty("top_p")
//    private Double topP; // default = 1

    private Boolean stream;
    private Integer logprobs; // 2023.8.1(화) 17h50 API 문서에서 보고 궁금해서 postman에서 테스트해봤는데 400 bad request 뜸

    @Builder
    public ChatGptRequestDto(String model, String prompt, Integer maxTokens, Double temperature, Boolean stream, Integer logprobs, List<Message> messages) {
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
    public ChatGptRequestDto(String model, Integer maxTokens, Double temperature, Boolean stream, List<Message> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.stream = stream;
        this.messages = messages;
    }

    // 2023.8.1(화) 21h20 다른 레퍼런스
    public static ChatGptRequestDto createRequestDto(String prompt) {
        return new ChatGptRequestDto("gpt-3.5-turbo", prompt, 300, 1.0);
    }

    public ChatGptRequestDto(String model, String prompt, Integer maxTokens, Double temperature) {
        this.model = model;
        this.prompt = prompt;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
    }
     */

    // 2023.8.1(화) 22h20 또다른 레퍼런스 https://www.baeldung.com/spring-boot-chatgpt-api-openai
    public ChatGptRequestDto(String model, String prompt) {
        this.model = model;

        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
