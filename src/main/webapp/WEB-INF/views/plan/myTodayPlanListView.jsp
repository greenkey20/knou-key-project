<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오늘의 일정</title>
    <link rel="stylesheet" href="resources/css/plan/myTodayPlanListView.css">
</head>
<body>
<!--2023.8.24(목) 2h 파일 생성 + 작업 시작-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/plan/myTodayPlanListView.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>오늘의 일정</h2>
    <br>

    <div class="calendar" align="center">
        <br>
        <%
            LocalDate today = LocalDate.now();
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            String todayFormat = today.format(formatter);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String todayFormat = sdf.format(today);
        %>
        <h4><%= todayFormat%></h4> <!--오늘 날짜 표시-->
        <c:choose>
            <c:when test="${ not empty actionDatesList }">
                <table class="actionDatesListTable" border="darkgreen" align="center">
                    <colgroup>
                        <col style="width: 10%">
                        <col style="width: 30%">
                        <col style="width: 15%">
                        <col style="width: 25%">
                        <col style="width: 20%">
                    </colgroup>

                    <thead>
                    <tr>
                        <td>No</td>
                        <td>활동 제목</td>
                        <td>수행 여부</td>
                        <td>수행 내용</td>
                        <td>활동 상세 보기</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="day" items="${ actionDatesList }" varStatus="status">
                        <tr>
                            <td class="holiday"> ${ status.count } </td>

                            <td>${ day.object }</td>

                            <c:choose>
                                <c:when test="${ not day.isDone }">
                                    <td class="check">-</td>
                                    <td class="smallerLetters">
                                        <c:choose>
                                            <c:when test="${ day.isMeasurable }">
                                                <c:choose>
                                                    <c:when test="${ day.planStartUnit ne day.planEndUnit }">
                                                        ${ day.planStartUnit } ~ ${ day.planEndUnit } ${ day.unit }
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${ day.planStartUnit } ${ day.unit }
                                                    </c:otherwise>
                                                </c:choose>
                                                <br>
                                                (총 ${ day.planActionQuantity }${ day.unit }) [예정]
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td class="check">✅</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${ day.isMeasurable }">
                                                <c:choose>
                                                    <c:when test="${ day.planStartUnit ne day.planEndUnit }">
                                                        ${ day.realStartUnit } ~ ${ day.realEndUnit } ${ day.unit }
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${ day.realStartUnit } ${ day.unit }
                                                    </c:otherwise>
                                                </c:choose>
                                                <br>
                                                (총 ${ day.realActionQuantity }${ day.unit })
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                </c:otherwise>
                            </c:choose>

                            <td><button type="button" onclick="location.href='myPlanDetail.pl?planId=${ day.planId }'">활동 상세 조회</button></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>오늘 예정된 활동 일정이 없습니다</c:otherwise>
        </c:choose>
        <br>
    </div>

    <div class="calendar" align="center">
        <!--JSP/Java로 달력 만들기-->
        <div class="navigation">
            <a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear - 1 }&month=${ calendarDatesList[10].numOfMonth % 12 }">⬅️</a>
            <c:choose>
                <c:when test="${ calendarDatesList[10].numOfMonth % 12 ne 1}"><a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear }&month=${ (calendarDatesList[10].numOfMonth - 1) % 12 }">←</a></c:when>
                <c:otherwise><a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear - 1 }&month=12">←</a></c:otherwise>
            </c:choose>
            <span class="thisYM"> ${ calendarDatesList[10].numOfYear }. ${ calendarDatesList[10].numOfMonth } </span>
            <c:choose>
                <c:when test="${ calendarDatesList[10].numOfMonth % 12 ne 0}"><a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear }&month=${ (calendarDatesList[10].numOfMonth + 1) % 12 }">→</a></c:when>
                <c:otherwise><a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear + 1}&month=1">→</a></c:otherwise>
            </c:choose>
            <a class="naviYM" href="myTodayPlanList.pl?year=${ calendarDatesList[10].numOfYear + 1}&month=${ calendarDatesList[10].numOfMonth % 12 }">➡️</a>
        </div>

        <table class="calendarBody" border="gray">
            <thead>
            <tr bgcolor="#9acd32">
                <td class="day holiday">일</td>
                <td class="day">월</td>
                <td class="day">화</td>
                <td class="day">수</td>
                <td class="day">목</td>
                <td class="day">금</td>
                <td class="day">토</td>
            </tr>
            </thead>
            <tbody>
                <c:forEach var="date" items="${ calendarDatesList }">
                    <c:choose>
                        <c:when test="${ date.numOfDay % 7 eq 0 }">
                            <c:choose>
                                <c:when test="${ date.schedule eq 'action'}">
                                    <c:choose>
                                        <c:when test="${ date.dateType.toString() eq 'TODAY' }">
                                            </tr><tr><td class="today action" align="left"> ${ date.numOfDate } </td>
                                        </c:when>
                                        <c:otherwise>
                                            </tr><tr><td class="action" align="left"> ${ date.numOfDate } </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${ date.dateType.toString() eq 'TODAY'}">
                                            </tr><tr><td class="holiday today" align="left"> ${ date.numOfDate } </td>
                                        </c:when>
                                        <c:otherwise>
                                            </tr><tr><td class="holiday" align="left"> ${ date.numOfDate } </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${ date.schedule eq 'action'}">
                                    <c:choose>
                                        <c:when test="${ date.dateType.toString() eq 'TODAY' }">
                                            <td class="action today" align="left"> ${ date.numOfDate } </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="action" align="left"> ${ date.numOfDate } </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${ date.dateType.toString() eq 'TODAY' }">
                                            <td class="today" align="left"> ${ date.numOfDate } </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td align="left"> ${ date.numOfDate } </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </tbody>
        </table>
        <br>
        <div align="right">
            <a class="smallerLetters" style="text-decoration: none; color: #53C81E" href="myTodayPlanList.pl?year=<%= today.getYear()%>&month=<%= today.getMonthValue()%>">🌱 오늘 날짜로 가기</a>
        </div>
        <div align="left">
            달력의 날짜를 클릭하여 해당 날짜의 일정을 확인할 수 있습니다
            <div class="actionDatesListTableArea">
                <!--Ajax 조회 결과가 없으면 -->해당 일자에 예정된 활동 일정이 없습니다
                <!--Ajax 조회 결과가 있으면 class="actionDatesListTable" border="darkgreen" align="center" 테이블 띄워보여줌-->
            </div>
        </div>
        <br>
        <br>
    </div>
    <br>
    <!--이번 달 달력 표시 영역 끝-->

    <br>
    <!--plan 상태에 따라 버튼 보여주기-->
    <div align="center">
        <button type="button" onclick="location.href='myPlanList.pl'">나의 활동 목록으로 가기</button>
        <button type="button" onclick="location.href='mainPage.cm'">홈페이지로 가기</button>
    </div>
    <br>
    <br>
</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>