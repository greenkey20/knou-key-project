package org.knou.keyproject.domain.plan.service;

import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;

public interface PlanService {
    Long saveNewPlan(PlanPostRequestDto requestDto);
}
