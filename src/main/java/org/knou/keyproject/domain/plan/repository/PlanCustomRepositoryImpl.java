package org.knou.keyproject.domain.plan.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.domain.plan.entity.QPlan;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static org.knou.keyproject.domain.plan.entity.QPlan.plan;

// 2023.8.4(금) 21h50
@Slf4j
@RequiredArgsConstructor
@Repository
public class PlanCustomRepositoryImpl implements PlanCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Plan> findAllActivePlansByMemberMemberId(Long memberId) {
        return jpaQueryFactory
                .selectFrom(plan)
                .where(plan.member.memberId.eq(memberId).and(plan.status.eq(PlanStatus.ACTIVE)))
                .orderBy(plan.planId.desc())
                .fetch();
    }

    // 2023.8.7(월) 16h5
    @Override
    public List<Plan> findMyPlans(Long memberId) {
        return jpaQueryFactory
                .selectFrom(plan)
                .where(plan.member.memberId.eq(memberId))
                .orderBy(plan.planId.desc())
                .fetch();
    }

    @Override
    public List<Plan> findAllPlansByMemberMemberId(Long memberId) {
        return jpaQueryFactory
                .selectFrom(plan)
                .where(plan.member.memberId.eq(memberId))
                .orderBy(plan.planId.desc())
                .fetch();
    }

}
