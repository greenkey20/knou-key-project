package org.knou.keyproject.domain.plan.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

// 2023.7.22(토) 15h55
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {
    private final PlanService planService;
    private final int SIZE = 10;


    // 2023.7.23(일) 0h50
    @GetMapping("calculatorNew.pl")
    public String calculatorNewEnrollForm() {
        return "plan/calculatorNewEnrollForm";
    }

    // 2023.7.24(월) 1h 수정

    /**
     * 활동 계획 계산 결과를 1차적으로 저장하는 요청을 처리하는 메서드
     *
     * @param requestDto
     * @param mv
     * @return
     */
    @RequestMapping(value = "newPlanInsert.pl", method = RequestMethod.POST)
    public ModelAndView postNewPlan(@ModelAttribute("plan") PlanPostRequestDto requestDto, ModelAndView mv) {
//        final RedirectView redirectView = new RedirectView("newPlanInsert.pl", true);
        log.info(requestDto.toString()); // 2023.7.23(일) 23h10 현재 view로부터 값 안 넘어오고 있음 PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=0, object='null', totalQuantity=null, unit='null', startDate=null, frequencyTypeNum=0, frequencyDetail='null', hasDeadline=0, deadlineTypeNum=0, deadlineDate=null, deadlinePeriod='null', quantityPerDayPredicted=null}
        // 2023.7.24(월) 0h 해결 = dto에 setter 필요하구나 -> PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=1, object='자바의 정석 완독', totalQuantity=987, unit='페이지', startDate=2023-07-24, frequencyTypeNum=3, frequencyDetail='주 3회', hasDeadline=1, deadlineTypeNum=2, deadlineDate=null, deadlinePeriod='40일', quantityPerDayPredicted=null}
        // PlanPostRequestDto{memberRepository=null, plannerId=null, isMeasurableNum=1, object='자바의 정석 완독', totalQuantity=987, unit='페이지', startDate=2023-07-24, frequencyTypeNum=3, frequencyDetail='주 3회', hasDeadline=0, deadlineTypeNum=2, deadlineDate=null, deadlinePeriod='40일', quantityPerDayPredicted=40}
        Plan savedPlan = planService.saveNewPlan(requestDto);

        mv.addObject("savedPlan", savedPlan).setViewName("plan/newPlanResult");
        return mv;

//        if (savedPlan != null) {
//            return "plan/newPlanResult";
//        } else {
//            model.addAttribute("errorMsg", "활동 계획 계산에 실패했습니다");
//            return "common/errorPage";
//        }
//        return redirectView;
    }

    // 2023.7.24(월) 17h
    /**
     * 활동 계획 계산 결과를 '나의 일정'에 저장하는 요청을 처리하는 메서드
     *
     * @return
     */
    @RequestMapping(value = "myNewPlanInsert.pl", method = RequestMethod.POST)
    public String postMyNewPlan(@ModelAttribute("plan") MyPlanPostRequestDto requestDto, HttpSession session) {
        log.info(requestDto.toString()); // 2023.7.24(월) 17h55 테스트 시 MyPlanPostRequestDto{planId=2, plannerId=null, startDate=2023-07-25} 찍힘

        planService.saveMyNewPlan(requestDto);

        session.setAttribute("alertMsg", "해당 활동 계획이 나의 일정에 성공적으로 저장되었습니다!");
        return "redirect:myPlanList.pl";
    }

    @GetMapping("myPlanList.pl")
    public ModelAndView getMyPlanList(@RequestParam(value = "cpage", defaultValue = "1") int currentPage, HttpServletRequest request, ModelAndView mv) {
        Long memberId = ((Member) request.getSession().getAttribute("loginUser")).getMemberId();
        List<Plan> myPlanList = planService.findPlansByMember(memberId, currentPage, SIZE);
        mv.addObject("myPlanList", myPlanList).setViewName("plan/myPlanListView");
        return mv;
    }
}
