package org.knou.keyproject.domain.plan.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// 2023.7.24(월) 17h25
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPlanPostRequestDto {
    private Long planId;
    private Long memberId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

//    @Builder
//    public MyPlanPostRequestDto(Long planId, Long memberId, LocalDate startDate) {
//        this.planId = planId;
//        this.memberId = memberId;
//        this.startDate = startDate;
//    }

    @Override
    public String toString() {
        return "MyPlanPostRequestDto{" +
                "planId=" + planId +
                ", memberId=" + memberId +
                ", startDate=" + startDate +
                '}';
    }
}
