package org.knou.keyproject.domain.chatgpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseLineDto;
import org.knou.keyproject.domain.chatgpt.entity.ChatGptResponseLine;
import org.knou.keyproject.domain.chatgpt.mapper.ChatGptResponseLineMapper;
import org.knou.keyproject.domain.chatgpt.repository.ChatGptResponseLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatGptResponseLineServiceImpl implements ChatGptResponseLineService {
    private final ChatGptResponseLineRepository chatGptResponseLineRepository;
    private final ChatGptResponseLineMapper chatGptResponseLineMapper;

    @Override
    public List<ChatGptResponseLineDto> getChatGptResponseLines(Long planId) {
        List<ChatGptResponseLine> chatGptResponseLineList = chatGptResponseLineRepository.findAllByPlanPlanId(planId);

        return chatGptResponseLineMapper.toChatGptResponseLineDtoList(chatGptResponseLineList);
    }

    @Override
    @Transactional
    public String updateIsDone(Long chatGptResponseLineId) {
        ChatGptResponseLine findChatGptResponseLine = findVerifiedChatGptResponseLine(chatGptResponseLineId);

        findChatGptResponseLine.changeIsDone(true);

        return "chatGptResponseLine update is done!";
    }

    private ChatGptResponseLine findVerifiedChatGptResponseLine(Long chatGptResponseLineId) {
        return chatGptResponseLineRepository.findById(chatGptResponseLineId).orElse(null);
    }
}
