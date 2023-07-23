package org.knou.keyproject.domain.plan.controller;

import jakarta.persistence.PreUpdate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

// 2023.7.22(토) 15h55
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {

    // 2023.7.23(일) 0h50
    @RequestMapping("calculatorNew.pl")
    public String calculatorNewEnrollForm() {
        return "plan/calculatorNewEnrollForm";
    }

    @RequestMapping("newPlanInsert.pl")
    public String insertNewPlan(Plan plan, HttpSession session, Model model) {
        Boolean result = false;

        if (result) {
            return "plan/newPlanResult";
        } else {
            model.addAttribute("errorMsg", "활동 계획 계산에 실패했습니다");
            return "common/errorPage";
        }
    }
}
