package org.knou.keyproject.global.utils.calculator;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.plan.entity.DeadlineType;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;
import org.knou.keyproject.global.utils.PlanStatisticUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.knou.keyproject.global.utils.calculator.CalculatorUtils.*;

// 2023.7.24(월) 19h50
@Slf4j
//@NoArgsConstructor // constructor ... is already defined in class ... 빌드 오류 때문에 주석 처리하고, 기본 생성자 수기로 만듦
@Getter
//@AllArgsConstructor
//@Builder
@RequiredArgsConstructor
@Component
public class Calculator {
//    private PlanPostRequestDto requestDto;
//    private Plan plan;

    // 계산 대상들을 멤버변수로 갖고 있음, 이 값은 requestDto로부터 그냥 넘어온 값 쓰면 안 되거나 거기에 없음
//    private LocalDate startDate;
//    private LocalDate deadlineDate;
//    private String frequencyDetail;
//    private Double frequencyFactor;
//    private Integer totalDurationDays;
//    private Integer totalNumOfActions;
//    private Integer quantityPerDay;
    private final PlanStatisticUtils planStatisticUtils;

    // 생성자
//    public Calculator() {
//    }

//    public Calculator(PlanPostRequestDto requestDto) {
//        this.requestDto = requestDto;
////        this.plan = null;
//    }

//    public Calculator(Plan plan) {
//        this.plan = plan;
////        this.requestDto = null;
//    }

    /**
     * 사용자로부터 입력받은 정보를 가지고, 활동 계획 수립에 필요한 정보로 변환 및 계산하는 메서드
     *
     * @return
     */
    public Plan calculateNewPlan(Plan planToCalculate) {
        // 현재로써는 측정 가능한 일만 이 계산기를 호출함
//        Plan planToCalculate = requestDto.toEntity();

//        if (requestDto != null) {
//            planToCalculate = requestDto.toEntity();
//        } else {
//            planToCalculate = plan;
//        }

        setStartDate(planToCalculate); // 사용자가 아직 시작일을 모른다고 한 경우, 일단 금일 시작을 기준으로 계산 결과 알려줌
        // 2023.7.24(월) 22h 결국 setter 메서드 몇 개 만들어서 처리하기로 함
//        planToCalculate.setStartDate(startDate);

        setDeadlineDate(planToCalculate);
//        planToCalculate.setDeadlineDate(deadlineDate);

        setFrequencyDetail(planToCalculate);
//        planToCalculate.setFrequencyDetail(frequencyDetail);

        setFrequencyFactor(planToCalculate);
//        planToCalculate.setFrequencyFactor(frequencyFactor);
//        if (planToCalculate.getStatus() == PlanStatus.RESULT) {
//
//        }

        setTotalDurationDays(planToCalculate);
//        planToCalculate.setTotalDurationDays(totalDurationDays);

        setTotalNumOfActions(planToCalculate);
//        planToCalculate.setTotalNumOfActions(totalNumOfActions);

        if (!planToCalculate.getIsMeasurable()) {
            setTotalQuantity(planToCalculate);
        }

        setQuantityPerDay(planToCalculate);
//        planToCalculate.setQuantityPerDay(quantityPerDay);

        setActionDatesList(planToCalculate);
//        planToCalculate.setActionDatesList(); // 2023.7.29(토) 3h30 교체 완료

        return planToCalculate;
        // 2023.7.24(월) 21h35 아래와 같은 return문을 썼는데, 아무래도 이건 아닌 것 같아..
        /*
        return Plan.builder()
                .isMeasurable(planToCalculate.getIsMeasurable())
                .object(planToCalculate.getObject())
                .totalQuantity(planToCalculate.getTotalQuantity())
                .unit(planToCalculate.getUnit())
                .hasStartDate(planToCalculate.getHasStartDate())
                .startDate(startDate)
                .frequencyType(planToCalculate.getFrequencyType())
                .frequencyDetail(planToCalculate.getFrequencyDetail())
                .hasDeadline(planToCalculate.getHasDeadline())
                .deadlineType(planToCalculate.getDeadlineType())
                .deadlineDate(deadlineDate)
                .deadlinePeriod(planToCalculate.getDeadlinePeriod())
                .quantityPerDayPredicted(planToCalculate.getQuantityPerDayPredicted())
                .totalDurationDays(totalDurationDays) // 계산 결과
                .totalNumOfActions(totalNumOfActions) // 계산 결과
                .quantityPerDay(quantityPerDay) // 계산 결과
                .build();
         */
    }

