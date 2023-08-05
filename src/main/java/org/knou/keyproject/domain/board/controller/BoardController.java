package org.knou.keyproject.domain.board.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 2023.8.5(토) 15h10 클래스 생성
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class BoardController {

    @GetMapping("boardEnrollForm.bd")
    public String boardEnrollForm(@RequestParam(name = "planId") @Positive Long planId,
                                  @RequestParam(name = "planStatus") String status,
                                  Model model) {
        log.info("컨트롤러 boardEnrollForm() 메서드에 requestParam으로 들어오는 planId = " + planId + ", status = " + status);
        model.addAttribute("planId", planId);
        model.addAttribute("status", status);

        return "board/boardEnrollForm";
    }
}
