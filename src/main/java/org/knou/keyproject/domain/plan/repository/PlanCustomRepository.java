package org.knou.keyproject.domain.plan.repository;

import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

// 2023.8.4(금) 21h40
public interface PlanCustomRepository {
    List<Plan> findAllActivePlansByMemberMemberId(Long memberId);

    List<Plan> findMyPlans(Long memberId);

    List<Plan> findAllPlansByMemberMemberId(Long memberId);
}
