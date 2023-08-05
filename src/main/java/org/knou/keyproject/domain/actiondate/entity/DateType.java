package org.knou.keyproject.domain.actiondate.entity;

import lombok.Getter;

// 2023.7.26(수) 15h30
public enum DateType {
    TODAY(1), // 빈 달력 만들 때 set
    NORMALDAY(2), // 빈 달력 만들 때 set
    ACTION(3), // 특정 plan의 actionDatesList 만들 때 set
    PAUSE(4), // 특정 plan 수행을 일시 중지할 때 set
    GIVEUP(5), // 특정 plan 수행을 포기할 때 set
    DONE(6); // 빈 달력에 '기 수행일' 표시하고자 할 때 set

    @Getter
    private final Integer num;

    DateType(final Integer num) {
        this.num = num;
    }
}
