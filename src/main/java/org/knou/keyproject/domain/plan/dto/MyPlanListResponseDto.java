package org.knou.keyproject.domain.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;

import java.time.LocalDate;

// 2023.7.25(화) 0h10
@Getter
@NoArgsConstructor
public class MyPlanListResponseDto {
    private Long planId;
    private String nickname;
    private String object;
    private Boolean isMeasurable;
    private PlanStatus status;
    private LocalDate startDate;
    private LocalDate deadlineDate;
    private Integer totalNumOfActions;
    private String frequencyDetail;
    private Integer quantityPerDay;

    public MyPlanListResponseDto(Plan entity) {
        this.planId = entity.getPlanId();
        this.nickname = entity.getPlanner().getNickname();
        this.object = entity.getObject();
        this.isMeasurable = entity.getIsMeasurable();
        this.status = entity.getStatus(); // 0h30 나의 질문 = String으로 저장해둔 enum 값이 String으로 잘 넘어가는지 궁금하다
        this.startDate = entity.getStartDate();
        this.deadlineDate = entity.getDeadlineDate();
        this.totalNumOfActions = entity.getTotalNumOfActions();
        this.frequencyDetail = entity.getFrequencyDetail();
        this.quantityPerDay = entity.getQuantityPerDay();
    }
}
