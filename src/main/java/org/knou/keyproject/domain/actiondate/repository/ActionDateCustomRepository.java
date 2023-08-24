package org.knou.keyproject.domain.actiondate.repository;

import org.knou.keyproject.domain.actiondate.entity.ActionDate;

import java.time.LocalDate;
import java.util.List;

// 2023.8.4(금) 21h50
public interface ActionDateCustomRepository {
    Integer getAccumulatedRealActionQuantity(Long planId);

    Integer getAccumulatedPlanActionQuantity(Long planId, LocalDate startDate);

    Integer getAverageTimeTakenForRealAction(Long planId);

    Integer getAccumulatedNumOfActions(Long planId);

    LocalDate getCompleteDate(Long planId);

    // 2023.8.5(토) 2h10 계획 상세 페이지 만들다가 추가
    Integer getAccumulatedPlanActionQuantityBeforePause(Long planId, LocalDate startDate, LocalDate lastStatusChangedAt);

    List<ActionDate> getMyTodayActionDates(Long memberId, String todayFormat);

    List<String> getActionDatesListByMemberAndYearAndMonth(int year, int month, Long memberId);

    // 2023.8.5(토) 17h40 plan resume 시 deadline 새로 계산(Calculator)하며 추가
//    LocalDate getLastActionDateBeforePause(Long planId);
}
