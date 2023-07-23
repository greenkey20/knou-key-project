package org.knou.keyproject.domain.plan.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

// 2023.7.22(토) 15h55
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {
    private final PlanService planService;


    // 2023.7.23(일) 0h50
    @GetMapping("calculatorNew.pl")
    public String calculatorNewEnrollForm() {
        return "plan/calculatorNewEnrollForm";
    }

    @RequestMapping(value = "newPlanInsert.pl", method = RequestMethod.POST)
    public String postNewPlan(@ModelAttribute("plan") PlanPostRequestDto requestDto, Model model) {
//        final RedirectView redirectView = new RedirectView("newPlanInsert.pl", true);
        log.info(requestDto.toString()); // 2023.7.23(일) 23h10 현재 view로부터 값 안 넘어오고 있음 PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=0, object='null', totalQuantity=null, unit='null', startDate=null, frequencyTypeNum=0, frequencyDetail='null', hasDeadline=0, deadlineTypeNum=0, deadlineDate=null, deadlinePeriod='null', quantityPerDayPredicted=null}
        // 2023.7.24(월) 0h 해결 = dto에 setter 필요하구나 -> PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=1, object='자바의 정석 완독', totalQuantity=987, unit='페이지', startDate=2023-07-24, frequencyTypeNum=3, frequencyDetail='주 3회', hasDeadline=1, deadlineTypeNum=2, deadlineDate=null, deadlinePeriod='40일', quantityPerDayPredicted=null}
        // PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=1, object='자바의 정석 완독', totalQuantity=987, unit='페이지', startDate=2023-07-24, frequencyTypeNum=3, frequencyDetail='주 3회', hasDeadline=0, deadlineTypeNum=2, deadlineDate=null, deadlinePeriod='40일', quantityPerDayPredicted=40}
        Long planId = planService.saveNewPlan(requestDto);

        if (planId != null) {
            return "plan/newPlanResult";
        } else {
            model.addAttribute("errorMsg", "활동 계획 계산에 실패했습니다");
            return "common/errorPage";
        }
//        return redirectView;
    }
}
