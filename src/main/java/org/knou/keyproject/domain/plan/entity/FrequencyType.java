package org.knou.keyproject.domain.plan.entity;

import lombok.Getter;

// 2023.7.21(금) 18h
public enum FrequencyType {
    DATE(1),
    EVERY(2),
    TIMES(3);

    @Getter
    private final Integer num;

    FrequencyType(final Integer num) {
        this.num = num;
    }
}
