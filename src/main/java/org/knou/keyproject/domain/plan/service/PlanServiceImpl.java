package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 2023.7.23(일) 22h
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Override
    @Transactional
    public Long saveNewPlan(PlanPostRequestDto requestDto) {
        return planRepository.save(requestDto.toEntity()).getPlanId();
    }
}
