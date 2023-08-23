package org.knou.keyproject.global.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringParsingUtils {
    // 2023.8.21(월) 11h40 메모 내용 parsing
    public static String replaceNewLineWithBr(String memo) {
        log.info("개행을 br 태그로 파싱 = " + memo.replaceAll("\r\n", "<br>"));
        return memo.replaceAll("\r\n", "<br>");
    }

    // 2023.8.23(수) 16h40 ChatGPT 답변 활용 구현하다가 만들어봄
    public static String[] splitIntoLinesByBr(String content) {
        return content.split("<BR>");
    }
}
