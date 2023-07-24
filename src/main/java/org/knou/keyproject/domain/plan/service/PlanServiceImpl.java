package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.dto.MyPlanListResponseDto;
import org.knou.keyproject.domain.plan.dto.MyPlanPostRequestDto;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.Calculator;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
//        Plan planToSave = requestDto.toEntity();
        Calculator calculator = new Calculator(requestDto);
        Plan calculatedPlan = calculator.calculateNewPlan();

        return planRepository.save(calculatedPlan);
    }

    // 2023.7.24(월) 17h40 -> 22h40 startDate 입력에 따른 계산 결과 반영
    @Override
    @Transactional
    public void saveMyNewPlan(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());

        Member findMember = memberRepository.findById(requestDto.getPlannerId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        findPlan.setPlanner(findMember);

        findPlan.setStatus(PlanStatus.ACTIVE);

        if (!findPlan.getHasStartDate()) {
            findPlan.setStartDate(requestDto.getStartDate());
        }

        Calculator calculator = new Calculator(findPlan);
        findPlan = calculator.calculateNewPlan();

        planRepository.save(findPlan);
    }

    public Plan findVerifiedPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLAN_NOT_FOUND));
    }

    // 2023.7.24(월) 17h20 자동 기본 구현만 해둠 -> 23h10 내용 구현
    @Override
    public List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return planRepository.findByMemberId(findMember.getMemberId(), PageRequest.of(currentPage - 1, size, Sort.by("planId").descending()))
                .stream()
                .map(MyPlanListResponseDto::new)
                .collect(Collectors.toList());
    }
}
