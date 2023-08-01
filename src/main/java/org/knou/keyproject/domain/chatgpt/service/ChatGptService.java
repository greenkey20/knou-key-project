package org.knou.keyproject.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.chatgpt.config.ChatGptConfig;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptRequestDto;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
import org.knou.keyproject.domain.chatgpt.dto.QuestionRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 2023.8.1(화) 18h45
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGptService {
//    private final RestTemplate restTemplate;

    @Value("${api-key.chat-gpt}")
    private String apiKey;

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        httpHeaders.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        return new HttpEntity<>(requestDto, httpHeaders);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> requestDtoHttpEntity) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(ClientHttpResponse response) throws IOException {
//                HttpStatus statusCode = response.getStatusCode();
//                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
                return false;
            }
        });

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000); // 답변이 길어질 경우 TimeOut Error가 발생하는 바, 1분 정도 설정해줌
        requestFactory.setReadTimeout(60 * 1000); // 1min = 60sec * 1000ms
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.CHAT_URL,
                requestFactory,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    public ChatGptResponseDto askQuestion(QuestionRequestDto requestDto) {
        List<ChatGptRequestDto.ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptRequestDto.ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(requestDto.getQuestion())
                .build());
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.CHAT_MODEL,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.STREAM,
                                messages)));
    }

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder.build();
//    }
}
