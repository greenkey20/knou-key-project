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
        <div class="actionDatesListTableArea">
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
        </div>
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
            <span class="thisYM"> <span id="this-year">${ calendarDatesList[10].numOfYear }</span>. <span id="this-month">${ calendarDatesList[10].numOfMonth }</span></span>
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
                                            </tr><tr><td class="today action" align="left"><span class="clickActionDate" onclick="searchThisDayPlanList(${ date.numOfDate })">${ date.numOfDate }</span></td>
                                        </c:when>
                                        <c:otherwise>
                                            </tr><tr><td class="action" align="left"><span class="clickActionDate" onclick="searchThisDayPlanList(${ date.numOfDate })">${ date.numOfDate }</span></td>
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
                                            <td class="action today" align="left"><span class="clickActionDate" onclick="searchThisDayPlanList(${ date.numOfDate })">${ date.numOfDate }</span></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="action" align="left"><span class="clickActionDate" onclick="searchThisDayPlanList(${ date.numOfDate })">${ date.numOfDate }</span></td>
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
        <br>
        <br>
        <div align="left">
            <span class="action">일정이 있는 날짜</span>를 클릭하시면 상세 일정을 확인할 수 있습니다
            <div>
                <div id="this-date-area" class="action" style="background-color: darkgreen" align="center">

                </div>
                <!--Ajax 조회 결과가 없으면 해당 일자에 예정된 활동 일정이 없습니다-->
                <!--Ajax 조회 결과가 있으면 class="actionDatesListTable" border="darkgreen" align="center" 테이블 띄워보여줌-->
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
                    <tbody id="this-day-action-list">

                    </tbody>
                </table>
            </div>
        </div>
        <br>
        <br>
    </div>
    <br>
    <!--이번 달 달력 표시 영역 끝-->

    <script>
        // 2023.8.24(목) 18h45
        function searchThisDayPlanList(numOfDate) {
            console.log("searchThisDayPlanList() 함수 안에 들어왔어요~");

            let $numOfYear = $("#this-year").text();
            let $numOfMonth = $("#this-month").text();
            let $numOfDate = numOfDate;
            console.log($numOfYear + "년 " + $numOfMonth + "월 " + $numOfDate + "일 actionDatesList를 구하고자 함");

            $.ajax({
                url: "thisDayPlanList.pl",
                dataType: 'json',
                data: {
                    year: $numOfYear,
                    month: $numOfMonth,
                    date: $numOfDate
                },
                success: function (result) {
                    console.log(result);

                    let divContent = $numOfYear + "년 " + $numOfMonth + "월 " + $numOfDate + "일자 일정";
                    $("#this-date-area").text(divContent);

                    let tbody = "";

                    if (!result.length) { // 검색 결과가 없는 경우
                        console.log("해당 날짜에 일정 없네요");
                        tbody = "<tr><td colspan='5'>해당 일자에 예정된 활동 일정이 없습니다</td></tr>";
                    } else {
                        console.log("해당 날짜에 일정 있습니다"); // 2023.8.24(목) 21h5 확인



                        for (var i = 0; i < result.length; i++) {
                            tbody += "<tr>"
                                + "<td>" + (i + 1) + "</td>"
                                + "<td>" + result[i].object + "</td>";

                            if (!result[i].isDone) {
                                tbody += "<td>-</td>"
                                    + "<td class='smallerLetters'>";

                                if (result[i].isMeasurable) {
                                    if (result[i].planStartUnit != result[i].planEndUnit) {
                                        tbody += result[i].planStartUnit + " ~ " + result[i].planEndUnit + result[i].unit;
                                    } else {
                                        tbody += result[i].planStartUnit + result[i].unit;
                                    }

                                    tbody += "<br>(총 " + result[i].planActionQuantity + result[i].unit + ") [예정]"
                                } else {
                                    tbody += "-";
                                }

                                tbody += "</td>";
                            } else {
                                tbody += "<td>✅</td>"
                                    + "<td>";

                                if (result[i].isMeasurable) {
                                    if (result[i].planStartUnit != result[i].planEndUnit) {
                                        tbody += result[i].realStartUnit + " ~ " + result[i].realEndUnit + result[i].unit;
                                    } else {
                                        tbody += result[i].realStartUnit + result[i].unit;
                                    }

                                    tbody += "<br>(총 " + result[i].realActionQuantity + result[i].unit + ")"
                                } else {
                                    tbody += "-";
                                }

                                tbody += "</td>";
                            }

                            tbody += "<td><button type='button' onclick='location.href='myPlanDetail.pl?planId=" + result[i].planId + "''>활동 상세 조회</button></td>"
                                + "</tr>";
                        } // for문 영역 끝
                    } // if-else문 끝

                    $("#this-day-action-list").html(tbody);
                }, // success 정의 영역 끝
                error: function () {
                    console.log("특정 날짜 일정 가져오는 AJAX 실패");
                },
            });
        }
    </script>

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