package org.knou.keyproject.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// 2023.7.22(토) 15h55
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
