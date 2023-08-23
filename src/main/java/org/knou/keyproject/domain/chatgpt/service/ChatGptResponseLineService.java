package org.knou.keyproject.domain.chatgpt.service;

import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseLineDto;

import java.util.List;

public interface ChatGptResponseLineService {
    List<ChatGptResponseLineDto> getChatGptResponseLines(Long planId);

    String updateIsDone(Long chatGptResponseLineId);
}
