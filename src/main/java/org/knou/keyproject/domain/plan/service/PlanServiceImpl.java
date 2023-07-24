package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 2023.7.23(일) 22h
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Plan saveNewPlan(PlanPostRequestDto requestDto) {
        return planRepository.save(requestDto.toEntity());
    }

    // 2023.7.24(월) 17h40
    @Override
    public Plan saveMyNewPlan(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());
        
        Member findMember = memberRepository.findById(requestDto.getPlannerId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        findPlan.setPlanner(findMember);

        if (!findPlan.getHasStartDate()) {
            findPlan.setStartDate(requestDto.getStartDate());
        }

        findPlan.setStatus(PlanStatus.ACTIVE);

        return planRepository.save(findPlan);
    }

    public Plan findVerifiedPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLAN_NOT_FOUND));
    }

    // 2023.7.24(월) 17h20 자동 기본 구현만 해둠
    @Override
    public List<Plan> findMemberPlans(Long memberId, int currentPage, int size) {
        return null;
    }
}
