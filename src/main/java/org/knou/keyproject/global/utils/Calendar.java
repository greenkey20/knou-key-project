package org.knou.keyproject.global.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.dto.ActionDateResponseDto;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.entity.DateType;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2023.7.25(화) 23h20 별도 클래스로 분리 -> static 메서드로 만든 것의 의미를 정확하게 이해해야 한다..
@Slf4j
//@RequiredArgsConstructor
public class Calendar {
    public static ActionDateMapper actionDateMapper;
    // 2023.7.25(화) 12h35 AJAX로 했으나 클라이언트에 [Object, Object]..로 전달됨 -> 21h40 생각해보니 꼭 AJAX로 하지 않아도 되는 것 같아, 접근 방식 변경
//    @ResponseBody
//    @RequestMapping(value = "calendar.pl", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public static List<ActionDate> getCalendar(List<ActionDate> actionDates) {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        ActionDate searchDate = new ActionDate(String.valueOf(year), month, String.valueOf(today.getDayOfMonth()), today.getDayOfWeek().getValue(), null);

        Map<String, Integer> todayInfo = searchDate.todayInfo(searchDate); // 21h50 이 메서드 내에서만 필요하고, JSP로 굳이 반환할 필요 없는 것 같은데..?

        List<ActionDate> calendarDatesList = new ArrayList<>(); // 이번 달 달력에 찍을 날짜들을 모은 리스트
        ActionDate individualDay;

        // 월요일부터 해당 월 시작일 요일 전까지 빈칸으로 채움 -> 나는 추후에 지난 달 날짜로 채우고 싶다! // todo
        for (int i = 0; i < todayInfo.get("startDay"); i++) {
            individualDay = new ActionDate(null, null, null, i, null);
            calendarDatesList.add(individualDay);
        }

        int dayInt = todayInfo.get("startDay"); // 해당 월 1일의 요일

        for (int i = todayInfo.get("startDate"); i <= todayInfo.get("endDate"); i++) {
            if (dayInt % 7 != 0) {
                dayInt = dayInt % 7;
            } else {
                dayInt = 7;
            }

            if (i == todayInfo.get("todayDate")) {
                individualDay = new ActionDate(String.valueOf(searchDate.getYear()), searchDate.getMonth(), String.valueOf(i), dayInt, DateType.TODAY);
            } else {
                individualDay = new ActionDate(String.valueOf(searchDate.getYear()), searchDate.getMonth(), String.valueOf(i), dayInt, DateType.NORMALDAY);
            }

            calendarDatesList.add(individualDay);
            dayInt++;
        }

        // 월 마지막 날 요일부터 일요일까지 빈칸으로 채움 -> 나는 추후에 다음 달 날짜로 채우고 싶다! // todo
        int delim = calendarDatesList.size() % 7;
        if (delim != 0) {
            for (int i = 0; i < 7 - delim; i++) {
                individualDay = new ActionDate(null, null, null, null, null);
                calendarDatesList.add(individualDay);
            }
        }

        log.info("Calendar 클래스에서 actionDateList = " + calendarDatesList);

        //-------------------------------------
        // 2023.7.26(수) 2h10 매개변수로 전달받은 actionDates에 해당하는 날의 dateData 객체 schedule 필드의 값을 'action'으로 세팅 -> JSP에서 이 값으로 CSS 다르게 줄 수 있을 것임
        for (int i = 0; i < calendarDatesList.size(); i++) {
            ActionDate thisDate = calendarDatesList.get(i);
            String thisFormat = thisDate.getDateFormat();

            for (int j = 0; j < actionDates.size(); j++) {
                String actionDateFormat = actionDates.get(j).getDateFormat();

                if (thisFormat.equals(actionDateFormat)) {
                    thisDate.setSchedule("action");
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("calendarDatesList", calendarDatesList);
        result.put("todayInfo", todayInfo);
//
//        log.info("individual day 첫번째 요소 = " + actionDateList.get(0).toString()); // todo
//        log.info("individual day 11번째 요소 = " + actionDateList.get(11).toString()); // todo
//        log.info("todayInfo에 저장된 startKey 키에 해당하는 value = " + todayInfo.get("startDay")); // todo
//        return result;

//        return new Gson().toJson(actionDateList);
        return calendarDatesList;
    }
}
