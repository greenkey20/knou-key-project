package org.knou.keyproject.domain.plan.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

// 2023.7.25(화) 14h
//@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateData {
    String year = "";
    String month = "";
    String date = ""; // 날짜
    Integer day = -1; // 요일
    String value = "";
    String schedule = "";
    String scheduleDetail = "";

    public DateData(String year, String month, String date, Integer day, String value) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.day = day;
        this.value = value;
    }

    public Map<String, Integer> todayInfo(DateData dateData) {
        Map<String, Integer> todayDataMap = new HashMap<>();

        LocalDate searchDate = LocalDate.of(Integer.parseInt(dateData.getYear()), Integer.parseInt(dateData.getMonth()), 1);

        int startDate = searchDate.with(firstDayOfMonth()).getDayOfMonth();
        int endDate = searchDate.with(lastDayOfMonth()).getDayOfMonth();
        int startDay = searchDate.with(firstDayOfMonth()).getDayOfWeek().getValue(); // 해당 월 첫번째 날의 요일의 정수 값(월1~일7)

        LocalDate today = LocalDate.now();
        int todayYear = today.getYear();
        int todayMonth = today.getMonthValue();

        int searchYear = Integer.parseInt(dateData.getYear());
        int searchMonth = Integer.parseInt(dateData.getMonth());

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

    @Override
    public String toString() {
        return "DateData{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", date='" + date + '\'' +
                ", day=" + day +
                ", value='" + value + '\'' +
                ", schedule='" + schedule + '\'' +
                ", scheduleDetail='" + scheduleDetail + '\'' +
                '}';
    }
}
