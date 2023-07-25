package org.knou.keyproject.domain.plan.service;

import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.NewPlanResponseDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;

import java.util.List;

public interface PlanService {
    NewPlanResponseDto saveNewPlan(PlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h40
    void saveMyNewPlan(MyPlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h20 선언만 해둠
    List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size);
}
