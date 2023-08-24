package org.knou.keyproject.domain.actiondate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.knou.keyproject.domain.actiondate.entity.DateType;

// 2023.8.24(목) 16h
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayActionDateResponseDto {
    private Long actionDateId;
    private Long planId;

    private String numOfYear;
    private String numOfMonth;
    private String numOfDate;
    private Integer numOfDay;
    private String dateFormat;

    private DateType dateType;
    private String schedule;

    private Integer planActionQuantity; // 각 날짜에 계획되어있었던 수행 분량
    private Boolean isDone; // 해당 planId에 대한 actionDatesList의 요소들의 이 값이 true인 것들을 count하면 = 현재까지 누적 수행 횟수
    private Integer realActionQuantity; // 해당 planId에 대한 actionDatesList의 요소들의 이 값을 더하면 = 현재까지 누적 수행량

    private Integer planStartUnit;
    private Integer planEndUnit;

    private Integer realStartUnit;
    private Integer realEndUnit;

    private String object;
    private Boolean isMeasurable;
    private String unit;
}
