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

    // 2023.7.28(금) 1h10 현재까지 dto-entity 사이 매핑/변환하는 걸 내가 만들었는데, 나의 실수, 계속 발생하는 dto 필드 변화 등으로 생산성이 낮아지는 바, 아무래도 mapper를 사용하는 것이 좋을 것 같다
//    public MyPlanDetailResponseDto toEntity(Plan findPlan) {
//        return MyPlanDetailResponseDto.builder()
//                .planId(findPlan.getPlanId())
//                .build();
//    }
}
