package org.knou.keyproject.domain.actiondate.mapper;

import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.mapstruct.Mapper;

import java.util.List;

// 2023.7.28(금) 1h40
@Mapper(componentModel = "spring")
public interface ActionDateMapper {
    ActionDateResponseDto toActionDateResponseDto(ActionDate entity);

    List<ActionDateResponseDto> entitiesToDtos(List<ActionDate> entities);

    // 2023.7.31(월) 2h10
    ActionDate toEntity(ActionDatePostRequestDto requestDto);
}