    // 2023.7.25(목) '나의 일정'에 저장하며 시작일이 새로 지정된 경우 호출되는 메서드
    public Plan calculateRealNewPlan(Plan planToCalculate) {
        // 2023.8.5(토) 16h 로직 수정
        if (planToCalculate.getHasDeadline()) {
            if (planToCalculate.getDeadlineType().equals(DeadlineType.PERIOD)) { // deadlineType이 PERIOD로써, 마감일이 시작일 대비 정해지는 경우 -> planToCalculate.getDeadlineDate() == null인 경우는 조정할 필요 없는 게 맞지?!
                log.info("calculator에서 계산 전 deadline date" + planToCalculate.getDeadlineDate());
                setDeadlineDate(planToCalculate);
                log.info("calculator에서 계산 후 deadline date" + planToCalculate.getDeadlineDate());

//                setActionDatesList(planToCalculate);
            } else { // deadlineType이 DATE로써, 마감일이 딱 정해져 있는 경우
                setTotalDurationDays(planToCalculate);
                setTotalNumOfActions(planToCalculate);
                setQuantityPerDay(planToCalculate);
//                setActionDatesList(planToCalculate);
            }
        }

        setActionDatesList(planToCalculate);

//        planToCalculate.setActionDatesList(); // 2023.7.29(토) 3h30 교체 완료
        return planToCalculate;
    }

    // 2023.8.5(토) 16h45 추가
    public Plan calculateResumePlan(Plan resumedPlan) {
        LocalDate originalStartDate = resumedPlan.getStartDate();

        setResumeStartDate(resumedPlan);
        setResumeDeadlineDate(resumedPlan, originalStartDate);
        setTotalDurationDays(resumedPlan);
        setTotalNumOfActions(resumedPlan);
        setResumeTotalQuantity(resumedPlan);
        setQuantityPerDay(resumedPlan);
        setActionDatesList(resumedPlan);

        return resumedPlan;
    }

    private void setResumeStartDate(Plan resumedPlan) {
        resumedPlan.setStartDate(LocalDate.now());
    }

    private void setResumeDeadlineDate(Plan resumedPlan, LocalDate originalStartDate) {
        LocalDate startDate = resumedPlan.getStartDate();
        LocalDate deadlineDate = resumedPlan.getDeadlineDate();

        if (resumedPlan.getHasDeadline()) {
            if (resumedPlan.getDeadlineType() == DeadlineType.PERIOD) {
                String deadlinePeriod = resumedPlan.getDeadlinePeriod(); // x일/주/개월

                StringBuilder nums = new StringBuilder();
                StringBuilder unit = new StringBuilder();
                for (int i = 0; i < deadlinePeriod.length(); i++) {
                    char ch = deadlinePeriod.charAt(i);

                    if (Character.isDigit(ch)) {
                        nums.append(ch);
                    } else {
                        unit.append(ch);
                    }
                }

                int endDatePeriod = Integer.parseInt(nums.toString());
                int periodDaysBeforePause = planStatisticUtils.getPeriodDaysBeforePause(resumedPlan, originalStartDate);

                String endDatePeriodUnit = unit.toString();

                switch (endDatePeriodUnit) {
                    case "일":
                        deadlineDate = startDate.plusDays(endDatePeriod).minusDays(periodDaysBeforePause);
                        break;
                    case "주":
                        deadlineDate = startDate.plusWeeks(endDatePeriod).minusDays(periodDaysBeforePause);
                        break;
                    case "개월":
                        deadlineDate = startDate.plusMonths(endDatePeriod).minusDays(periodDaysBeforePause);
                        break;
                }
            } else {
                deadlineDate = resumedPlan.getDeadlineDate();
            }

            resumedPlan.setDeadlineDate(deadlineDate);
        }
    }

