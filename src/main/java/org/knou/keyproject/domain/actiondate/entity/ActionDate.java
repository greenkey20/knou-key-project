package org.knou.keyproject.domain.actiondate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.knou.keyproject.domain.plan.entity.Plan;
import org.knou.keyproject.global.audit.BaseTimeEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

// 2023.7.25(화) 14h -> 2023.7.26(수) 14h20 내 프로그램 내에서의 의미에 맞게 이름 변경
//@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ActionDate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionDateId;

    // 2023.7.28(금) 17h20 h2 db로 세팅해서 테스트해보다가 Syntax error in SQL statement expected "identifier" 에러 발생해서 멤버변수명 변경
    private String numOfYear;
    private Integer numOfMonth;
    private String numOfDate; // 날짜
    private Integer numOfDay; // 요일 -> 요일도 enum(original 타입)으로 해 두면 더 관리가 수월할까?

    @Enumerated(EnumType.STRING)
    @Column
    private DateType dateType; // 달력 생성 시 today, normal day 등 + actionDatesList 생성 시 action + pause plan 시 pause, giveUp 시 giveup

    private String schedule; // 달력 생성 시 action

    // 2023.7.26(수) 2h 이런저런 생각하다가 추가해봄 -> 3h15 수정
    private String dateFormat = String.format("%s-%02d-%s", numOfYear, numOfMonth, numOfDate);
    //    public void setDataFormat(String year, String month, String date) {
//        if (Integer.parseInt(month) < 10) {
//            month = "0" + month;
//        }
//
//        this.dataFormat = year + "-" + month + "-" + date;
//    }

    // 2023.7.26(수) 14h25 추가
    private String memo; // 메모

    private Integer planActionQuantity/* = this.plan.getQuantityPerDay()*/; // 계획 수행 분량

//    public void setPlanActionQuantity(Integer planActionQuantity) {
//        this.planActionQuantity = planActionQuantity;
//    }

    private Boolean isDone; // 수행 여부 <- JSP 체크박스
    private Integer realActionQuantity; // 실제 수행 분량
    private Integer timeTakenForRealAction;
    private Integer reviewScore;
    private LocalDate realActionDate; // 실제 수행일, 사용자가 기록한 날짜

    // 2023.8.20(일) 8h40 수행일별 수행 내용 추가/표시를 위해 추가할 것
//    private Integer order; // actionDateId 활용해서 유도 가능?
//    private Integer startUnit;
//    private Integer endUnit;

    // 2023.7.26(수) 0h
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID")
    Plan plan;

    // 생성자 -> 2023.7.29(토) 3h45 필요 없는 것들은 주석 처리
    public ActionDate(String numOfYear, Integer numOfMonth, String numOfDate, Integer numOfDay, DateType dateType) {
        this.numOfYear = numOfYear;
        this.numOfMonth = numOfMonth;
        this.numOfDate = numOfDate;
        this.numOfDay = numOfDay;
        this.dateType = dateType;
    }

    // 2023.7.26(수) 15h55 추가 = actionDatesList 계산할 때 해당일 각각에 수행해야 하는 분량도 배정하기 위해
//    public ActionDate(String numOfYear, Integer numOfMonth, String numOfDate, Integer numOfDay, DateType dateType, Integer planActionQuantity, Boolean isDone) {
//        this.numOfYear = numOfYear;
//        this.numOfMonth = numOfMonth;
//        this.numOfDate = numOfDate;
//        this.numOfDay = numOfDay;
//        this.dateType = dateType;
//        this.planActionQuantity = planActionQuantity;
//        this.isDone = false;
//    }

    // 2023.7.29(토) 0h45 추가 = 그런데 이렇게 세팅하는 게 맞는 건가?
//    public ActionDate(Long planId, String numOfYear, Integer numOfMonth, String numOfDate, Integer numOfDay, DateType dateType, String dateFormat, Integer planActionQuantity, Boolean isDone) {
//        this.plan.setPlanId(planId);
//        this.numOfYear = numOfYear;
//        this.numOfMonth = numOfMonth;
//        this.numOfDate = numOfDate;
//        this.numOfDay = numOfDay;
//        this.dateType = dateType;
//        this.dateFormat = dateFormat;
//        this.planActionQuantity = planActionQuantity;
//        this.isDone = false;
//    }

    public Map<String, Integer> todayInfo(ActionDate actionDate) {
        Map<String, Integer> todayDataMap = new HashMap<>();

        LocalDate searchDate = LocalDate.of(Integer.parseInt(actionDate.getNumOfYear()), actionDate.getNumOfMonth(), 1);

        int startDate = searchDate.with(firstDayOfMonth()).getDayOfMonth();
        int endDate = searchDate.with(lastDayOfMonth()).getDayOfMonth();
        int startDay = searchDate.with(firstDayOfMonth()).getDayOfWeek().getValue(); // 해당 월 첫번째 날의 요일의 정수 값(월1~일7)

        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();

        int searchYear = Integer.parseInt(actionDate.getNumOfYear());
        int searchMonth = actionDate.getNumOfMonth();

        int todayDate = 0;
        if (todayYear == searchYear && todayMonth == searchMonth) {
            todayDate = today.getDayOfMonth();
        }

        Map<String, Integer> prevNextCalendarMap = prevNextCalendar(searchYear, searchMonth);

        todayDataMap.put("startDay", startDay); // 해당 월 시작(1일) 요일
        todayDataMap.put("startDate", startDate); // 해당 월 시작 일자 = 1
        todayDataMap.put("endDate", endDate); // 해당 월 마지막 날짜
        todayDataMap.put("todayDate", todayDate); // 오늘 날짜
        todayDataMap.put("searchYear", searchYear);
        todayDataMap.put("searchMonth", searchMonth);
        todayDataMap.put("prevYear", prevNextCalendarMap.get("prevYear"));
        todayDataMap.put("prevMonth", prevNextCalendarMap.get("prevMonth"));
        todayDataMap.put("nextYear", prevNextCalendarMap.get("nextYear"));
        todayDataMap.put("nextMonth", prevNextCalendarMap.get("nextMonth"));

        return todayDataMap;
    }

    private Map<String, Integer> prevNextCalendar(int searchYear, int searchMonth) {
        Map<String, Integer> prevNextCalendarMap = new HashMap<>();
        int prevYear = searchYear - 1;
        int prevMonth = searchMonth - 1;
        int nextYear = searchYear + 1;
        int nextMonth = searchMonth + 1;

        if (prevMonth < 1) {
            prevMonth = 12;
        }

        if (nextMonth > 12) {
            nextMonth = 1;
        }

        prevNextCalendarMap.put("prevYear", prevYear);
        prevNextCalendarMap.put("prevMonth", prevMonth);
        prevNextCalendarMap.put("nextYear", nextYear);
        prevNextCalendarMap.put("nextMonth", nextMonth);

        return prevNextCalendarMap;
    }

//    @Override
//    public String toString() {
//        return "ActionDate{" +
//                "actionDateId=" + actionDateId +
//                ", year='" + year + '\'' +
//                ", month=" + month +
//                ", date='" + date + '\'' +
//                ", day=" + day +
//                ", dateType=" + dateType +
//                ", schedule='" + schedule + '\'' +
//                ", dataFormat='" + dataFormat + '\'' +
//                ", actionDetail='" + actionDetail + '\'' +
//                ", planActionQuantity=" + planActionQuantity +
//                ", isDone=" + isDone +
//                ", realActionQuantity=" + realActionQuantity +
//                ", plan=" + plan +
//                '}';
//    }
}
