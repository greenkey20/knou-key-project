package org.knou.keyproject.domain.plan.entity;

import lombok.Getter;

// 2023.7.21(금) 18h5
public enum DeadlineType {
    DATE(1),
    PERIOD(2);

    @Getter
    private final Integer num;

    DeadlineType(final Integer num) {
        this.num = num;
    }
}