    private void setResumeTotalQuantity(Plan resumedPlan) {
        Integer originalTotalQuantity = resumedPlan.getTotalQuantity();
        Integer accumulatedRealActionQuantity = planStatisticUtils.getAccumulatedRealActionQuantity(resumedPlan.getParentPlan().getPlanId());
        resumedPlan.setTotalQuantity(originalTotalQuantity - accumulatedRealActionQuantity);
    }

    public void setStartDate(Plan planToCalculate) {
        LocalDate startDate = planToCalculate.getStartDate();

        if (!planToCalculate.getHasStartDate() && planToCalculate.getStatus() == PlanStatus.RESULT) {
            startDate = LocalDate.now();
        } else {
            startDate = planToCalculate.getStartDate();
        }

        planToCalculate.setStartDate(startDate);
    }

    public void setDeadlineDate(Plan planToCalculate) {
        LocalDate startDate = planToCalculate.getStartDate();
        LocalDate deadlineDate = planToCalculate.getDeadlineDate();

        if (planToCalculate.getHasDeadline()) {
            if (planToCalculate.getDeadlineType() == DeadlineType.PERIOD) {
                String deadlinePeriod = planToCalculate.getDeadlinePeriod(); // x일/주/개월

                StringBuilder nums = new StringBuilder();
                StringBuilder unit = new StringBuilder();
                for (int i = 0; i < deadlinePeriod.length(); i++) {
                    char ch = deadlinePeriod.charAt(i);

                    if (Character.isDigit(ch)) {
                        nums.append(ch);
                    } else {
                        unit.append(ch);
                    }
                }

                int endDatePeriod = Integer.parseInt(nums.toString());
                String endDatePeriodUnit = unit.toString();

                switch (endDatePeriodUnit) {
                    case "일":
                        deadlineDate = startDate.plusDays(endDatePeriod);
                        break;
                    case "주":
                        deadlineDate = startDate.plusWeeks(endDatePeriod);
                        break;
                    case "개월":
                        deadlineDate = startDate.plusMonths(endDatePeriod);
                        break;
                }
            } else {
                deadlineDate = planToCalculate.getDeadlineDate();
            }

            planToCalculate.setDeadlineDate(deadlineDate);
        }
        // 처음 계산 시 deadline 없다/모른다고 한 경우, PLAN 엔티티/테이블에는 deadlineDate 저장 안 됨 + 마지막 날 알고 싶으면 해당 plan 관련 actionDates 마지막 날 찾아야 함
    }

    public void setFrequencyDetail(Plan planToCalculate) {
        String frequencyDetail = planToCalculate.getFrequencyDetail();

        FrequencyType frequencyType = planToCalculate.getFrequencyType();
        StringBuilder sb = new StringBuilder();
        sb.append(frequencyDetail);

        switch (frequencyType) {
            case DATE:
                sb.append("요일마다");
                break;
            case EVERY:
                sb.append("씩");
                String[] words = frequencyDetail.split(" ");
                break;
        }

        frequencyDetail = sb.toString();
        planToCalculate.setFrequencyDetail(frequencyDetail);
    }

