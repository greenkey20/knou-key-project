package org.knou.keyproject.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;
import org.knou.keyproject.domain.plan.entity.DeadlineType;
import org.knou.keyproject.domain.plan.entity.FrequencyType;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.domain.plan.entity.PlanStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

// 2023.7.24(월) 19h50
@Slf4j
public class Calculator {
    private PlanPostRequestDto requestDto;
//    private Plan plan;

    // 계산 대상들을 멤버변수로 갖고 있음, 이 값은 requestDto로부터 그냥 넘어온 값 쓰면 안 되거나 거기에 없음
//    private LocalDate startDate;
//    private LocalDate deadlineDate;
//    private String frequencyDetail;
//    private Double frequencyFactor;
//    private Integer totalDurationDays;
//    private Integer totalNumOfActions;
//    private Integer quantityPerDay;

    // 생성자
    public Calculator() {
    }

    public Calculator(PlanPostRequestDto requestDto) {
        this.requestDto = requestDto;
//        this.plan = null;
    }

//    public Calculator(Plan plan) {
//        this.plan = plan;
////        this.requestDto = null;
//    }

    // 2023.7.25(목) '나의 일정'에 저장하며 시작일이 새로 지정된 경우 호출되는 메서드
    public Plan calculateRealNewPlan(Plan planToCalculate) {
        setDeadlineDate(planToCalculate);
        planToCalculate.setActionDatesList();
        return planToCalculate;
    }


    /**
     * 사용자로부터 입력받은 정보를 가지고, 활동 계획 수립에 필요한 정보로 변환 및 계산하는 메서드
     *
     * @return
     */
    public Plan calculateNewPlan(PlanPostRequestDto requestDto) {
        // 현재로써는 측정 가능한 일만 이 계산기를 호출함
        Plan planToCalculate = requestDto.toEntity();

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

        setQuantityPerDay(planToCalculate);
//        planToCalculate.setQuantityPerDay(quantityPerDay);

        planToCalculate.setActionDatesList();

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
                frequencyFactor = (double) Character.getNumericValue(words[1].charAt(0)) / Character.getNumericValue(words[0].charAt(0));
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
    }

    public void setTotalNumOfActions(Plan planToCalculate) {
        Double frequencyFactor = planToCalculate.getFrequencyFactor();
        Integer totalDurationDays = planToCalculate.getTotalDurationDays();
        Integer totalNumOfActions = 0;

        if (planToCalculate.getHasDeadline()) {
            totalNumOfActions = (int) (totalDurationDays * frequencyFactor);
        } else {
            totalNumOfActions = (int) (Math.ceil(planToCalculate.getTotalQuantity() / planToCalculate.getQuantityPerDayPredicted())) + 1;
        }

        planToCalculate.setTotalNumOfActions(totalNumOfActions);
    }

    public void setQuantityPerDay(Plan planToCalculate) {
        Integer totalNumOfActions = planToCalculate.getTotalNumOfActions();
        Integer quantityPerDay = 0;

        if (planToCalculate.getHasDeadline()) {
            quantityPerDay = (int) (Math.ceil(planToCalculate.getTotalQuantity() / totalNumOfActions)) + 1;
        } else {
            quantityPerDay = planToCalculate.getQuantityPerDayPredicted();
        }

        planToCalculate.setQuantityPerDay(quantityPerDay);
    }

    // 2023.7.26(수) 18h15 활동일 리스트 구하는 메서드들 Plan 엔티티 클래스로부터 여기로 분리?!
}
