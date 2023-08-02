package org.knou.keyproject.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;

import java.time.LocalDate;
import java.util.List;

// 2023.7.25(화) 17H45
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPlanResponseDto {
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
    private String deadlinePeriod;
    private Integer totalDurationDays;

    private String frequencyDetail;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;
    private List<ActionDate> actionDates; // todo 이것도 dto로 교체하는 것 생각해보기

//    public NewPlanResponseDto(Plan entity) {
//        this.planId = entity.getPlanId();
//        this.object = entity.getObject();
//        this.totalQuantity = entity.getTotalQuantity();
//        this.unit = entity.getUnit();
//        this.hasStartDate = entity.getHasStartDate();
//        this.startDate = entity.getStartDate();
//        this.deadlineDate = entity.getDeadlineDate();
//        this.hasDeadline = entity.getHasDeadline();
//        this.totalDurationDays = entity.getTotalDurationDays();
//        this.frequencyDetail = entity.getFrequencyDetail();
//        this.totalNumOfActions = entity.getTotalNumOfActions();
//        this.quantityPerDay = entity.getQuantityPerDay();
//        this.actionDates = entity.getActionDatesList();
//    }
}