    public void setFrequencyFactor(Plan planToCalculate) {
//        log.info("setFrequencyFactor 메서드 안에 들어는 오나? 2023.7.25(화) 18h15"); // 18h35 현재 ok
        Double frequencyFactor = planToCalculate.getFrequencyFactor();
        FrequencyType frequencyType = planToCalculate.getFrequencyType();
        String frequencyDetail = planToCalculate.getFrequencyDetail();

        switch (frequencyType) {
            case DATE:
                frequencyFactor = (frequencyDetail.length() - 4) / 7.0; // 예시) 월화수목금토일, 월수금, 월화수목금 등
                break;
            case EVERY:
                String[] words = frequencyDetail.split(" "); // 예시) 2일마다 1회, 5일마다 2회 등
                log.info("frequencyFactor 계산하는 과정에서 words = " + Arrays.toString(words));
                int interval = Integer.parseInt(extractNumsFromStr(words[0]));
                int times = Integer.parseInt(extractNumsFromStr(words[1]));

                frequencyFactor = (double) times / interval;
                break;
            case TIMES:
                words = frequencyDetail.split(" "); // 예시) 주 2회, 월 10회 등

                // 2023.7.26(수) 1h 횟수가 2자리 수 이상인 경우를 위해 로직 수정 필요했음
                // x(x)회의 숫자 x(x) 뽑아내야 함
                String obj = words[1];
                StringBuilder nums = new StringBuilder();

                // 아래 반복문도 deadlinePeriod 문자열로부터 deadline 뽑아낼 때, TIMES 빈도로부터 활동일 뽑아낼 때 등 비슷하게 사용되므로, 메서드로 추출 가능?!
                for (int i = 0; i < obj.length(); i++) {
                    char ch = obj.charAt(i);

                    if (Character.isDigit(ch)) {
                        nums.append(ch);
                    }
                }

                switch (words[0]) {
                    case "주":
                        frequencyFactor = Integer.parseInt(nums.toString()) / 7.0;
                        break;
                    case "월":
                        frequencyFactor = Integer.parseInt(nums.toString()) / (365 / 12.0);
                        break;
                }
        }

        planToCalculate.setFrequencyFactor(frequencyFactor);
    }

    public void setTotalDurationDays(Plan planToCalculate) {
        LocalDate startDate = planToCalculate.getStartDate();
        LocalDate deadlineDate = planToCalculate.getDeadlineDate();

        if (planToCalculate.getHasDeadline()) {
            Long result = ChronoUnit.DAYS.between(startDate, deadlineDate);
            Integer totalDurationDays = result.intValue();
            planToCalculate.setTotalDurationDays(totalDurationDays);
        }
        // 처음 계산 시 deadline 없다/모른다고 한 경우, 처음 계산 결과에는 '기간' 정보 제공 안 함
    }

    public void setTotalNumOfActions(Plan planToCalculate) {
        Double frequencyFactor = planToCalculate.getFrequencyFactor();
        Integer totalDurationDays = planToCalculate.getTotalDurationDays();
        Integer totalNumOfActions = 0;

        // 2023.8.2(수) 2h10 ChatGpt 계산기 구현하며 분기
        if (planToCalculate.getIsMeasurable()) {
            if (planToCalculate.getHasDeadline()) {
                totalNumOfActions = (int) (totalDurationDays * frequencyFactor);
            } else {
                double delim = planToCalculate.getTotalQuantity() / planToCalculate.getQuantityPerDayPredicted();

                if (delim != 0) {
                    totalNumOfActions = Math.round(planToCalculate.getTotalQuantity() / planToCalculate.getQuantityPerDayPredicted()) + 1;
                } else {
                    totalNumOfActions = planToCalculate.getTotalQuantity() / planToCalculate.getQuantityPerDayPredicted();
                }
            }
        } else { // 측정 어려운 활동의 경우, 계산기에서 deadlineType이 deadlinePeriod로만 가능하게 되어있음
            String deadlinePeriodUnit = planToCalculate.getDeadlinePeriodUnit();
            Integer deadlinePeriodNum = planToCalculate.getDeadlinePeriodNum();
            // 2023.8.5(토) 16h20 로직 수정
            int delim = (int) (deadlinePeriodNum * frequencyFactor);

            switch (deadlinePeriodUnit) {
                case "일":
                    totalNumOfActions = delim;
                    break;
                case "주":
                    totalNumOfActions = delim * 7;
                    break;
                case "개월":
                    totalNumOfActions = delim * 30;
                    break;
            }
        }

        planToCalculate.setTotalNumOfActions(totalNumOfActions);
    }

    private void setTotalQuantity(Plan planToCalculate) {
        planToCalculate.setTotalQuantity(planToCalculate.getTotalDurationDays()); // 2023.8.5(토) 16h35 로직 수정
    }

