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
@Entity
public class ActionDate extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actionDateId;

    private String year;
    private Integer month;
    private String date; // 날짜
    private Integer day; // 요일 -> 요일도 enum(original 타입)으로 해 두면 더 관리가 수월할까?

    @Enumerated(EnumType.STRING)
    @Column
    private DateType dateType; // 달력 생성 시 today, normal day 등 ou actionDatesList 생성 시 action 등

    private String schedule; // 달력 생성 시 action

    // 2023.7.26(수) 2h 이런저런 생각하다가 추가해봄 -> 3h15 수정
    private String dateFormat = String.format("%s-%02d-%s", year, month, date);
    //    public void setDataFormat(String year, String month, String date) {
//        if (Integer.parseInt(month) < 10) {
//            month = "0" + month;
//        }
//
//        this.dataFormat = year + "-" + month + "-" + date;
//    }

    // 2023.7.26(수) 14h25 추가
    private String actionDetail; // 메모

    private Integer planActionQuantity/* = this.plan.getQuantityPerDay()*/; // 계획 수행 분량

    public void setPlanActionQuantity(Integer planActionQuantity) {
        this.planActionQuantity = planActionQuantity;
    }

    private Boolean isDone; // 수행 여부 <- JSP 체크박스
    Integer realActionQuantity; // 실제 수행 분량

    // 2023.7.26(수) 0h
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_ID")
    Plan plan;

    public ActionDate(String year, Integer month, String date, Integer day, DateType dateType) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
        this.dateType = dateType;
    }

    // 2023.7.26(수) 15h55 추가 = actionDatesList 계산할 때 해당일 각각에 수행해야 하는 분량도 배정하기 위해
    public ActionDate(String year, Integer month, String date, Integer day, DateType dateType, Integer planActionQuantity, Boolean isDone) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
        this.dateType = dateType;
        this.planActionQuantity = planActionQuantity;
        this.isDone = false;
    }

    public Map<String, Integer> todayInfo(ActionDate actionDate) {
        Map<String, Integer> todayDataMap = new HashMap<>();

        LocalDate searchDate = LocalDate.of(Integer.parseInt(actionDate.getYear()), actionDate.getMonth(), 1);

        int startDate = searchDate.with(firstDayOfMonth()).getDayOfMonth();
        int endDate = searchDate.with(lastDayOfMonth()).getDayOfMonth();
        int startDay = searchDate.with(firstDayOfMonth()).getDayOfWeek().getValue(); // 해당 월 첫번째 날의 요일의 정수 값(월1~일7)

        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();

        int searchYear = Integer.parseInt(actionDate.getYear());
        int searchMonth = actionDate.getMonth();

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
