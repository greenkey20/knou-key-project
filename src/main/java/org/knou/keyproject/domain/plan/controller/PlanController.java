package org.knou.keyproject.domain.plan.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.plan.dto.MyPlanDetailResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.global.utils.Calendar;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.domain.plan.service.PlanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.knou.keyproject.global.utils.Calendar.getCalendarDatesList;

// 2023.7.22(토) 15h55
@Slf4j
@RequiredArgsConstructor
@Validated
@Controller
//@RequestMapping("/plans")
public class PlanController {
    private final PlanRepository planRepository;
    private final PlanService planService;
    private final PlanMapper planMapper;
    private final ActionDateMapper actionDateMapper;
    private final int SIZE = 5;
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
        Plan savedPlan = planService.saveNewPlan(requestDto);
//        log.info("controller postNewPlan에서 getActionDays = " + savedPlan.getActionDates().toString());
//        log.info("controller postNewPlan에서 getActionDays의 크기 = " + savedPlan.getActionDates().size());

        // 2023.7.25(화) 21h45
        List<List<ActionDate>> calendars = Calendar.getCalendars(savedPlan);

        // 2023.7.26(수) 3h35
        List<ActionDate> actionDatesList = savedPlan.getActionDatesList();

        mv
                .addObject("savedPlan", planMapper.toNewPlanResponseDto(planRepository.save(savedPlan)))
                .addObject("calendars", calendars) // plan 시작일~종료일까지의 달력(들)을 제목(xxxx. x 형식)과 함께 담은 Map
                .addObject("actionDatesList", actionDateMapper.entitiesToDtos(actionDatesList))
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
        log.info(requestDto.toString()); // 2023.7.27(목) 15h25 테스트 시  MyPlanPostRequestDto{planId=40, memberId=null, startDate=2023-07-28} 찍힘

        planService.saveMyNewPlan(requestDto);

        if (requestDto.getMemberId() == null) {
            session.setAttribute("requestDto", requestDto);
            session.setAttribute("alertMsg", "저장하시려면 로그인이 필요합니다");
            return "redirect:/loginPage.me";
        } else {
            session.setAttribute("alertMsg", "해당 활동 계획이 나의 일정에 성공적으로 저장되었습니다!");
            return "redirect:myPlanList.pl";
        }
    }

    // 2023.7.27(목) 17h10 추가
    @RequestMapping(value = "myNewPlanInsertAfterLogin.pl", method = RequestMethod.GET)
    public String postMyNewPlanAfterLogin(HttpSession session) {
        MyPlanPostRequestDto requestDto = (MyPlanPostRequestDto) session.getAttribute("requestDto");
        session.removeAttribute("requestDto");
        session.removeAttribute("status");

        requestDto.setMemberId(((Member) session.getAttribute("loginUser")).getMemberId());

        planService.saveMyNewPlanAfterLogin(requestDto);

        return "redirect:myPlanList.pl";
    }

    @GetMapping("myPlanList.pl")
    public ModelAndView getMyPlanList(@PageableDefault(size = 2, sort = "planId", direction = Sort.Direction.DESC) Pageable pageable,
                                      @RequestParam(required = false, defaultValue = "") String keyword,
                                      HttpSession session,
                                      ModelAndView mv) {
        Long memberId = ((Member) session.getAttribute("loginUser")).getMemberId();
        log.info("plan controller getMyPlanList()에서 특정 회원의 나의 일정 목록 불러올 때 memberId = " + memberId); // 2023.7.28(금) 16h50 plan controller getMyPlanList()에서 특정 회원의 나의 일정 목록 불러올 때 memberId = 1 이렇게 찍히는데, 왜 null 회원의 일정도 나오지? 회원2 가입시키고 해봐야겠다
        Page<Plan> planList = planRepository.findAllByMemberMemberId(memberId, pageable); // todo dto 반환 고민해보기

        if (keyword != null) {
            planList = planRepository.findByObjectContaining(keyword, pageable);
        }

        int pageNumber = planList.getPageable().getPageNumber(); // 현재 페이지
        int totalPages = planList.getTotalPages(); // 총 페이지 수  = 변수 size의 값
        int pageBlock = 3; // 블럭의 수
        int startBlockPage = (pageNumber / pageBlock) * pageBlock + 1; // 22h5 레퍼런스 설명으로는 이 식 이해 잘 안 됨
        int endBlockPage = startBlockPage + pageBlock - 1;
        endBlockPage = totalPages < endBlockPage ? totalPages : endBlockPage;

        List<Plan> list = planList.getContent();

        mv.addObject("startBlockPage", startBlockPage)
                .addObject("endBlockPage", endBlockPage)
                .addObject("planList", planList)
                .addObject("list", list)
                .setViewName("plan/myPlanListView");

//        Long memberId = ((Member) request.getSession().getAttribute("loginUser")).getMemberId();
//        List<MyPlanListResponseDto> list = planService.findPlansByMember(memberId, currentPage, SIZE);

//        mv.addObject("list", list).setViewName("plan/myPlanListView");
        return mv;
    }

    // 2023.7.28(금) 0h
    @RequestMapping("myPlanDetail.pl")
    public String getMyPlanDetail(@RequestParam(name = "planId") @Positive Long planId, Model model) {
        MyPlanDetailResponseDto planResponseDto = planService.findPlanById(planId);
        model.addAttribute("plan", planResponseDto);
        return "plan/myPlanDetailView";
    }

    // 2023.7.25(화) 12h35 AJAX로 했으나 클라이언트에 [Object, Object]..로 전달됨 -> 21h40 생각해보니 꼭 AJAX로 하지 않아도 되는 것 같아, 접근 방식 변경
//    @ResponseBody
    @RequestMapping(value = "calendar.pl", method = RequestMethod.GET)
    public ModelAndView getArrowCalendar(@RequestParam(name = "year", defaultValue = "2023") @Positive int year,
                                         @RequestParam(name = "month", defaultValue = "7") int month,
                                         ModelAndView mv) {
//        log.info("calendar.pl 처리하는 controller에 들어오는 request params 값 = year " + year + ", month " + month);
        if (month == 0) month = 12;
        LocalDate today = LocalDate.now();
        ActionDate searchDate = new ActionDate(String.valueOf(year), month, String.valueOf(today.getDayOfMonth()), today.getDayOfWeek().getValue(), null);

        // searchDate로부터 todayInfo를 만들어냄
        List<ActionDate> calendarDatesList = getCalendarDatesList(searchDate);
//        Map<String, Integer> todayInfo = searchDate.todayInfo(searchDate); // 21h50 이 메서드 내에서만 필요하고, JSP로 굳이 반환할 필요 없는 것 같은데..?

//        Map<String, Object> result = new HashMap<>();
//        result.put("calendarDatesList", calendarDatesList);
//        log.info("calendar.pl 처리하는 controller에서 만들어진 calendarDatesList = " + calendarDatesList);
//        result.put("todayInfo", todayInfo);

//        return new Gson().toJson(actionDateList);
        mv.addObject("calendarDatesList", calendarDatesList).setViewName("plan/newPlanResultView");
        return mv;
    }
}
