package org.knou.keyproject.domain.chatgpt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
import org.knou.keyproject.domain.chatgpt.dto.QuestionRequestDto;
import org.knou.keyproject.domain.chatgpt.service.ChatGptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

// 2023.8.1(화) 18h35
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
@RestController
public class ChatGptController {
//    private final APIResponse apiResponse;
    private final ChatGptService chatGptService;

    @PostMapping("/question")
    public ResponseEntity postQuestion(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody QuestionRequestDto requestDto) {
        ChatGptResponseDto responseDto = null;
        try {
            responseDto = chatGptService.askQuestion(requestDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(responseDto != null ? responseDto.getChoices().get(0).getMessage() : new ChatGptResponseDto(), HttpStatus.OK);
    }
}
