package org.knou.keyproject.domain.plan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// 2023.7.24(월) 17h25
@Getter
@Setter
@NoArgsConstructor
public class MyPlanPostRequestDto {
    private Long planId;
    private Long plannerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Builder
    public MyPlanPostRequestDto(Long planId, Long plannerId, LocalDate startDate) {
        this.planId = planId;
        this.plannerId = plannerId;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "MyPlanPostRequestDto{" +
                "planId=" + planId +
                ", plannerId=" + plannerId +
                ", startDate=" + startDate +
                '}';
    }
}
