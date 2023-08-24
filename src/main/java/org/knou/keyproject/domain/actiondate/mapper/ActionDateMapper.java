package org.knou.keyproject.domain.actiondate.mapper;

import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.dto.TodayActionDateResponseDto;
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

    default TodayActionDateResponseDto toTodayActionDateResponseDto(ActionDate entity) {
        if (entity == null) {
            return null;
        }

        TodayActionDateResponseDto.TodayActionDateResponseDtoBuilder todayActionDateResponseDto = TodayActionDateResponseDto.builder();

        todayActionDateResponseDto.actionDateId(entity.getActionDateId());
        todayActionDateResponseDto.planId(entity.getPlan().getPlanId());

        todayActionDateResponseDto.numOfYear(entity.getNumOfYear());

        if (entity.getNumOfMonth() != null) {
            todayActionDateResponseDto.numOfMonth(String.valueOf(entity.getNumOfMonth()));
        }

        todayActionDateResponseDto.numOfDate(entity.getNumOfDate());
        todayActionDateResponseDto.numOfDay(entity.getNumOfDay());
        todayActionDateResponseDto.dateFormat(entity.getDateFormat());

        todayActionDateResponseDto.dateType(entity.getDateType());
        todayActionDateResponseDto.schedule(entity.getSchedule());

        todayActionDateResponseDto.planActionQuantity(entity.getPlanActionQuantity());
        todayActionDateResponseDto.isDone(entity.getIsDone());
        todayActionDateResponseDto.realActionQuantity(entity.getRealActionQuantity());

        todayActionDateResponseDto.planStartUnit(entity.getPlanStartUnit());
        todayActionDateResponseDto.planEndUnit(entity.getPlanEndUnit());

        todayActionDateResponseDto.realStartUnit(entity.getRealStartUnit());
        todayActionDateResponseDto.realEndUnit(entity.getRealEndUnit());

        todayActionDateResponseDto.object(entity.getPlan().getObject());
        todayActionDateResponseDto.isMeasurable(entity.getPlan().getIsMeasurable());
        todayActionDateResponseDto.unit(entity.getPlan().getUnit());

        return todayActionDateResponseDto.build();
    }

    List<TodayActionDateResponseDto> toTodayActionDateResponseDtos(List<ActionDate> actionDatesList);
}
