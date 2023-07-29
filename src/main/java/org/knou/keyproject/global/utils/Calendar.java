package org.knou.keyproject.global.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knou.keyproject.domain.actiondate.entity.ActionDate;
import org.knou.keyproject.domain.actiondate.entity.DateType;
import org.knou.keyproject.domain.actiondate.mapper.ActionDateMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2023.7.25(화) 23h20 별도 클래스로 분리 -> static 메서드로 만든 것의 의미를 정확하게 이해해야 한다..
@Slf4j
//@NoArgsConstructor
@Getter
//@AllArgsConstructor
@Builder
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

        ActionDate searchDate = new ActionDate(String.valueOf(year), month, String.valueOf(today.getDayOfMonth()), today.getDayOfWeek().getValue(), DateType.TODAY);
        List<ActionDate> calendarDatesList = getCalendarDatesList(searchDate);

//        log.info("Calendar 클래스에서 actionDateList = " + calendarDatesList);

        //-------------------------------------
        // 2023.7.26(수) 2h10 매개변수로 전달받은 actionDates에 해당하는 날의 dateData 객체 schedule 필드의 값을 'action'으로 세팅 -> JSP에서 이 값으로 CSS 다르게 줄 수 있을 것임
        // 2023.7.30(일) 0h20 나의 생각 = 여기 2중 for문이라 성능 안 좋은데, 어떻게 개선할 수 있을까? todo
        for (ActionDate thisDate : calendarDatesList) {
            String thisFormat = thisDate.getDateFormat();
            log.info("thisFormat = " + thisFormat);

            for (ActionDate ad : actionDates) {
                String actionDateFormat = ad.getDateFormat();
                log.info("actionDateFormat = " + actionDateFormat);

                if (thisFormat.equals(actionDateFormat)) {
                    thisDate.setSchedule("action");
//                    log.info("달력 날짜의 schedule로써 action 찍힙니다");
                } else {
//                    log.info("달력 날짜의 schedule로써 action 안 찍힙니다 = format 비교가 안 되거나, 둘 다 null이거나..");
                }
            }
        }

//        Map<String, Object> result = new HashMap<>();
//        result.put("calendarDatesList", calendarDatesList);
//        result.put("todayInfo", todayInfo);
//
//        log.info("individual day 첫번째 요소 = " + actionDateList.get(0).toString()); // todo
//        log.info("individual day 11번째 요소 = " + actionDateList.get(11).toString()); // todo
//        log.info("todayInfo에 저장된 startKey 키에 해당하는 value = " + todayInfo.get("startDay")); // todo
//        return result;

//        return new Gson().toJson(actionDateList);

        // 2023.7.28(금) 17h50 JSP 달력 출력(td에 대한 tags 지정 필요)을 위한 Java 코드
        /*
        ActionDate sampleDate = calendarDatesList.get(0);
        String tags = "";
        if (sampleDate.getNumOfDay() == 7) { // 일요일 = 개행 필요
            if (sampleDate.getSchedule().equals("action")) {
                if (sampleDate.getDateType().equals("TODAY")) {
                    tags = "action, today";
                } else {
                    tags = "action";
                }
            } else {
                tags = "holiday";
            }
        } else { // 평일
            if (sampleDate.getSchedule().equals("action")) {
                if (sampleDate.getDateType().equals("TODAY")) {
                    tags = "action, today";
                } else {
                    tags = null;
                }
            } else {
                tags = null;
            }
        }
         */

        return calendarDatesList;
    }

    public static List<ActionDate> getCalendarDatesList(ActionDate searchDate) {
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
                individualDay = ActionDate.builder()
                        .numOfYear(String.valueOf(searchDate.getNumOfYear()))
                        .numOfMonth(searchDate.getNumOfMonth())
                        .numOfDate(String.valueOf(i))
                        .numOfDay(dayInt)
                        .dateType(DateType.TODAY)
                        .dateFormat(String.format("%s-%02d-%02d", searchDate.getNumOfYear(), searchDate.getNumOfMonth(), i))
                        .build();
            } else {
                individualDay = ActionDate.builder()
                        .numOfYear(String.valueOf(searchDate.getNumOfYear()))
                        .numOfMonth(searchDate.getNumOfMonth())
                        .numOfDate(String.valueOf(i))
                        .numOfDay(dayInt)
                        .dateType(DateType.NORMALDAY)
                        .dateFormat(String.format("%s-%02d-%02d", searchDate.getNumOfYear(), searchDate.getNumOfMonth(), i))
                        .build();
            }

//            individualDay.setDateFormat(String.format("%s-%02d-%02d", searchDate.getNumOfYear(), searchDate.getNumOfMonth(), Integer.parseInt(searchDate.getNumOfDate())));

            calendarDatesList.add(individualDay);
            dayInt++;
        }

        // 월 마지막 날 요일부터 일요일까지 빈칸으로 채움 -> 나는 추후에 다음 달 날짜로 채우고 싶다! // todo
        int delim = calendarDatesList.size() % 7;
        if (delim != 0) {
            for (int i = 0; i < 7 - delim; i++) {
                individualDay = new ActionDate(null, null, null, i + delim, null);
                calendarDatesList.add(individualDay);
            }
        }

        return calendarDatesList;
    }
}
