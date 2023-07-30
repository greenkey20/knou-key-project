package org.knou.keyproject.domain.actiondate.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

// 2023.7.31(월) 1h5
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionDatePostRequestDto {
    private Long planId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateFormat;

    private Integer realActionQuantity;

    private Integer timeTakeForRealAction; // null일 수 있음
    private Integer reviewScore; // null일 수 있음
    private String memo; // null일 수 있음

    @Override
    public String toString() {
        return "ActionDatePostRequestDto{" +
                "planId=" + planId +
                ", dateFormat='" + dateFormat + '\'' +
                ", realActionQuantity=" + realActionQuantity +
                ", timeTakeForRealAction=" + timeTakeForRealAction +
                ", reviewScore=" + reviewScore +
                ", memo='" + memo + '\'' +
                '}';
    }
}
