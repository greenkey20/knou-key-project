package org.knou.keyproject.domain.plan.service;

import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanService {
    Plan saveNewPlan(PlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h40
    void saveMyNewPlan(MyPlanPostRequestDto requestDto);

    // 2023.7.29(토) 22h20 추가
    void saveMyNewPlanAfterLogin(MyPlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h20 선언만 해둠

    Plan findVerifiedPlan(Long planId);

    List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size);

    Plan findPlanById(Long planId); // plain Plan 객체를 찾는 메서드

    // 2023.8.4(금) 22h50 plan 상세보기 요청 controller에 대응하기 위한 메서드
    MyPlanStatisticDetailResponseDto getPlanStatisticDetailById(Long planId);

    List<BookInfoDto> searchBookTitle(String bookSearchKeyword);

    String getChatGptResponse(PlanPostRequestDto requestDto);

    List<List<ActionDate>> getActionDatesCalendars(Long planId);

    List<List<ActionDate>> getPlanCalendars(Plan savedPlan);

    List<ActionDate> getArrowCalendar(int year, int month);

    Long resumePlan(Long planId);

    void pausePlan(Long planId);

    void giveUpPlan(Long planId);

    Page<Plan> findAllByMemberMemberIdOrderByPlanIdDesc(Long memberId, Pageable pageable);

    List<MyPlanStatisticDetailResponseDto> findStatisticDtosByMember(Long memberId);

    List<Plan> findAllActivePlansByMemberMemberId(Long memberId);
}
