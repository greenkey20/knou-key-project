package org.knou.keyproject.domain.actiondate.entity;

import lombok.Getter;

// 2023.7.26(수) 15h30
public enum DateType {
    TODAY(1),
    NORMALDAY(2),
    ACTION(3);

    @Getter
    private final Integer num;

    DateType(final Integer num) {
        this.num = num;
    }
}
