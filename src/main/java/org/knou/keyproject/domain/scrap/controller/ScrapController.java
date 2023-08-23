package org.knou.keyproject.domain.scrap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

// 2023.8.24(목) 1h25 틀만 추가해 둠
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class ScrapController {

    @GetMapping("myPlanScrapList.sc")
    public String getMyPlanScrapList(Model model) {

        return "scrap/myPlanScrapListView";
    }
}
