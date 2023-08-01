package org.knou.keyproject.global.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "User not found"),
    PLAN_NOT_FOUND(404, "Plan not found"),
    MEMBER_EXISTS(409, "Member exists"),
    UNAUTHORIZED_USER(403, "Unauthorized user"), // 비밀번호 불일치 등으로 비인가된 이용자
    SERVER_ERROR(500, "Internal server error");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
