package org.knou.keyproject.domain.plan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.member.entity.Member;
import org.knou.keyproject.domain.member.repository.MemberRepository;
import org.knou.keyproject.domain.plan.dto.*;
import org.knou.keyproject.domain.plan.mapper.PlanMapper;
import org.knou.keyproject.global.utils.Calculator;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.repository.PlanRepository;
import org.knou.keyproject.global.exception.BusinessLogicException;
import org.knou.keyproject.global.exception.ExceptionCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final PlanMapper planMapper;

    @Override
    @Transactional
    public NewPlanResponseDto saveNewPlan(PlanPostRequestDto requestDto) {
//        Plan planToSave = requestDto.toEntity();
        Calculator calculator = new Calculator();
        Plan calculatedPlan = calculator.calculateNewPlan(requestDto);
//        System.out.println("이거 안 찍히나?" + calculatedPlan.getActionDatesList().get(0).getDateType().toString()); // 2023.7.28(금) 3h15 이거 안 찍히나?ACTION 찍히는데

        return planMapper.toNewPlanResponseDto(planRepository.save(calculatedPlan));
    }

    // 2023.7.24(월) 17h40 -> 22h40 startDate 입력에 따른 계산 결과 반영
    @Override
    @Transactional
    public void saveMyNewPlan(MyPlanPostRequestDto requestDto) {
        Plan findPlan = findVerifiedPlan(requestDto.getPlanId());

        Member findMember = null;
        // 로그인 안 하고 저장을 희망하는 이용자가 있을 수 있어서, 아래 null 처리 필요
        if (requestDto.getMemberId() != null) {
            findMember = memberRepository.findById(requestDto.getMemberId()).orElse(null); // todo 예외처리 방식 변경
        }

        findPlan.setMember(findMember);

        findPlan.setStatus(PlanStatus.ACTIVE);

        if (!findPlan.getHasStartDate()) {
            findPlan.setStartDate(requestDto.getStartDate());
        }

        Calculator calculator = new Calculator();
        findPlan = calculator.calculateRealNewPlan(findPlan);

        // 2023.7.27(목) 2h35 회원 가입 - 로그인 - 계산 - 저장 - 목록 조회 테스트 하다 생각난 점 보완 = 처음 계산 시 deadline 지정 안 했어도, 위 과정에서 계산 결과에 따른 deadlineDate가 생기는 바, 이 날짜로 정보를 저장하자
        if (!findPlan.getHasDeadline()) {
            int year = Integer.parseInt(findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfYear());
            int month = findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfMonth();
            int date = Integer.parseInt(findPlan.getActionDatesList().get(findPlan.getActionDatesList().size() - 1).getNumOfDate());

            findPlan.setDeadlineDate(LocalDate.of(year, month, date));
        }

        planRepository.save(findPlan);
    }

    public Plan findVerifiedPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PLAN_NOT_FOUND));
    }

    // 2023.7.24(월) 17h20 자동 기본 구현만 해둠 -> 23h10 내용 구현
    @Override
    public List<MyPlanListResponseDto> findPlansByMember(Long memberId, int currentPage, int size) {
        Member findMember = memberRepository.findById(memberId).orElse(null);
        return planRepository.findByMemberMemberId(findMember.getMemberId(), PageRequest.of(currentPage - 1, size, Sort.by("planId").descending()))
                .stream()
                .map(plan -> planMapper.toMyPlanListResponseDto(plan))
                .collect(Collectors.toList());
    }

    // 2023.7.28(금) 1h45 MapStruct 사용하여 수정
    @Override
    public MyPlanDetailResponseDto findPlanById(Long planId) {
        Plan findPlan = planRepository.findById(planId).orElse(null);

        return planMapper.toMyPlanDetailResponseDto(findPlan);
    }
}
