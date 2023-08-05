package org.knou.keyproject.domain.actiondate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.knou.keyproject.domain.actiondate.entity.QActionDate.actionDate;

// 2023.8.4(금) 21h55
@Slf4j
@RequiredArgsConstructor
@Repository
public class ActionDateCustomRepositoryImpl implements ActionDateCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Integer getAccumulatedRealActionQuantity(Long planId) {
//        JPAQuery<LocalDate> startDate = jpaQueryFactory
//                .select(actionDate.plan.startDate)
//                .from(actionDate.plan)
//                .where(actionDate.plan.planId.eq(planId));
//        log.info("actionDateCustomRepository에서 특정 planId의 startDate 찾아오기");
        return jpaQueryFactory
                .select(actionDate.realActionQuantity.sum())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId).and(actionDate.isDone)) // 오늘 기준 isDone이 true인 actionDate의 realAction 분량을 다 더함
                .fetchOne();
    }

    @Override
    public Integer getAccumulatedPlanActionQuantity(Long planId, LocalDate startDate) {
        return jpaQueryFactory
                .select(actionDate.planActionQuantity.sum())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId).and(actionDate.dateFormat.between(startDate.toString(), LocalDate.now().toString()))) // 해당 plan 시작일~오늘 계획된 분량들의 합
                .fetchOne();
    }

    // 2023.8.5(토) 2h20
    @Override
    public Integer getAccumulatedPlanActionQuantityBeforePause(Long planId, LocalDate startDate, LocalDate lastStatusChangedAt) {
        return jpaQueryFactory
                .select(actionDate.planActionQuantity.sum())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId).and(actionDate.realActionDate.between(startDate, lastStatusChangedAt)))
                .fetchOne();
    }

    @Override
    public Integer getAverageTimeTakenForRealAction(Long planId) {
        Double result = jpaQueryFactory
                .select(actionDate.timeTakenForRealAction.avg())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId).and(actionDate.isDone)) // 오늘 기준 isDone이 true인 actionDate의 실행 소요 시간을 다 더함
                .fetchOne();

        if (result != null) {
            return Math.toIntExact(Math.round(result));
        }

        return 0;
    }

    @Override
    public Integer getAccumulatedNumOfActions(Long planId) {
        Long result = jpaQueryFactory
                .select(actionDate.actionDateId.count())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId).and(actionDate.isDone))
                .fetchOne();

        if (result != null) {
            return Math.toIntExact(result);
        }

        return 0;
    }

    // plan 엔티티의 lastStatusChangedAt 속성 쓰면 되지, 이 메서드 필요 없을 듯?
    @Override
    public LocalDate getCompleteDate(Long planId) {
        return jpaQueryFactory
                .select(actionDate.realActionDate.max())
                .from(actionDate)
                .where(actionDate.plan.planId.eq(planId))
                .fetchOne();
    }
}
