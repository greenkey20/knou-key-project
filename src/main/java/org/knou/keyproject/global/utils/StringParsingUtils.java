package org.knou.keyproject.global.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringParsingUtils {
    // 2023.8.21(월) 11h40 메모 내용 parsing
    public static String replaceNewLineWithBr(String memo) {
        log.info("개행을 br 태그로 파싱 = " + memo.replaceAll("\r\n", "<br>"));
        return memo.replaceAll("\r\n", "<br>");
    }
}
