package org.knou.keyproject.domain.actiondate.service;

import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.dto.TodayActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;

import java.util.List;

// 2023.7.29(토) 22h35
public interface ActionDateService {
    void deleteActionDatesByPlanId(Long planId);

    ActionDate findByActionDateId(Long actionDateId);

    ActionDate saveNewActionDate(ActionDatePostRequestDto requestDto);

    // 2023.7.31(월) 6h10
    void deleteActionDate(Long actionDateId);

    ActionDate updateActionDate(ActionDatePostRequestDto requestDto);

    List<TodayActionDateResponseDto> getMyTodayActionDates(Long memberId);

    List<ActionDate> getArrowCalendarOfActionDates(int year, int month, Long memberId);
}
