package org.knou.keyproject.domain.plan.service;

import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.Plan;

import java.util.List;

public interface PlanService {
    Plan saveNewPlan(PlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h40
    Plan saveMyNewPlan(MyPlanPostRequestDto requestDto);

    // 2023.7.24(월) 17h20 선언만 해둠
    List<Plan> findMemberPlans(Long memberId, int currentPage, int size);
}
