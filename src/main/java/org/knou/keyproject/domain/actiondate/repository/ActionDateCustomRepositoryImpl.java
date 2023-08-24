package org.knou.keyproject.domain.actiondate.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    // 2023.8.24(목) 16h10
    /**
     * 특정 회원이 참여 중인(ACTIVE) plans 관련 actionDates 중 수행 예정일이 오늘인 것을 조회하는 메서드
     * @param memberId
     * @param todayFormat
     * @return
     */
    @Override
    public List<ActionDate> getMyTodayActionDates(Long memberId, String todayFormat) {
        return jpaQueryFactory
                .selectFrom(actionDate)
                .where(actionDate.plan.member.memberId.eq(memberId).and(actionDate.dateFormat.eq(todayFormat)).and(actionDate.plan.status.eq(PlanStatus.ACTIVE)))
                .orderBy(actionDate.plan.planId.desc())
                .fetch();
    }

    // 2023.8.24(목) 16h55
    @Override
    public List<String> getActionDatesListByMemberAndYearAndMonth(int year, int month, Long memberId) {
        // select distinct(date_format) from action_date where num_of_year = 2023 and num_of_month = 8 order by num_of_date asc;
        return jpaQueryFactory
                .selectDistinct(actionDate.dateFormat)
                .from(actionDate)
                .where(actionDate.plan.member.memberId.eq(memberId).and(actionDate.numOfYear.eq(String.valueOf(year))).and(actionDate.numOfMonth.eq(month)))
                .orderBy(actionDate.numOfDate.asc())
                .fetch();
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
