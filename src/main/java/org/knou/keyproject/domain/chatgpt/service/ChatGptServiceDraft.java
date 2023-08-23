//package org.knou.keyproject.domain.chatgpt.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.github.flashvayne.chatgpt.service.ChatgptService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.knou.keyproject.domain.chatgpt.config.ChatGptConfig;
//import org.knou.keyproject.domain.chatgpt.dto.ChatGptRequestDto;
//import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
//import org.knou.keyproject.domain.chatgpt.dto.QuestionRequestDto;
//import org.knou.keyproject.global.exception.BusinessLogicException;
//import org.knou.keyproject.global.exception.ExceptionCode;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.*;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.DefaultResponseErrorHandler;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLParameters;
//import java.io.IOException;
//import java.net.Authenticator;
//import java.net.CookieHandler;
//import java.net.ProxySelector;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpResponse;
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executor;
//
//import static org.knou.keyproject.domain.chatgpt.config.ChatGptConfig.*;
//
//// 2023.8.1(화) 18h45
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class ChatGptService {
//    //    private final RestTemplate restTemplate;
//    //    @Bean
////    public RestTemplate restTemplate(RestTemplateBuilder builder) {
////        return builder.build();
////    }
//    private final ChatgptService chatgptService; // 의존성 주입 받은 인터페이스
//
//    @Value("${api-key.chat-gpt}")
//    private String apiKey;
//
//    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
//        httpHeaders.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
//        return new HttpEntity<>(requestDto, httpHeaders);
//    }
//
//    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> requestDtoHttpEntity) {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//            public boolean hasError(ClientHttpResponse response) throws IOException {
////                HttpStatus statusCode = response.getStatusCode();
////                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
//                return false;
//            }
//        });
//
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(60000); // 답변이 길어질 경우 TimeOut Error가 발생하는 바, 1분 정도 설정해줌
//        requestFactory.setReadTimeout(60 * 1000); // 1min = 60sec * 1000ms
//        restTemplate.setRequestFactory(requestFactory);
//
//        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
//                CHAT_URL,
//                requestFactory,
//                ChatGptResponseDto.class);
//
//        return responseEntity.getBody();
//    }
//
//    public ChatGptResponseDto askQuestion(QuestionRequestDto requestDto) {
//        List<ChatGptRequestDto.Message> messages = new ArrayList<>();
//        messages.add(ChatGptRequestDto.Message.builder()
//                .role(ChatGptConfig.ROLE)
//                .content(requestDto.getQuestion())
//                .build());
//        return this.getResponse(
//                this.buildHttpEntity(
//                        new ChatGptRequestDto(
//                                ChatGptConfig.CHAT_MODEL,
//                                ChatGptConfig.MAX_TOKEN,
//                                ChatGptConfig.TEMPERATURE,
//                                ChatGptConfig.STREAM,
//                                messages)));
//    }
//
//    // 2023.8.1(화) 21h
//    public ChatGptResponseDto getAnswer(String prompt) {
//        try {
//            String responseMessage = chatgptService.sendMessage(prompt);
//            return new ChatGptResponseDto(responseMessage);
//        } catch (Exception e) {
//            throw new BusinessLogicException(ExceptionCode.SERVER_ERROR);
//        }
//    }
//
//    // 2023.8.1(화) 21h40 다른 레퍼런스
//    /*
//    private final ObjectMapper objectMapper;
//    private final HttpClient httpClient;
//
//
//    public String chat(String prompt) throws IOException, InterruptedException {
//        ChatGptRequestDto requestDto = ChatGptRequestDto.createRequestDto(prompt);
//        String requestBody = objectMapper.writeValueAsString(requestDto);
//        String responseBody = sendRequest(requestBody);
//        ChatGptResponseDto responseDto = objectMapper.readValue(responseBody, ChatGptResponseDto.class);
//
//        return responseDto.getText().orElseThrow();
//    }
//
//    private String sendRequest(String requestBodyAsJson) throws IOException, InterruptedException {
//        URI url = URI.create(CHAT_URL);
//        java.net.http.HttpRequest httpRequest = java.net.http.HttpRequest.newBuilder()
//                .uri(url)
//                .header("CONTENT_TYPE", MEDIA_TYPE)
//                .header("AUTHORIZATION", AUTHORIZATION)
//                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(requestBodyAsJson)).build();
//
//        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
//    }
//
//    @Bean
//    public HttpClient httpClient() {
//        return new HttpClient() {
//            @Override
//            public Optional<CookieHandler> cookieHandler() {
//                return Optional.empty();
//            }
//
//            @Override
//            public Optional<Duration> connectTimeout() {
//                return Optional.empty();
//            }
//
//            @Override
//            public Redirect followRedirects() {
//                return null;
//            }
//
//            @Override
//            public Optional<ProxySelector> proxy() {
//                return Optional.empty();
//            }
//
//            @Override
//            public SSLContext sslContext() {
//                return null;
//            }
//
//            @Override
//            public SSLParameters sslParameters() {
//                return null;
//            }
//
//            @Override
//            public Optional<Authenticator> authenticator() {
//                return Optional.empty();
//            }
//
//            @Override
//            public Version version() {
//                return null;
//            }
//
//            @Override
//            public Optional<Executor> executor() {
//                return Optional.empty();
//            }
//
//            @Override
//            public <T> HttpResponse<T> send(java.net.http.HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) throws IOException, InterruptedException {
//                return null;
//            }
//
//            @Override
//            public <T> CompletableFuture<HttpResponse<T>> sendAsync(java.net.http.HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
//                return null;
//            }
//
//            @Override
//            public <T> CompletableFuture<HttpResponse<T>> sendAsync(java.net.http.HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
//                return null;
//            }
//        };
//    }
//     */
//}
