package org.knou.keyproject.domain.chatgpt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptRequestDto;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// 2023.8.1(화) 18h35
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
@RestController
public class ChatGptController {
//    private final APIResponse apiResponse;
//    private final ChatGptService chatGptService;

    // 2023.8.1(화) 22h25 추가
    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    /*
    @PostMapping("/question/v1")
    public ResponseEntity postQuestion1(HttpServletRequest request,
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

    // 2023.8.1(화) 20h55
    @ResponseBody
    @PostMapping("/question/v2")
    public ResponseEntity<ChatGptResponseDto> postQuestion(@RequestBody QuestionRequestDto requestDto) {
        try {
            String question = requestDto.getQuestion();
            ChatGptResponseDto responseDto = chatGptService.getAnswer(question);

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
     */

    // 2023.8.1(화) 21h30 다른 레퍼런스 https://blog.anchors-biz.com/?p=355
    /*
    @PostMapping("/question/v3")
    public ResponseEntity chat(@RequestBody QuestionRequestDto requestDto) throws IOException, InterruptedException {
        String prompt = requestDto.getQuestion();
        log.info("컨트롤러 chat()에서 받는 prompt");
        String response = chatGptService.chat(prompt);
        log.info("컨트롤러 chat()으로부터 보내는 response");

        return new ResponseEntity(response, HttpStatus.OK);
    }
     */

    // 2023.8.1(화) 22h25 또다른 레퍼런스 https://www.baeldung.com/spring-boot-chatgpt-api-openai
    @GetMapping("/question/v4")
    public String chat(@RequestParam String prompt) {
        ChatGptRequestDto request = new ChatGptRequestDto(model, prompt);
        ChatGptResponseDto response = restTemplate.postForObject(apiUrl, request, ChatGptResponseDto.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "no response";
        }
        return response.getChoices().get(0).getMessage().getContent();
    }
}
