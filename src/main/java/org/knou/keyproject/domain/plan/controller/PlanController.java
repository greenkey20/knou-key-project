package org.knou.keyproject.domain.plan.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.NewPlanResponseDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.ActionDate;
import org.knou.keyproject.domain.plan.entity.Calendar;
import org.knou.keyproject.domain.plan.entity.DateType;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2023.7.22(토) 15h55
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {
    private final PlanService planService;
    private final int SIZE = 10;
    private final int THIS_YEAR = LocalDate.now().getYear();
    private final int THIS_MONTH = LocalDate.now().getMonthValue();

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
        NewPlanResponseDto savedPlan = planService.saveNewPlan(requestDto);
        log.info("controller postNewPlan에서 getActionDays = " + savedPlan.getActionDates().toString());
        log.info("controller postNewPlan에서 getActionDays의 크기 = " + savedPlan.getActionDates().size());

        // 2023.7.25(화) 21h45
        List<ActionDate> calendarDatesList = Calendar.getCalendar(savedPlan.getActionDates());

        // 2023.7.26(수) 3h35
        List<ActionDate> actionDatesList = savedPlan.getActionDates();

        mv
                .addObject("savedPlan", savedPlan)
                .addObject("calendarDatesList", calendarDatesList)
                .addObject("actionDatesList", actionDatesList)
                .setViewName("plan/newPlanResultView"); // 2023.7.25(화) 21h40 생각 = 여기에서 달력 출력할 정보도 같이 넘겨준다..
        return mv;

//        if (savedPlan != null) {
//            return "plan/newPlanResultView";
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
        List<MyPlanListResponseDto> list = planService.findPlansByMember(memberId, currentPage, SIZE);

        mv.addObject("list", list).setViewName("plan/myPlanListView");
        return mv;
    }

    // 2023.7.25(화) 12h35 AJAX로 했으나 클라이언트에 [Object, Object]..로 전달됨 -> 21h40 생각해보니 꼭 AJAX로 하지 않아도 되는 것 같아, 접근 방식 변경
//    @ResponseBody
    @RequestMapping(value = "calendar.pl", method = RequestMethod.GET)
    public ModelAndView getArrowCalendar(@RequestParam(name = "year", defaultValue = "2023") int year,
                                         @RequestParam(name = "month", defaultValue = "7") int month,
                                         ModelAndView mv) {
        LocalDate today = LocalDate.now();

        ActionDate searchDate = new ActionDate(String.valueOf(year), month, String.valueOf(today.getDayOfMonth()), today.getDayOfWeek().getValue(), null);

        Map<String, Integer> todayInfo = searchDate.todayInfo(searchDate); // 21h50 이 메서드 내에서만 필요하고, JSP로 굳이 반환할 필요 없는 것 같은데..?

        List<ActionDate> actionDateList = new ArrayList<>(); // 이번 달 달력에 찍을 날짜들을 모은 리스트
        ActionDate individualDay;

        // 월요일부터 해당 월 시작일 요일 전까지 빈칸으로 채움 -> 나는 추후에 지난 달 날짜로 채우고 싶다! // todo
        for (int i = 0; i < todayInfo.get("startDay"); i++) {
            individualDay = new ActionDate(null, null, null, i, null);
            actionDateList.add(individualDay);
        }

        int dayInt = todayInfo.get("startDay"); // 해당 월 1일의 요일

        for (int i = todayInfo.get("startDate"); i <= todayInfo.get("endDate"); i++) {
            if (dayInt % 7 != 0) {
                dayInt = dayInt % 7;
            } else {
                dayInt = 7;
            }

            if (i == todayInfo.get("todayDate")) {
                individualDay = new ActionDate(String.valueOf(searchDate.getYear()), searchDate.getMonth(), String.valueOf(i), dayInt, DateType.TODAY);
            } else {
                individualDay = new ActionDate(String.valueOf(searchDate.getYear()), searchDate.getMonth(), String.valueOf(i), dayInt, DateType.NORMALDAY);
            }

            actionDateList.add(individualDay);
            dayInt++;
        }

        // 월 마지막 날 요일부터 일요일까지 빈칸으로 채움 -> 나는 추후에 다음 달 날짜로 채우고 싶다! // todo
        int delim = actionDateList.size() % 7;
        if (delim != 0) {
            for (int i = 0; i < 7 - delim; i++) {
                individualDay = new ActionDate(null, null, null, null, null);
                actionDateList.add(individualDay);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dateDataList", actionDateList);
        result.put("todayInfo", todayInfo);

//        return new Gson().toJson(actionDateList);
        mv.addObject(actionDateList).setViewName("plan/newPlanResultView");
        return mv;
    }
}
