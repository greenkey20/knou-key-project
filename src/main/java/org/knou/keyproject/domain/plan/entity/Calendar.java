package org.knou.keyproject.domain.plan.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2023.7.25(화) 23h20 별도 클래스로 분리 -> static 메서드로 만든 것의 의미를 정확하게 이해해야 한다..
public class Calendar {
    // 2023.7.25(화) 12h35 AJAX로 했으나 클라이언트에 [Object, Object]..로 전달됨 -> 21h40 생각해보니 꼭 AJAX로 하지 않아도 되는 것 같아, 접근 방식 변경
//    @ResponseBody
//    @RequestMapping(value = "calendar.pl", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public static List<DateData> getCalendar() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        DateData searchDate = new DateData(String.valueOf(year), String.valueOf(month), String.valueOf(today.getDayOfMonth()), today.getDayOfWeek().getValue(), null);

        Map<String, Integer> todayInfo = searchDate.todayInfo(searchDate); // 21h50 이 메서드 내에서만 필요하고, JSP로 굳이 반환할 필요 없는 것 같은데..?

        List<DateData> dateDataList = new ArrayList<>(); // 이번 달 달력에 찍을 날짜들을 모은 리스트
        DateData individualDay;

        // 월요일부터 해당 월 시작일 요일 전까지 빈칸으로 채움 -> 나는 추후에 지난 달 날짜로 채우고 싶다! // todo
        for (int i = 0; i < todayInfo.get("startDay"); i++) {
            individualDay = new DateData(null, null, null, i, null);
            dateDataList.add(individualDay);
        }

        int dayInt = todayInfo.get("startDay"); // 해당 월 1일의 요일

        for (int i = todayInfo.get("startDate"); i <= todayInfo.get("endDate"); i++) {
            if (dayInt % 7 != 0) {
                dayInt = dayInt % 7;
            } else {
                dayInt = 7;
            }

            if (i == todayInfo.get("todayDate")) {
                individualDay = new DateData(String.valueOf(searchDate.getYear()), String.valueOf(searchDate.getMonth()), String.valueOf(i), dayInt, "today");
            } else {
                individualDay = new DateData(String.valueOf(searchDate.getYear()), String.valueOf(searchDate.getMonth()), String.valueOf(i), dayInt, "normalDay");
            }

            dateDataList.add(individualDay);
            dayInt++;
        }

        // 월 마지막 날 요일부터 일요일까지 빈칸으로 채움 -> 나는 추후에 다음 달 날짜로 채우고 싶다! // todo
        int delim = dateDataList.size() % 7;
        if (delim != 0) {
            for (int i = 0; i < 7 - delim; i++) {
                individualDay = new DateData(null, null, null, null, null);
                dateDataList.add(individualDay);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dateDataList", dateDataList);
        result.put("todayInfo", todayInfo);
//
//        log.info("individual day 첫번째 요소 = " + dateDataList.get(0).toString()); // todo
//        log.info("individual day 11번째 요소 = " + dateDataList.get(11).toString()); // todo
//        log.info("todayInfo에 저장된 startKey 키에 해당하는 value = " + todayInfo.get("startDay")); // todo
//        return result;

//        return new Gson().toJson(dateDataList);
        return dateDataList;
    }
}