    // 2023.8.2(수) 2h25 수정해봄
    public void setQuantityPerDay(Plan planToCalculate) {
        Integer totalQuantity = planToCalculate.getTotalQuantity();
        Integer totalNumOfActions = planToCalculate.getTotalNumOfActions();
        Integer quantityPerDay = 0;

        if (planToCalculate.getHasDeadline()) {
            int delim = totalQuantity % totalNumOfActions;

            if (delim != 0) {
                quantityPerDay = Math.round(totalQuantity / totalNumOfActions) + 1;
            } else {
                quantityPerDay = totalQuantity / totalNumOfActions;
            }
        } else {
            quantityPerDay = planToCalculate.getQuantityPerDayPredicted();
        }

        planToCalculate.setQuantityPerDay(quantityPerDay); // 2023.8.5(토) 16h35 측정 불가능한 일의 경우도 deadline이 있는 일인 바, 계산 방식은 동일할 것 같아 로직 수정 + 측정 불가능한 일의 경우 quantityPerDay를 사용/제시하는 일이 없긴 함

//        if (planToCalculate.getIsMeasurable()) {
//
//        } else {
//            planToCalculate.setQuantityPerDay(1);
//        }
    }

    // 2023.7.26(수) 18h15 활동일 리스트 구하는 메서드들 Plan 엔티티 클래스로부터 여기로 분리?!
    // 2023.7.29(토) 2h20 위 비스무리한 작업을 지금 하게 되는 것 같다..?
    public void setActionDatesList(Plan planToCalculate) {
        List<ActionDate> actionDatesList = planToCalculate.getActionDatesList();

//        List<ActionDate> actionDays = new ArrayList<>();

        if (planToCalculate.getHasDeadline()) { // 기한이 있는 계획의 경우
            switch (planToCalculate.getFrequencyType()) {
                case DATE: // frequencyType이 DATE일 때 활동일 목록을 구함
                    actionDatesList = getActionDatesWithDeadlineAndFrequencyTypeDATE(planToCalculate, actionDatesList);
                    break;
                case EVERY: // EVERY-TIMES 계산 원리는 비슷
                    actionDatesList = getActionDatesWithDeadlineAndFrequencyTypeEVERY(planToCalculate, actionDatesList);
                    break;
                case TIMES:
                    actionDatesList = getActionDatesWithDeadlineAndFrequencyTypeTIMES(planToCalculate, actionDatesList);
                    break;
            }
        } else { // 기한이 없는 계획인 경우
            switch (planToCalculate.getFrequencyType()) {
                case DATE: // frequencyType이 DATE일 때 활동일 목록을 구함
                    actionDatesList = getActionDatesWithNoDeadlineAndFrequencyTypeDATE(planToCalculate, actionDatesList);
                    break;
                case EVERY: // EVERY-TIMES 계산 원리는 비슷
                    actionDatesList = getActionDatesWithNoDeadlineAndFrequencyTypeEVERY(planToCalculate, actionDatesList);
                    break;
                case TIMES:
                    actionDatesList = getActionDatesWithNoDeadlineAndFrequencyTypeTIMES(planToCalculate, actionDatesList);
                    break;
            }
        }

//        this.actionDatesList = actionDays;
        planToCalculate.setActionDatesList(actionDatesList);
    }

    private List<ActionDate> getActionDatesWithDeadlineAndFrequencyTypeDATE(Plan planToCalculate, List<ActionDate> actionDates) {
        // 활동하는 요일들의 정수 값을 리스트로 받음
        String daysStr = extractActionDaysFromStr(planToCalculate.getFrequencyDetail());
        List<Integer> daysList = getActionWeekdays(daysStr);
//        log.info("getActionDaysWithFrequencyTypeDATE에서 daysList = " + daysList);

        for (LocalDate date = planToCalculate.getStartDate(); date.isBefore(planToCalculate.getDeadlineDate()); date = date.plusDays(1)) {
            // 순회 중인 날이 해당 요일이면 활동일 리스트에 담음
            int dayOfDate = date.getDayOfWeek().getValue();
//            log.info("순회 중인 date의 요일 번호 = " + dayOfDate);

            if (daysList.contains(dayOfDate)) {
                if (checkIfLastActionDate(planToCalculate, actionDates, date)) break;
            }
        }

//        log.info("순회 마치고 actionDays 리스트 = " + getActionDatesList());
        return actionDates;
    }

