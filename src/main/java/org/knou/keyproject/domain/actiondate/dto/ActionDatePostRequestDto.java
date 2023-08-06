package org.knou.keyproject.domain.actiondate.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

// 2023.7.31(월) 1h5
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDatePostRequestDto {
    private Long actionDateId;
    private Long planId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate realActionDate;

    private Integer realActionQuantity;

    private Integer timeTakenForRealAction; // null일 수 있음
    private Integer reviewScore; // null일 수 있음
    private String memo; // null일 수 있음

    @Override
    public String toString() {
        return "ActionDatePostRequestDto{" +
                "planId=" + planId +
                ", realActionDate=" + realActionDate +
                ", realActionQuantity=" + realActionQuantity +
                ", timeTakenForRealAction=" + timeTakenForRealAction +
                ", reviewScore=" + reviewScore +
                ", memo='" + memo + '\'' +
                '}';
    }
}
