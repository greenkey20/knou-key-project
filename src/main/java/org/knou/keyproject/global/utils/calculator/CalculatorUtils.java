package org.knou.keyproject.global.utils.calculator;

// 2023.7.29(금) 3h30

import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.entity.DateType;
import org.knou.keyproject.domain.plan.entity.Plan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculator 클래스로부터 기능적인 메서드들 분리해서 모아둠
 */
@Slf4j
public class CalculatorUtils {
    // 2023.7.29(토) 0h45 ActionDate 객체들이 만들어져서 db에 저장될 때 planId가 null인 것 보고, 이 부분에 추가해봄
    // -> 그런데 이렇게 FK를 수동으로 넣는 게 이상한 것 같은데.. 애초에 이렇게 ActionDate의 생성자를 사용하지 말았어야 하나..
    private static ActionDate changeActionDateIntoActionDateData(Plan planToCalculate, LocalDate date, Integer quantityPerDay) {
        return ActionDate.builder()
                .numOfYear(String.valueOf(date.getYear()))
                .numOfMonth(date.getMonthValue())
                .numOfDate(String.valueOf(date.getDayOfMonth()))
                .numOfDay(date.getDayOfWeek().getValue())
                .dateType(DateType.ACTION)
                .schedule("action")
                .dateFormat(String.format("%s-%02d-%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth()))
                .planActionQuantity(quantityPerDay)
                .isDone(false)
                .plan(planToCalculate)
                .build();
    }

    public static String extractActionDaysFromStr(String frequencyDetail) {
        return frequencyDetail.substring(0, frequencyDetail.length() - 4);
    }

    public static void verifyLastElementPositive(List<ActionDate> actionDates) {
        if (actionDates.get(actionDates.size() - 1).getPlanActionQuantity() <= 0) {
            actionDates.remove(actionDates.size() - 1);
        }

        // 2023.7.29(토) 3h 나의 생각 = 이렇게 유효하지 않은 값의 마지막 ActionDate 요소 제거한 뒤, totalNumOfActions의 값도 1 감소시켜야 하나? -> totalNumOfActions는 어차피 근사값인 바, 그런 값의 정확성을 굳이 고칠 필요는 없을 것 같다
    }

    // 2023.7.29(토) 3h5 extract a separate method for refactoring
    public static Map<String, Integer> extractIntervalAndTimesFromStr(String frequencyDetail) {
        Map<String, Integer> results = new HashMap<>();

        int interval = Integer.parseInt(extractNumsFromStr(frequencyDetail.split(" ")[0]));
        results.put("interval", interval);

        int times = Integer.parseInt(extractNumsFromStr(frequencyDetail.split(" ")[1]));
        results.put("times", times);

        log.info("calculatorUtils에서 interval and times from a str= " + results);
        return results;
    }

    // 2023.7.29(토) 2h50 공통 기능을 메서드로 뽑아내기 refactoring
    public static String extractNumsFromStr(String obj) {
        StringBuilder nums = new StringBuilder();

        for (int i = 0; i < obj.length(); i++) {
            char ch = obj.charAt(i);

            if (Character.isDigit(ch)) nums.append(ch);
        }

        return nums.toString();
    }

    public static int getIntervalFromTimeUnit(char timeUnit) {
        int interval = 0;

        switch (timeUnit) {
            case '주':
                interval = 7;
                break;
            case '월':
                interval = 30;
                break;
        }

        return interval;
    }

    public static boolean checkIfLastActionDate(Plan planToCalculate, List<ActionDate> actionDates, LocalDate date) {
        int leftUnit = planToCalculate.getTotalQuantity() - actionDates.size() * planToCalculate.getQuantityPerDay();
        if (leftUnit < planToCalculate.getQuantityPerDay()) {
            actionDates.add(changeActionDateIntoActionDateData(planToCalculate, date, leftUnit));
            return true;
        } else {
            actionDates.add(changeActionDateIntoActionDateData(planToCalculate, date, planToCalculate.getQuantityPerDay()));
        }
        return false;
    }

    // 2023.7.26(수) 17h refactoring = extract as a function
    // 빈도 조건에 맞는 활동일 찾기
    public static List<ActionDate> getActionDatesWithDeadlineAndIntervalAndTimes(Plan planToCalculate, List<ActionDate> actionDates, int interval, int times) {
        for (LocalDate date = planToCalculate.getStartDate(); date.isBefore(planToCalculate.getDeadlineDate()); date = date.plusDays(interval)) {
            if (checkIfLastActionDate(planToCalculate, actionDates, date)) break;

            getActionDatesWithinInterval(planToCalculate, actionDates, interval, times, date);
        }

        verifyLastElementPositive(actionDates);

        return actionDates;
    }

    public static List<ActionDate> getActionDatesWithNoDeadlineAndIntervalAndTimes(Plan planToCalculate, List<ActionDate> actionDates, int interval, int times) {
        LocalDate date = planToCalculate.getStartDate();

        int accumulatedUnit = 0;
        while (accumulatedUnit <= planToCalculate.getTotalQuantity()) {
            if (checkIfLastActionDate(planToCalculate, actionDates, date)) break;

            getActionDatesWithinInterval(planToCalculate, actionDates, interval, times, date);

            accumulatedUnit = actionDates.size() * planToCalculate.getQuantityPerDay();
            date = date.plusDays(interval);
        }

//        if (actionDates.get(actionDates.size() - 1).getPlanActionQuantity() < 0) {
//            actionDates.remove(actionDates.size() - 1);
//        }
        verifyLastElementPositive(actionDates);

        return actionDates;
    }

    public static void getActionDatesWithinInterval(Plan planToCalculate, List<ActionDate> actionDates, int interval, int times, LocalDate date) {
        LocalDate nextDate = date;
        int plusDay = interval / times;
        for (int i = 1; i < times; i++) {
            nextDate = nextDate.plusDays(plusDay);

            // 2023.7.26(수) 18h50 이 예외 처리 안 해서 isAfter() 호출 시 null pointer exception 발생
            if (planToCalculate.getHasDeadline()) {
                if (nextDate.isAfter(planToCalculate.getDeadlineDate())) break;
            }

            if (checkIfLastActionDate(planToCalculate, actionDates, nextDate)) break;
        }
    }

    public static List<Integer> getActionWeekdays(String daysStr) {
        List<Integer> daysList = new ArrayList<>();
        int day = -1;

        for (int i = 0; i < daysStr.length(); i++) {
            char ch = daysStr.charAt(i);

            switch (ch) {
                case '월':
                    day = 1;
                    daysList.add(day);
                    break;
                case '화':
                    day = 2;
                    daysList.add(day);
                    break;
                case '수':
                    day = 3;
                    daysList.add(day);
                    break;
                case '목':
                    day = 4;
                    daysList.add(day);
                    break;
                case '금':
                    day = 5;
                    daysList.add(day);
                    break;
                case '토':
                    day = 6;
                    daysList.add(day);
                    break;
                default: // 2023.7.27(목) 22h35 발견 <- 계산 결과 list에서 일요일이 없었음
                    day = 7;
                    daysList.add(day);
            }
        }

        return daysList;
    }
}
