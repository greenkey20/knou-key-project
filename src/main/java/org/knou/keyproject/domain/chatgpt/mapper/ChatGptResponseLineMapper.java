package org.knou.keyproject.domain.chatgpt.mapper;

import org.knou.keyproject.domain.chatgpt.dto.ChatGptResponseLineDto;
import org.knou.keyproject.domain.chatgpt.entity.ChatGptResponseLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatGptResponseLineMapper {
    ChatGptResponseLineDto toChatGptResponseLineDto(ChatGptResponseLine entity);

    List<ChatGptResponseLineDto> toChatGptResponseLineDtoList(List<ChatGptResponseLine> entities);
}
