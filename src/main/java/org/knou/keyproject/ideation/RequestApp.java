package org.knou.keyproject.ideation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

import static org.knou.keyproject.common.Util.printBoldLine;

// 2023.6.30(금) 1h50

/**
 * 매일 일정한 공부 계획 수립을 도와주는 서비스,
 * 또는 각자의 공부 계획을 공유하고, 평균적인 공부 계획을 세워 알려주는 서비스..
 */
public class RequestApp {
    Scanner scanner = new Scanner(System.in);
    String unit;
    LocalDateTime startDate;
    String frequencyDetailsInput;
    LocalDateTime endDate;
    int numOfWorkDays;

    public void view() {
        while (true) {
//            System.out.print("실행 시작 예정일 > ");
//            String inputDate = scanner.nextLine();

            // 2023.7.1(토) 3h25
            /* 로직의 문제, 가정해야 하는 것
            1. 선형적 상관 관계 e.g. 체중 조절이 시간과 정비례하지 않을 수 있음 -> 그런 경우 어떻게 계산해서 + 어떤 정보를 사용자에게 응답할 것인가?
            2. 1회독 후 복습이 필요한 경우
            3. 구간별 난이도가 다른 경우
            4. 동영상 강의 1개(자격증, 공무원 시험 등)나 단원별(knou 교재 등), 주차별 학습량(knou 커리큘럼)이 고르게/비슷비슷하게 분배되어있다고 가정
             */

            // 2023.7.1(토) 15h55 ~ 17h10 측정 가능한 일에 대한 간단 로직 구현
            // 무엇을 준비하고 계신가요? (측정 가능한 일 or 측정이 어려운 일 or 잘 모르겠어요) + 목표 기간이 이미 정해져 있나요? -> 경우의 수별 예시, 시나리오
            System.out.print("무엇을 준비하고 계신가요? (1. 측정 가능한 일 / 2. 측정이 어려운 일 / 3. 잘 모르겠어요) > ");
            int countability = Integer.parseInt(scanner.nextLine());

            switch (countability) {
                case 1:
                    planCountableTask();
                    break;
                case 2:
                    planUncountableTask();
                    break;
                case 3:
                    doNotKnowCountability();
                    break;
            }

            // 복습이 필요하다고 생각하시나요? (yes or no or 잘 모르겠어요, 도와주세요)

            // 본인의 가용 시간을 알고 계시나요? (주말만 가능, 주7일 가능, 주3일 가능, 그 때 그 때 다름)
        }
    }

    // 2023.7.1(토) 16h
    // [목표 대상에 따라]
    // 책, 교과서, 수험서, 악보, 원고, 과목 수, 예상 면접 질문 수, 체중 조절, 단어 암기 등
    public void planCountableTask() {
        System.out.print("수행 목표 대상은 무엇인가요? (예시: 책/교과서/수험서/악보/원고 등 pages, 과목/단어/문제 등 개수, 자격증, 체중 등) > ");
        String countableObject = scanner.nextLine();

        System.out.print("목표 수량/분량 > ");
        int countableQuantity = Integer.parseInt(scanner.nextLine());

        // 2023.7.8(토) 11h5 보완
        System.out.print("측정 단위 > ");
        unit = scanner.nextLine();

        // 목표 기간 설정
        System.out.print("시작일을 알고 있나요? (1. 네 2. 아니오) > ");
        int knowStartDate = Integer.parseInt(scanner.nextLine());

        switch (knowStartDate) {
            case 1:
                System.out.print("언제부터 시작할 예정인가요? (입력 형식 = YYYY-MM-DD) > ");
                String startDateInput = scanner.nextLine();
                startDate = dateFormatter(startDateInput);
                break;
            case 2:
                // 시작일이 아직 미정인 경우, 수행 빈도에 따라, 금일~달성 기한/기간까지 or 달성 기한이 정해져 있지 않은 경우 하루에 얼마큼의 단위씩 수행할 수 있을지에 따라, 분량을 알려줌
        }

        // 활동/수행 빈도 설정
        System.out.print("어떤 빈도로 수행할 예정인가요? 1. 특정 요일 선택 2. x일마다 1회 3. 주/월 x회 > ");
        int frequencyInput = Integer.parseInt(scanner.nextLine());
        System.out.print("구체적인 활동/수행 빈도를 입력해 주세요 ");
        double frequencyDetails = 0.0;

        switch (frequencyInput) {
            case 1:
                System.out.print("(예시: 월화수목금토일, 월수금, 월화수목금 등) > ");
                frequencyDetailsInput = scanner.nextLine() + "마다";
                frequencyDetails = (frequencyDetailsInput.length() - 2) / 7.0;
                break;
            case 2:
                System.out.print("(예시: 2일마다 1회, 5일마다 2회 등) > ");
                frequencyDetailsInput = scanner.nextLine() + "씩";
                String[] inputs1 = frequencyDetailsInput.split(" ");
                frequencyDetails = (double) Character.getNumericValue(inputs1[1].charAt(0)) / Character.getNumericValue(inputs1[0].charAt(0));
                break;
            case 3:
                System.out.print("(예시: 주 2회, 월 10회 등) > ");
                frequencyDetailsInput = scanner.nextLine();
                String[] inputs2 = frequencyDetailsInput.split(" ");
                if (inputs2[0].equals("주")) {
                    frequencyDetails = Character.getNumericValue(inputs2[1].charAt(0)) / 7.0;
                } else if (inputs2[0].equals("월")) {
                    frequencyDetails = Character.getNumericValue(inputs2[1].charAt(0)) / 30.0;
                }
                break;
        }

        int numOfWorkDays = planNumOfWorkDays(countableQuantity, startDate);

        // 추가 기능
//        System.out.print("1주일에 며칠 공부 예정인가요? > ");
//        int workDaysPerWeek = Integer.parseInt(scanner.nextLine());

        double quantityPerDay = countableQuantity / numOfWorkDays / frequencyDetails;
        System.out.println(countableObject + " " + countableQuantity + unit + " 분량 목표 달성을 위해서는 "
                + startDate + "부터 " + endDate + "까지 총 " + numOfWorkDays + "일동안 "
                + frequencyDetailsInput + " "
                + quantityPerDay + unit + "만큼 수행해야 합니다");
        printBoldLine();
    }

