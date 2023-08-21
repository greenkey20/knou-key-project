package org.knou.keyproject.domain.bookchapter.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.bookchapter.serivce.BookChapterService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

// 2023.8.21(월) 21h25
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class BookChapterController {
    private final BookChapterService bookChapterService;

    @ResponseBody
    @RequestMapping(value = "bookChapterIsDone.bc", method = RequestMethod.POST)
    public String ajaxBookChapterIsDone(@Positive Long bookChapterId) {
        log.info("계획 상세 페이지 체크 박스 체크되어 넘어온 bookChapterId = " + bookChapterId);
        String result = bookChapterService.updateIsDone(bookChapterId);

        return result;
    }
}
