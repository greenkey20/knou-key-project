package org.knou.keyproject.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.plan.entity.Plan;

import java.time.LocalDate;
import java.util.List;

// 2023.7.28(금) 0h10
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPlanDetailResponseDto {
    private Long planId;
    private String object;
    private Integer totalQuantity;
    private String unit;
    private LocalDate startDate;
    private LocalDate deadlineDate;
    private String frequencyDetail;
    private Integer totalDurationDays;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private List<ActionDateResponseDto> actionDatesList;

    // 통계 자료
    // 2023.7.28(금) 20h35
    // 해당 plan의 status가
    // 2. ACTIVE인 경우
    private Integer accumulatedRealActionQuantity; // 오늘까지(PAUSE나 GIVEUP의 경우 마지막 실행일까지) 누적 실제 수행 분량 = 시작일~오늘 중 actionDate 객체의 realActionQuantity 값의 총합
    private Integer accumulatedPlanActionQuantity; // 오늘까지 누적 계획 수행 분량 = 시작일~오늘 중 actionDate 객체의 planActionQuantity 값의 총합
    private Integer averageTimeTakenForRealAction; // 평균 수행 소요 시간 = 시작일~오늘 중 actionDate 객체의 timeTakenForRealAction 값의 평균
    private Integer accumulatedNumOfActions; // 오늘까지 actionDate 객체 중 isDone true 상태인 것 계수
    // 2. COMPLETE인 경우
    private LocalDate completeDate;

    // 4. PAUSE인 경우
    private LocalDate pauseDate; // 일시중지 일자 = 해당 plan 관련 ActionDate 중 isDone이 true인 것 중 가장 최근 자료 or 해당 plan status의 변경 일자(필드 새로 하나 만들어서?) e.g. lastStatusChangedAt

    // 5. GIVEUP인 경우
    private LocalDate giveUpDate;

    // 2023.7.28(금) 1h10 현재까지 dto-entity 사이 매핑/변환하는 걸 내가 만들었는데, 나의 실수, 계속 발생하는 dto 필드 변화 등으로 생산성이 낮아지는 바, 아무래도 mapper를 사용하는 것이 좋을 것 같다
//    public MyPlanDetailResponseDto toEntity(Plan findPlan) {
//        return MyPlanDetailResponseDto.builder()
//                .planId(findPlan.getPlanId())
//                .build();
//    }
}
