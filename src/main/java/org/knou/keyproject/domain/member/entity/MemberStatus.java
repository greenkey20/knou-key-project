package org.knou.keyproject.domain.member.entity;

import lombok.Getter;

// 2023.7.21(금) 18h15
public enum MemberStatus {
    ACTIVE(1),
    QUIT(2),
    BANNED(3);

    @Getter
    private final Integer num;

    MemberStatus(final Integer num) {
        this.num = num;
    }
}
