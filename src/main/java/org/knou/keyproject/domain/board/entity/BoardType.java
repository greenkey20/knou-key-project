package org.knou.keyproject.domain.board.entity;

import lombok.Getter;

// 2023.8.6(일) 6h25
public enum BoardType {
    PLAN(1),
    SUCCESS(2);

    @Getter
    private final int num;

    BoardType(final int num) {
        this.num = num;
    }

//    @Getter
//    private final String value;
//
//    BoardType(final String value) {
//        this.value = value;
//    }
}
