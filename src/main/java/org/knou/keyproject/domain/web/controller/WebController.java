package org.knou.keyproject.domain.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

// 2023.7.22(토) 21h25
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class WebController {

    @RequestMapping("/page")
    public String page() {
        return "common/page";
    }

    @RequestMapping("/testPlanList")
    public String testPlanList(Model model) {
        List<Plan> planList = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            Plan plan = Plan.builder()
                    .isMeasurable(true)
                    .object("정보처리기사 실기 합격")
                    .frequencyType(FrequencyType.TIMES)
                    .frequencyDetail("주 " + i + "회")
                    .hasDeadline(false)
                    .status(PlanStatus.ACTIVE).build();
            planList.add(plan);
        }

        model.addAttribute("planList", planList);
        return "common/testPlanList";
    }
}
