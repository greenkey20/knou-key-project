package org.knou.keyproject.domain.member.entity;

import lombok.Getter;

// 2023.7.21(금) 18h15
public enum Gender {
    MALE(1),
    FEMALE(2),
    OTHERS(3);

    @Getter
    private final Integer num;

    Gender(final Integer num) {
        this.num = num;
    }
}
