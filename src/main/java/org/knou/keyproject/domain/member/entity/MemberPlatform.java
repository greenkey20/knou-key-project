package org.knou.keyproject.domain.member.entity;

import lombok.Getter;

// 2023.7.26(수) 23h40
public enum MemberPlatform {
    WEBSITE(1),
    GOOGLE(2),
    KAKAO(3),
    NAVER(4);

    @Getter
    private final Integer num;

    MemberPlatform(final Integer num) {
        this.num = num;
    }
}