    // 기타 정량적 측정이 어려운 과제 e.g. 곰인형 만들기, 그림/디자인 작업, 안무/춤 등
    public void planUncountableTask() {
        System.out.println("서비스 준비 중입니다");
    }

    public void doNotKnowCountability() {
        System.out.println("서비스 준비 중입니다");
    }

    // 2023.7.1(토) 16h10
    public int planNumOfWorkDays(int countableQuantity, LocalDateTime startDate) {
        // [목표 기간 결정 여부]
        System.out.print("달성 목표/희망 기한이 정해져 있나요? (1. 네(시험, 면접, 보고서/과제 제출 등 날짜가 주어진 경우) / 2. 아니오, 아직 계획/생각 중이에요) > ");
        int isDeadlineSet = Integer.parseInt(scanner.nextLine());
//        Map<Boolean, Integer> result = new HashMap<>();

        int quantityPerDayPredicted = 0;

        switch (isDeadlineSet) {
            case 1: // 목표 기간을 아는/목표 기간이 정해진 경우 e.g. 시험, 발표회, 면접 등
                System.out.print("목표 기한 종류 (1. 특정 날짜 / 2. 기간(x일/주/개월 등)) > ");
                int deadlineType = Integer.parseInt(scanner.nextLine());

                if (deadlineType == 1) {
                    System.out.print("달성 목표 날짜 (입력 형식 = YYYY-MM-DD) > ");
                    String deadlineInput = scanner.nextLine();
                    endDate = dateFormatter(deadlineInput);
                } else {
                    System.out.print("달성 목표 기간 (입력 형식 = xx일/주/개월) > ");
                    String deadlineInput = scanner.nextLine();
                    int deadlinePeriod = 0;
                    String deadlinePeriodUnit = "";

                    StringBuilder nums = new StringBuilder();
                    StringBuilder unit = new StringBuilder();
                    for (int i = 0; i < deadlineInput.length(); i++) {
                        char ch = deadlineInput.charAt(i);

                        if (Character.isDigit(ch)) {
                            nums.append(ch);
                        } else {
                            unit.append(ch);
                        }
                    }

                    deadlinePeriod = Integer.parseInt(nums.toString());
                    deadlinePeriodUnit = unit.toString();
                    LocalDateTime today = LocalDateTime.now();
                    if (deadlinePeriodUnit.equals("주")) {
                        endDate = today.plusWeeks(deadlinePeriod);
                    } else if (deadlinePeriodUnit.equals("개월")) {
                        endDate = today.plusMonths(deadlinePeriod);
                    } else {
                        endDate = today.plusDays(deadlinePeriod);
                    }
                }

                /* 사용자로부터 간단하게 입력 받는 경우
                System.out.print("목표 기간 > ");
                int inputDays = Integer.parseInt(scanner.nextLine());
                 */

                break;
            case 2: // 목표 기간을 내가 정할 수 있는 경우 e.g. 자기계발을 위한 독서 등 -> 내가 목표로 하는 기간, 나의 현재 능력/상태/속도에 따라 계산된 기간 등 가능
                System.out.print("하루에 몇 페이지씩 공부할 수 있을 것 같나요? > ");
                quantityPerDayPredicted = Integer.parseInt(scanner.nextLine());
//                result.put(false, quantityPerDayPredicted);
//                return result;
        }

        switch (isDeadlineSet) {
            case 1:
                numOfWorkDays = (int) ChronoUnit.DAYS.between(startDate, endDate);
                break;
            case 2:
                numOfWorkDays = countableQuantity / quantityPerDayPredicted;
                break;
        }

        return numOfWorkDays;
    }

    public LocalDateTime dateFormatter(String dateInput) {
        /* 처음 생각했던 방법
        StringTokenizer st = new StringTokenizer(dateInput, "-");
        return LocalDateTime.of(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), 0, 0, 0);
         */

        // Java 정석 책 참고
        LocalDate date = LocalDate.parse(dateInput);
        LocalTime time = LocalTime.of(0, 0, 0); // 임의로 그냥 모든 마감일을 일단 그 날을 시작하는 자정으로 설정

        return LocalDateTime.of(date, time);
    }
}
