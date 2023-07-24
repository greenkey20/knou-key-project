package org.knou.keyproject.domain.plan.entity;

import org.knou.keyproject.domain.plan.dto.PlanPostRequestDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// 2023.7.24(월) 19h50
public class Calculator {
    private PlanPostRequestDto requestDto;

    // 계산 대상들을 멤버변수로 갖고 있음, 이 값은 requestDto로부터 그냥 넘어온 값 쓰면 안 되거나 거기에 없음
    private LocalDate startDate;
    private LocalDate deadlineDate;
    private String frequencyDetail;
    private Double frequencyFactor;
    private Integer totalDurationDays;
    private Integer totalNumOfActions;
    private Integer quantityPerDay;

    // 생성자
    public Calculator(PlanPostRequestDto requestDto) {
        this.requestDto = requestDto;
    }

    /**
     * 사용자로부터 입력받은 정보를 가지고, 활동 계획 수립에 필요한 정보로 변환 및 계산하는 메서드
     *
     * @return
     */
    public Plan calculateNewPlan() {
        // 현재로써는 측정 가능한 일만 이 계산기를 호출함

        Plan planToCalculate = requestDto.toEntity();

        setStartDate(planToCalculate); // 사용자가 아직 시작일을 모른다고 한 경우, 일단 금일 시작을 기준으로 계산 결과 알려줌
        // 2023.7.24(월) 22h 결국 setter 메서드 몇 개 만들어서 처리하기로 함
        planToCalculate.setStartDate(startDate);

        setDeadlineDate(planToCalculate);
        planToCalculate.setDeadlineDate(deadlineDate);

        setFrequencyDetail(planToCalculate);
        planToCalculate.setFrequencyDetail(frequencyDetail);

        setFrequencyFactor(planToCalculate);
        planToCalculate.setFrequencyFactor(frequencyFactor);

        setTotalDurationDays();
        planToCalculate.setTotalDurationDays(totalDurationDays);

        setTotalNumOfActions(planToCalculate);
        planToCalculate.setTotalNumOfActions(totalNumOfActions);

        setQuantityPerDay(planToCalculate);
        planToCalculate.setQuantityPerDay(quantityPerDay);

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
        if (!planToCalculate.getHasStartDate()) {
            startDate = LocalDate.now();
        } else {
            startDate = planToCalculate.getStartDate();
        }
    }

    public void setDeadlineDate(Plan planToCalculate) {
        if (!planToCalculate.getHasDeadline()) {
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
    }

    public void setFrequencyDetail(Plan planToCalculate) {
        FrequencyType frequencyType = planToCalculate.getFrequencyType();
        StringBuilder sb = new StringBuilder().append(frequencyDetail);

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
    }

    public void setFrequencyFactor(Plan planToCalculate) {
        FrequencyType frequencyType = planToCalculate.getFrequencyType();

        switch (frequencyType) {
            case DATE:
                frequencyFactor = (frequencyDetail.length() - 4) / 7.0; // 예시) 월화수목금토일, 월수금, 월화수목금 등
                break;
            case EVERY:
                String[] words = frequencyDetail.split(" "); // 예시) 2일마다 1회, 5일마다 2회 등
                frequencyFactor = (double) Character.getNumericValue(words[1].charAt(0)) / Character.getNumericValue(words[0].charAt(0));
                break;
            case TIMES:
                words = frequencyDetail.split(" "); // 예시) 주 2회, 월 10회 등

                switch (words[0]) {
                    case "주":
                        frequencyFactor = Character.getNumericValue(words[1].charAt(0)) / 7.0;
                        break;
                    case "월":
                        frequencyFactor = Character.getNumericValue(words[1].charAt(0)) / (365 / 12.0);
                        break;
                }
        }
    }

    public void setTotalDurationDays() {
        Long result = ChronoUnit.DAYS.between(startDate, deadlineDate);
        totalDurationDays = result.intValue();
    }

    public void setTotalNumOfActions(Plan planToCalculate) {
        if (planToCalculate.getHasDeadline()) {
            totalNumOfActions = (int) (totalDurationDays * frequencyFactor);
        } else {
            totalNumOfActions = (int) Math.ceil(planToCalculate.getTotalQuantity() / planToCalculate.getQuantityPerDayPredicted());
        }
    }

    public void setQuantityPerDay(Plan planToCalculate) {
        if (planToCalculate.getHasDeadline()) {
            quantityPerDay = (int) Math.ceil(planToCalculate.getTotalQuantity() / totalNumOfActions);
        } else {
            quantityPerDay = planToCalculate.getQuantityPerDayPredicted();
        }
    }
}
