package org.knou.keyproject.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.plan.entity.PlanStatus;

import java.time.LocalDate;
import java.util.List;

// 2023.7.28(금) 0h10
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPlanDetailResponseDto {
    private Long planId;
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

    // 2023.8.4(금) 23h40 위 3개의 날짜를 하나의 속성으로 사용할 수 있을 듯
    private LocalDate lastStatusChangedAt;
    // 2023.7.28(금) 1h10 현재까지 dto-entity 사이 매핑/변환하는 걸 내가 만들었는데, 나의 실수, 계속 발생하는 dto 필드 변화 등으로 생산성이 낮아지는 바, 아무래도 mapper를 사용하는 것이 좋을 것 같다
//    public MyPlanDetailResponseDto toEntity(Plan findPlan) {
//        return MyPlanDetailResponseDto.builder()
//                .planId(findPlan.getPlanId())
//                .build();
//    }
}
