package org.knou.keyproject.domain.chatgpt.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.chatgpt.service.ChatGptResponseLineService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 2023.8.23(수) 17h50
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class ChatGptResponseLineController {
    private final ChatGptResponseLineService chatGptResponseLineService;

    @ResponseBody
    @RequestMapping(value = "chatGptResponseLineIsDone.cg", method = RequestMethod.POST)
    public String ajaxChatGptResponseLineIsDone(@Positive Long chatGptResponseLineId) {
        log.info("계획 상세 페이지 체크 박스 체크되어 넘어온 chatGptResponseLineId = " + chatGptResponseLineId);
        String result = chatGptResponseLineService.updateIsDone(chatGptResponseLineId);

        return result;
    }
}
