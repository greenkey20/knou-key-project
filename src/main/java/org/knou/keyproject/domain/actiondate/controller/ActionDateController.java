package org.knou.keyproject.domain.actiondate.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.dto.ActionDatePostRequestDto;
import org.knou.keyproject.domain.actiondate.dto.TodayActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.knou.keyproject.domain.actiondate.service.ActionDateService;
import org.knou.keyproject.domain.member.dto.MemberResponseDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 2023.7.30(일) 23h10
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
public class ActionDateController {
    private final PlanService planService;
    private final PlanMapper planMapper;
    private final ActionDateService actionDateService;
    private final ActionDateMapper actionDateMapper;

    // 2023.7.30(일) 6h45
    @ResponseBody
    @RequestMapping(value = "checkIsDone.pl", method = RequestMethod.POST)
    public String postRealActionQuantity(Long actionDateId, Integer realActionQuantity) {
        log.info("postRealActionQuantity() 컨트롤러 메서드에서 매개변수로 받은 값 realActionQuantity = " + realActionQuantity + ", actionDateId = " + actionDateId);
        return "실제 수행 분량 등록 AJAX 통신 성공";
    }

    @GetMapping("actionDetailRecordPage.ad")
    public String actionDetailRecordForm(@RequestParam(name = "planId") @Positive Long planId,
                                         @RequestParam(name = "actionDateId") @Positive Long actionDateId,
                                         Model model) {
        Plan findPlan = planService.findPlanById(planId);
        model.addAttribute("plan", planMapper.toMyPlanListResponseDto(findPlan));

        ActionDate findActionDate = actionDateService.findByActionDateId(actionDateId);
        model.addAttribute("actionDate", actionDateMapper.toActionDateResponseDto(findActionDate));

        return "actiondate/actionDetailRecordForm";
    }

    // 2023.7.31(월) 1h
    @RequestMapping(value = "newActionDateInsert.ad", method = RequestMethod.POST)
    public String postNewActionDate(@ModelAttribute("actionDate") ActionDatePostRequestDto requestDto) {
        log.info("컨트롤러 메서드 postNewActionDate()로 받은 requestDto = " + requestDto); // 2023.7.31(월) 2h {planId=1, dateFormat='2023-08-12', realActionQuantity=30, timeTakeForRealAction=null, reviewScore=5, memo=''}
        ActionDate savedActionDate = actionDateService.saveNewActionDate(requestDto);
        Long planId = savedActionDate.getPlan().getPlanId();

        return "redirect:myPlanDetail.pl?planId=" + planId;
    }

    // 2023.8.7(월) 5h35
    @RequestMapping(value = "actionDateUpdate.ad", method = RequestMethod.POST)
    public String patchActionDate(@RequestParam(name = "actionDateId") @Positive Long actionDateId,
                                  @ModelAttribute("actionDate") ActionDatePostRequestDto requestDto) {
        log.info("컨트롤러 메서드 patchActionDate()로 받은 requestDto = " + requestDto);
        ActionDate updatedActionDate = actionDateService.updateActionDate(requestDto);

        Long planId = updatedActionDate.getPlan().getPlanId();
        return "redirect:actionDetailView.ad?planId=" + planId + "&actionDateId=" + actionDateId;
    }

    // 2023.7.30(일) 23h25
    @GetMapping("actionDetailView.ad")
    public String actionDetailView(@RequestParam(name = "planId") @Positive Long planId,
                                   @RequestParam(name = "actionDateId") @Positive Long actionDateId,
                                   Model model) {
        Plan findPlan = planService.findPlanById(planId);
        model.addAttribute("plan", planMapper.toMyPlanListResponseDto(findPlan));

        ActionDate findActionDate = actionDateService.findByActionDateId(actionDateId);
        model.addAttribute("actionDate", actionDateMapper.toActionDateResponseDto(findActionDate));

        return "actiondate/actionDetailView";
    }

    // 2023.7.31(월) 5h40
    @GetMapping("actionDetailUpdatePage.ad")
    public String actionDetailUpdateForm(@RequestParam(name = "planId") @Positive Long planId,
                                         @RequestParam(name = "actionDateId") @Positive Long actionDateId,
                                         Model model) {
        Plan findPlan = planService.findPlanById(planId);
        model.addAttribute("plan", planMapper.toMyPlanListResponseDto(findPlan));

        ActionDate findActionDate = actionDateService.findByActionDateId(actionDateId);
        model.addAttribute("actionDate", actionDateMapper.toActionDateResponseDto(findActionDate));

        return "actiondate/actionDetailUpdateForm";
    }


    // 2023.8.5(토) 15h25 나의 생각 = 이걸 굳이 post로 보내야 하나? 쿼리스트링으로 id들 정보 붙여서 보내도 될 것 같은데?
    @RequestMapping(value = "actionDetailDelete.ad", method = RequestMethod.POST)
    public String deleteActionDate(@RequestParam(name = "actionDateId") @Positive Long actionDateId,
                                   @RequestParam(name = "planId") @Positive Long planId) {
        actionDateService.deleteActionDate(actionDateId);
        return "redirect:myPlanDetail.pl?planId=" + planId;
    }

    // 2023.8.24(목) 0h45 추가 + 16h5 필요한 actionDate 리스트를 응답하는 것이 주요 임무인 메서드인 바, planController로부터 여기로 옮김
    @GetMapping("myTodayPlanList.pl")
    public String getMyTodayPlanList(@RequestParam(name = "year", defaultValue = "2023") @Positive int year,
                                     @RequestParam(name = "month", defaultValue = "8") int month,
//                                     @RequestParam(name = "date", defaultValue = "24") int date,
                                     HttpSession session,
                                     Model model) {
        if (month == 0) month = 12;

        Long memberId = ((MemberResponseDto.AfterLoginMemberDto) session.getAttribute("loginUser")).getMemberId();
        List<TodayActionDateResponseDto> myTodayActionDatesList = actionDateService.getMyTodayActionDates(memberId);
//        List<ActionDate> calendarDatesList = planService.getArrowCalendar(year, month/*, date*/);
        List<ActionDate> calendarDatesList = actionDateService.getArrowCalendarOfActionDates(year, month, memberId);

        model.addAttribute("actionDatesList", myTodayActionDatesList);
        model.addAttribute("calendarDatesList", calendarDatesList);
        return "plan/myTodayPlanListView";
    }


}
