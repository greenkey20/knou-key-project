package org.knou.keyproject.domain.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.plan.entity.DateData;
import org.knou.keyproject.domain.plan.entity.Plan;

import java.time.LocalDate;
import java.util.List;

// 2023.7.25(화) 17H45
@Getter
@NoArgsConstructor
public class NewPlanResponseDto {
    private Long planId;

    private String object;
    private Integer totalQuantity;
    private String unit;

    private Boolean hasStartDate;
    private LocalDate startDate;
    private LocalDate deadlineDate;

    private Boolean hasDeadline;
    private Integer totalDurationDays;

    private String frequencyDetail;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private List<DateData> actionDays;

    public NewPlanResponseDto(Plan entity) {
        this.planId = entity.getPlanId();
        this.object = entity.getObject();
        this.totalQuantity = entity.getTotalQuantity();
        this.unit = entity.getUnit();
        this.hasStartDate = entity.getHasStartDate();
        this.startDate = entity.getStartDate();
        this.deadlineDate = entity.getDeadlineDate();
        this.hasDeadline = entity.getHasDeadline();
        this.totalDurationDays = entity.getTotalDurationDays();
        this.frequencyDetail = entity.getFrequencyDetail();
        this.totalNumOfActions = entity.getTotalNumOfActions();
        this.quantityPerDay = entity.getQuantityPerDay();
        this.actionDays = entity.getActionDays();
    }
}