    private List<ActionDate> getActionDatesWithNoDeadlineAndFrequencyTypeDATE(Plan planToCalculate, List<ActionDate> actionDates) {
        // 활동하는 요일들의 정수 값을 리스트로 받음
        String daysStr = extractActionDaysFromStr(planToCalculate.getFrequencyDetail());
        List<Integer> daysList = getActionWeekdays(daysStr);
//        log.info("getActionDaysWithFrequencyTypeDATE에서 daysList = " + daysList);

        LocalDate date = planToCalculate.getStartDate();

        int accumulatedUnit = 0;
        while (accumulatedUnit <= planToCalculate.getTotalQuantity()) {
            int dayOfDate = date.getDayOfWeek().getValue();

            if (daysList.contains(dayOfDate)) {
                if (checkIfLastActionDate(planToCalculate, actionDates, date)) break;
            }

            accumulatedUnit = actionDates.size() * planToCalculate.getQuantityPerDay();
            date = date.plusDays(1);
        }

        verifyLastElementPositive(actionDates);

//        log.info("순회 마치고 actionDays 리스트 = " + getActionDatesList());
        return actionDates;
    }

    /**
     * 매 x일마다 활동하는 경우의 활동일 리스트 만드는 메서드
     * e.g. 2일마다 1회, 5일마다 2회, 10일마다 3회 등 + 30일마다 11회 등 2자리 숫자가 interval 및 times로 올 수도 있음
     *
     * @return
     */
    private List<ActionDate> getActionDatesWithDeadlineAndFrequencyTypeEVERY(Plan planToCalculate, List<ActionDate> actionDates) {
        Map<String, Integer> results = extractIntervalAndTimesFromStr(planToCalculate.getFrequencyDetail());

        return getActionDatesWithDeadlineAndIntervalAndTimes(planToCalculate, actionDates, results.get("interval"), results.get("times"));
    }

    private List<ActionDate> getActionDatesWithNoDeadlineAndFrequencyTypeEVERY(Plan planToCalculate, List<ActionDate> actionDates) {
        Map<String, Integer> results = extractIntervalAndTimesFromStr(planToCalculate.getFrequencyDetail());

        return getActionDatesWithNoDeadlineAndIntervalAndTimes(planToCalculate, actionDates, results.get("interval"), results.get("times"));
    }

    /**
     * 주/월 x회마다 활동하는 경우의 활동일 리스트 만드는 메서드
     * e.g. 주 3회, 월 10회 등
     *
     * @return
     */
    private List<ActionDate> getActionDatesWithDeadlineAndFrequencyTypeTIMES(Plan planToCalculate, List<ActionDate> actionDates) {
        // timeUnit 관련 데이터 가공
        int interval = getIntervalFromTimeUnit(planToCalculate.getFrequencyDetail().charAt(0));

        // 활동 횟수 관련 데이터 가공
        String obj = planToCalculate.getFrequencyDetail().split(" ")[1];
        int times = Integer.parseInt(extractNumsFromStr(obj));

        return getActionDatesWithDeadlineAndIntervalAndTimes(planToCalculate, actionDates, interval, times);
    }

    private List<ActionDate> getActionDatesWithNoDeadlineAndFrequencyTypeTIMES(Plan planToCalculate, List<ActionDate> actionDates) {
        // timeUnit 관련 데이터 가공
        int interval = getIntervalFromTimeUnit(planToCalculate.getFrequencyDetail().charAt(0));

        // 활동 횟수 관련 데이터 가공
        String obj = planToCalculate.getFrequencyDetail().split(" ")[1];
        int times = Integer.parseInt(extractNumsFromStr(obj));

        return getActionDatesWithNoDeadlineAndIntervalAndTimes(planToCalculate, actionDates, interval, times);
    }
}
