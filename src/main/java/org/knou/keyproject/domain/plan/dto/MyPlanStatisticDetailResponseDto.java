package org.knou.keyproject.domain.plan.dto;

import lombok.*;

// 2023.8.5(토) 0h10
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPlanStatisticDetailResponseDto {
    // 통계 자료
    // 2023.7.28(금) 20h35
    // 해당 plan의 status가
    // 2. ACTIVE인 경우
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

    // 2. COMPLETE인 경우
    //private LocalDate completeDate; // 상태가 COMPLETE인 plan의 actionDates 중 가장 최근의 realActionDate

    // 4. PAUSE인 경우
    //private LocalDate pauseDate; // 일시중지 일자 = 해당 plan 관련 ActionDate 중 isDone이 true인 것 중 가장 최근 자료 or 해당 plan status의 변경 일자(필드 새로 하나 만들어서?) e.g. lastStatusChangedAt

    // 5. GIVEUP인 경우
    //private LocalDate giveUpDate; // 해당 plan의 lastStatusChangedAt

}
