package org.knou.keyproject.domain.plan.entity;

import lombok.Getter;

// 2023.7.21(금) 18h10
public enum PlanStatus {
    RESULT(1),
    ACTIVE(2),
    COMPLETE(3),
    PAUSE(4),
    GIVEUP(5),
    DELETE(6);

    @Getter
    private final Integer num;

    PlanStatus(final Integer num) {
        this.num = num;
    }
}
