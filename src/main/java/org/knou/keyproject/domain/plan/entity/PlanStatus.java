package org.knou.keyproject.domain.plan.entity;

import lombok.Getter;

// 2023.7.21(금) 18h10
public enum PlanStatus {
    ACTIVE(1),
    COMPLETE(2),
    PAUSE(3),
    GIVEUP(4),
    DELETE(5);

    @Getter
    private final Integer num;

    PlanStatus(final Integer num) {
        this.num = num;
    }
}
