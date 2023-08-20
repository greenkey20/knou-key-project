package org.knou.keyproject.domain.plan.dto;

import lombok.*;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.plan.entity.PlanStatus;

import java.time.LocalDate;
import java.util.List;

// 2023.8.20(일) 20h40
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageResponseDto {
    // MyPlanDetailResponseDto 속성들
    private Long planId;
    private Boolean isMeasurable;
    private String object;
    private Integer totalQuantity;
    private String unit;

    private Boolean hasStartDate;
    private LocalDate startDate;
    private Integer startYear;
    private Integer startMonth;

    private LocalDate deadlineDate;
    private Integer deadlineYear;
    private Integer deadlineMonth;
    private Boolean hasDeadline;

    private String frequencyDetail;
    private Integer totalDurationDays;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private List<ActionDateResponseDto> actionDatesList;
    private PlanStatus status;

    private LocalDate lastStatusChangedAt;

    private Boolean isChild;
    private int sizeOfModifiedPlansList;

    // MyPlanStatisticDetailResponseDto 속성들
    private Integer accumulatedRealActionQuantity; // 오늘까지(PAUSE나 GIVEUP의 경우 마지막 실행일까지) 누적 실제 수행 분량 = 시작일~오늘 중 actionDate 객체의 realActionQuantity 값의 총합
    private Integer accumulatedPlanActionQuantity; // 오늘까지 누적 계획 수행 분량 = 시작일~오늘 중 actionDate 객체의 planActionQuantity 값의 총합
    private Integer quantityDifferenceBetweenPlanAndReal;
    private Integer quantityToEndPlan;
    private String ratioOfRealActionQuantityTillToday;
    private String ratioOfQuantityToEndPlan;
    private Integer accumulatedNumOfActions; // 오늘까지 actionDate 객체 중 isDone true 상태인 것 계수
    private Integer numOfActionsToEndPlan;
    private Integer averageTimeTakenForRealAction; // 평균 수행 소요 시간 = 시작일~오늘 중 actionDate 객체의 timeTakenForRealAction 값의 평균

    @Setter
    private Integer periodDaysBeforePause;
    private Integer accumulatedPlanActionQuantityBeforePause;
}
