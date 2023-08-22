package org.knou.keyproject.global.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.repository.ActionDateRepository;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// 2023.8.5(토) 16h55 어제 PlanServiceImpl에 만든 plan 통계 관련 메서드들을 여기로 옮겨서, planService 외의 곳에서도 사용할 수 있도록 하고자 함
@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class PlanStatisticUtils {
    private final ActionDateRepository actionDateRepository;

    // 2023.8.5(토) 4h10 숫자 null -> 0으로 처리하기 위해 만듦
    public int makeNullAsZero(Integer num) {
        if (num != null) {
            return num;
        }

        return 0;
    }

    public Integer getAccumulatedRealActionQuantity(Long planId) {
        return makeNullAsZero(actionDateRepository.getAccumulatedRealActionQuantity(planId));
    }

    public Integer getAccumulatedPlanActionQuantity(Long planId, LocalDate startDate) {
        return makeNullAsZero(actionDateRepository.getAccumulatedPlanActionQuantity(planId, startDate));
    }

    public Integer getAccumulatedPlanActionQuantityBeforePause(Long planId, LocalDate startDate, LocalDate lastStatusChangedAt) {
        return makeNullAsZero(actionDateRepository.getAccumulatedPlanActionQuantityBeforePause(planId, startDate, lastStatusChangedAt));
    }

    public Integer getQuantityDifferenceBetweenPlanAndReal(Integer plan, Integer real) {
        return plan - real; // 양의 정수 = 계획보다 실행이 뒤처지고 있음 vs 음의 정수 = 계획보다 더 많이 실행해서 일정보다 앞서고 있음
    }

    public Integer getQuantityToEndPlan(Integer total, Integer real) {
        return total - real;
    }

    public Double getRatioOfRealActionQuantityTillToday(Integer real, Integer total) {
        return (double) real / total * 100;
    }

    public Double getRatioOfQuantityToEndPlan(Integer real, Integer total) {
        return (1 - (double) real / total) * 100;
    }

    public String formatPercentage(Double num) {
        DecimalFormat df = new DecimalFormat("###.#");
        return df.format(num);
    }

    public Integer getNumOfActionsToEndPlan(Integer total, Integer real) {
        return total - real;
    }

    public Integer getAverageTimeTakenForRealAction(Long planId) {
        return actionDateRepository.getAverageTimeTakenForRealAction(planId);
    }

    public Integer getAccumulatedNumOfActions(Long planId) {
        return actionDateRepository.getAccumulatedNumOfActions(planId);
    }

    // 2023.8.5(토) 17h40 plan resume 시 deadline 새로 계산(Calculator)하며 추가
    public Integer getPeriodDaysBeforePause(Plan resumedPlan, LocalDate originalStartDate) {
//        LocalDate lastActionDateBeforePause = actionDateRepository.getLastActionDateBeforePause(planId);
        log.info("plan 통계 utils의 getPeriodDaysBeforePause()에 들어오는 originalStartDate = " + originalStartDate);

        LocalDate pauseDate = resumedPlan.getLastStatusChangedAt();
        log.info("plan 통계 utils의 getPeriodDaysBeforePause()에서 구해진 pauseDate = " + pauseDate);

        if (pauseDate != null) {
            return Math.toIntExact(ChronoUnit.DAYS.between(originalStartDate, pauseDate));
        }

        return 0;
    }
}
