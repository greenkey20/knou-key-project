package org.knou.keyproject.domain.member.entity;

import lombok.Getter;

// 2023.7.21(금) 18h15
public enum Role {
    USER(1),
    ADMIN(2);

    @Getter
    private final Integer num;

    Role(final Integer num) {
        this.num = num;
    }
}
