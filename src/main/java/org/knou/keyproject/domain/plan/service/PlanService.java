package org.knou.keyproject.domain.plan.service;

import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.entity.Plan;

import java.util.List;

public interface PlanService {
    Plan saveNewPlan(PlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h40
    void saveMyNewPlan(MyPlanPostRequestDto requestDto);

    // 2023.7.29(토) 22h20 추가
    void saveMyNewPlanAfterLogin(MyPlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h20 선언만 해둠

    List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size);

    Plan findPlanById(Long planId);

    List<BookInfoDto> searchBookTitle(String bookSearchKeyword);
}
