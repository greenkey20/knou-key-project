package org.knou.keyproject.global.exception;

import lombok.Getter;

// 2023.7.23(일) 21h35
public class BusinessLogicException extends RuntimeException{
    @Getter
    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage()); // 나의 질문 = super가 RuntimeException을 의미하는 것일까?
        this.exceptionCode = exceptionCode;

    }
}
