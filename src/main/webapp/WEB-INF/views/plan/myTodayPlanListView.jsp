<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <c:choose>
            <c:when test="${ empty calendars }">오늘의 일정이 없습니다</c:when>
            <c:otherwise>
                <!--JSP/Java로 달력 만들기-->
                <div class="navigation">
                    <a class="naviYM" href="calendar.pl?year=${ calendarDatesList[10].year - 1 }&month=${ calendarDatesList[10].month % 12 }">⬅️</a>
                    <a class="naviYM" href="calendar.pl?year=${ calendarDatesList[10].year }&month=${ (calendarDatesList[10].month - 1) % 12 }">←</a>
                    <span class="thisYM"> ${ calendarDatesList[10].year }. ${ calendarDatesList[10].month } </span>
                    <a class="naviYM" href="calendar.pl?year=${ calendarDatesList[10].year }&month=${ (calendarDatesList[10].month + 1) % 12 }">→</a>
                    <a class="naviYM" href="calendar.pl?year=${ calendarDatesList[10].year + 1}&month=${ calendarDatesList[10].month % 12 }">➡️</a>
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
                    <!--ajax 통신 결과 result에서 만든 태그들 붙여넣는 곳 vs 2023.7.25(화) 21h55 ajax로 안 하기로 함(할 필요 없음)-->
                    <c:forEach var="date" items="${ calendarDatesList }">
                        <c:choose>
                            <c:when test="${ date.day % 7 eq 0 }">
                                <tr><td class="holiday today" align="left"> ${ date.date } </td>
                            </c:when>
                            <c:when test="${ date.day % 7 == 6}">
                                <td align="left"> ${ date.date } </td></tr>
                            </c:when>
                            <c:otherwise>
                                <td align="left"> ${ date.date } </td>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    </tbody>
                </table>

                <br>
                <br>
                <br>
            </c:otherwise>
        </c:choose>


        <c:forEach var="calendarDatesList" items="${ calendars }" varStatus="status">
            <!--JSP/Java로 달력 만들기-->
            <div class="navigation"> <!--xxxx. x 형식의 제목 만들어야 함-->
                <c:set var="currentMonth" value="${ plan.startMonth + status.index }" />
                <c:choose>
                    <c:when test="${ currentMonth gt 12 }">
                        <c:choose>
                            <c:when test="${ currentMonth % 12 != 0}">
                                ${ plan.startYear + (currentMonth / 12).intValue() }. ${ currentMonth - 12 * (currentMonth / 12).intValue() }
                            </c:when>
                            <c:otherwise>
                                ${ plan.startYear + (currentMonth / 12).intValue() - 1}. 12
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        ${ plan.startYear }. ${ currentMonth }
                    </c:otherwise>
                </c:choose>
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
                <c:when test="${ date.dateType.toString() eq 'DONE' }">
                </tr><tr><td class="action done" align="left" bgcolor="#228b22"> ${ date.numOfDate } </td>
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
                    <c:when test="${ date.dateType.toString() eq 'DONE' }">
                    <td class="action done" align="left" bgcolor="#228b22"> ${ date.numOfDate } </td>
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
        </c:forEach>
    </div>
    <br>
    <!--총 활동기간의 달력 표시 영역 끝-->

    <div class="calendar" align="center">
        <br>
        <h4> 활동 일정 목록 </h4>
        <%--        * 수행 여부를 체크하면 기본적으로 수행 예정 분량이 실제 수행 분량으로 기록됩니다<br>--%>
        <%--        * 상세 기록 버튼을 클릭해서 수행 소요 시간과 메모를 기억해 보세요~--%>
        <table class="actionDatesListTable" border="darkgreen" align="center">
            <colgroup>
                <col style="width: 10%">
                <col style="width: 30%">
                <col style="width: 10%">
                <col style="width: 15%">
                <col style="width: 20%">
                <col style="width: 15%">
            </colgroup>

            <thead>
            <tr>
                <td>No</td>
                <td>날짜</td>
                <td>수행 여부</td>
                <td>수행 내용</td>
                <td>활동 점수</td>
                <td>기록</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="day" items="${ actionDatesList }" varStatus="status">
                <tr>
                    <td class="holiday"> ${ status.count } </td>

                    <td>
                        <c:choose>
                            <c:when test="${ day.isDone }">
                                <fmt:parseDate value="${ day.realActionDate }" pattern="yyyy-MM-dd" var="parsedDate" type="date"/>
                                <fmt:formatDate value="${ parsedDate }" type="date" pattern="yyyy.MM.dd" var="printDate"/>
                                ${ printDate }
                            </c:when>
                            <c:otherwise>
                                ${ day.numOfYear }. ${ day.numOfMonth }. ${ day.numOfDate }
                                <c:choose>
                                    <c:when test="${ day.numOfDay == 1 }"> (월) </c:when>
                                    <c:when test="${ day.numOfDay == 2 }"> (화) </c:when>
                                    <c:when test="${ day.numOfDay == 3 }"> (수) </c:when>
                                    <c:when test="${ day.numOfDay == 4 }"> (목) </c:when>
                                    <c:when test="${ day.numOfDay == 5 }"> (금) </c:when>
                                    <c:when test="${ day.numOfDay == 6 }"> (토) </c:when>
                                    <c:otherwise> (일) </c:otherwise>
                                </c:choose>

                                <span class="smallerLetters">[예정]</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <c:choose>
                        <c:when test="${ not day.isDone }">
                            <td class="check">-</td>
                            <td class="smallerLetters">
                                <c:choose>
                                    <c:when test="${ plan.isMeasurable }">
                                        <c:choose>
                                            <c:when test="${ day.planStartUnit ne day.planEndUnit }">
                                                ${ day.planStartUnit } ~ ${ day.planEndUnit } ${ savedPlan.unit }
                                            </c:when>
                                            <c:otherwise>
                                                ${ day.planStartUnit } ${ savedPlan.unit }
                                            </c:otherwise>
                                        </c:choose>
                                        <br>
                                        (총 ${ day.planActionQuantity }${ plan.unit })
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>-</td>
                            <td>
                                <c:choose>
                                    <c:when test="${ day.dateType.toString() eq 'PAUSE' or day.dateType.toString() eq 'GIVEUP' }">
                                        <button type="button" disabled="disabled">상세 기록</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" onclick="location.href='actionDetailRecordPage.ad?planId=${ plan.planId }&actionDateId=${ day.actionDateId }'">상세 기록</button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td class="check">✅</td>
                            <td>
                                <c:choose>
                                    <c:when test="${ plan.isMeasurable }">
                                        <c:choose>
                                            <c:when test="${ day.planStartUnit ne day.planEndUnit }">
                                                ${ day.realStartUnit } ~ ${ day.realEndUnit } ${ savedPlan.unit }
                                            </c:when>
                                            <c:otherwise>
                                                ${ day.realStartUnit } ${ savedPlan.unit }
                                            </c:otherwise>
                                        </c:choose>
                                        <br>
                                        (총 ${ day.realActionQuantity }${ plan.unit })
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>

                            <c:choose>
                                <c:when test="${ not empty day.reviewScore }">
                                    <c:choose>
                                        <c:when test="${ day.reviewScore eq 5 }">
                                            <td>⭐⭐⭐️</td>
                                        </c:when>
                                        <c:when test="${ day.reviewScore gt 2 }"> <!--3 또는 4점을 의미-->
                                            <td>⭐⭐</td>
                                        </c:when>
                                        <c:otherwise> <!--1 또는 2점을 의미-->
                                            <td>⭐️</td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <td>-</td>
                                </c:otherwise>
                            </c:choose>

                            <td><button type="button" onclick="location.href='actionDetailView.ad?planId=${ plan.planId }&actionDateId=${ day.actionDateId }'">상세 기록</button></td> <!--소요 시간 및 메모 기록하려면, 아래 버튼 눌러서 '1일 활동 내역 기록' 화면으로 가야 함-->
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <br>
    </div>
    <br>

    <!--2023.8.21(월) 15h25-->
    <!--처음 계획 계산 시 도서 검색한 경우, 목차 정보 보여줌-->
    <c:if test="${ not empty plan.isbn13 }">
        <div class="checkTableOfContents">
            * 도서 목차 [<span id="toc-toggle" onclick="openCloseToc()">보기</span>] (학습/독서 완료한 챕터는 체크해 보세요)
            <div id="toc-content" align="left">
                <table border="darkgreen" align="center">
                    <thead>
                    <tr>
                        <td>목차</td>
                        <td>체크</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="line" items="${ tableOfContents }">
                        <c:if test="${ not empty line }">
                            <tr>
                                <td>${ line.bookChapterString }</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ line.isDone eq true }">
                                            <input class="chapter-check" type="checkbox" name="isDone" checked onclick="check(this);" value="${ line.bookChapterId }">
                                        </c:when>
                                        <c:otherwise>
                                            <input class="chapter-check" type="checkbox" name="isDone" onclick="check(this);" value="${ line.bookChapterId }">
                                        </c:otherwise>
                                    </c:choose>
                                    <input hidden name="bookChapterId" value="${ line.bookChapterId }">
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
    <br>
    <br>

    <!--2023.8.23(수) 17h30-->
    <!--측정 어려운 일의 경우, chatGpt 답변을 line by line + checkbox 보여줌-->
    <c:if test="${ !plan.isMeasurable }">
        <div class="checkChatGptResponseLines">
            * ChatGpt가 답변해준 활동 목록 예시 [<span id="chatgpt-toggle" onclick="openCloseChatGptLines()">보기</span>] (수행 완료한 활동이 있다면 체크해 보세요)
            <div id="chatgpt-content" align="left">
                <table border="darkgreen" align="center">
                    <thead>
                    <tr>
                        <td>활동</td>
                        <td>체크</td>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="line" items="${ chatGptResponseLines }">
                        <c:if test="${ not empty line }">
                            <tr>
                                <td>${ line.chatGptResponseLineString }</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ line.isDone eq true }">
                                            <input class="chatgpt-check" type="checkbox" name="isDone" checked onclick="checkChatGpt(this);" value="${ line.chatGptResponseLineId }">
                                        </c:when>
                                        <c:otherwise>
                                            <input class="chatgpt-check" type="checkbox" name="isDone" onclick="checkChatGpt(this);" value="${ line.chatGptResponseLineId }">
                                        </c:otherwise>
                                    </c:choose>
                                    <input hidden name="chatGptResponseLineId" value="${ line.chatGptResponseLineId }">
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>
    <br>
    <br>

    <script>
        // 토글 관련 함수들은 js 파일에 있음
        function check(box) {
            if (box.checked == true) {
                console.log("체크박스 체크했음");
                let $bookChId = box.value;
                console.log("선택된 체크박스의 chId = " + $bookChId);

                $.ajax({
                    url: "bookChapterIsDone.bc",
                    // dataType: 'json',
                    type: 'POST',
                    data: {
                        bookChapterId: $bookChId
                    },
                    success: function (result) {
                        console.log("bookChIsDone Ajax 통신 성공!");
                        console.log(result);

                    },
                    error: function () {
                        console.log("bookChIsDone Ajax 통신 실패")
                    }
                });
            } else {
                console.log("체크박스 해제했음")
            }
        }

        function checkChatGpt(box) {
            if (box.checked == true) {
                console.log("체크박스 체크했음");
                let $chatGptResponseLineId = box.value;
                console.log("선택된 체크박스의 chatGptResponseLineId = " + $chatGptResponseLineId)

                $.ajax({
                    url: "chatGptResponseLineIsDone.cg",
                    // dataType: 'json',
                    type: 'POST',
                    data: {
                        chatGptResponseLineId: $chatGptResponseLineId
                    },
                    success: function (result) {
                        console.log("chatGptResponseLineIsDone Ajax 통신 성공!");
                        console.log(result);

                    },
                    error: function () {
                        console.log("chatGptResponseLineIsDone Ajax 통신 실패")
                    }
                });
            } else {
                console.log("체크박스 해제했음")
            }
        }
    </script>

    <!--plan 상태에 따라 버튼 보여주기-->
    <div align="center">
        <c:choose>
            <c:when test="${ plan.status.toString() eq 'ACTIVE'}">
                <c:choose>
                    <c:when test="${ plan.sizeOfModifiedPlansList eq 0 }"> <!--아직 일시 정지한 적 없는 경우-->
                        <!--뒤로 가기 + 게시판에 공유하기 + 일시 중지하기 + 포기하기-->
                        <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
                        <button type="button" class="greenBtn" onclick="location.href='boardEnrollForm.bd?planId=${ plan.planId }&planStatus=${ plan.status.toString() }'">게시판에 공유하기</button> <!--게시판에 글 쓰는(post) 양식으로 이동-->
                        <button type="button" class="grayBtn" data-toggle="modal" data-target="#pauseForm">일시 중지하기</button>
                        <button type="button" data-toggle="modal" data-target="#giveUpForm">포기하기</button>
                    </c:when>
                    <c:otherwise>
                        <!--뒤로/목록으로 가기 버튼만-->
                        <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${ plan.status.toString() eq 'COMPLETE'}">
                <!--뒤로 가기 + 게시판에 공유하기-->
                <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
                <button type="button" class="greenBtn" onclick="location.href='boardEnrollForm.bd?planId=${ plan.planId }&planStatus=${ plan.status.toString() }'">게시판에 공유하기</button> <!--게시판에 글 쓰는(post) 양식으로 이동-->
            </c:when>
            <c:when test="${ plan.status.toString() eq 'PAUSE'}">
                <c:choose>
                    <c:when test="${ plan.sizeOfModifiedPlansList eq 0 }"> <!--아직 일시 정지한 적 없는 경우-->
                        <!--뒤로 가기 + 이어서 하기 + 포기하기-->
                        <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
                        <button type="button" class="greenBtn" onclick="location.href='resumePlan.pl?planId=${ plan.planId }'">이어서 하기</button>
                        <button type="button" data-toggle="modal" data-target="#giveUpForm">포기하기</button>
                    </c:when>
                    <c:otherwise>
                        <!--뒤로/목록으로 가기 버튼만-->
                        <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:otherwise> <!--'포기' 상태인 경우-->
                <button type="button" onclick="location.href='myPlanList.pl'">목록으로 가기</button>
            </c:otherwise>
        </c:choose>
    </div>
    <br>
    <br>
</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<!-- The Modal : pauseForm -->
<div class="modal pause-form fade" id="pauseForm">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">활동 일시 중지하기</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <ul id="pauseList"></ul>
                <span>일시 중지하면 이어서 하기로 결정할 때까지 활동 내역을 기록할 수 없습니다. 또한 이어서 하실 때는 잔여일/잔여량에 따라 마감일이나 매 회 활동 분량이 재조정됩니다. 그래도 일시 중지하시겠습니까?</span>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="grayBtn" data-dismiss="modal">취소</button>
                <button type="button" id="pauseBtn" onclick="location.href='pausePlan.pl?planId=${ plan.planId }'">일시 중지하기</button>
            </div>

        </div>
    </div>
</div>

<!-- The Modal : giveUpForm -->
<div class="modal give-up-form fade" id="giveUpForm">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">활동 포기하기</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <ul id=giveUpList"></ul>
                <span>포기하면 더 이상 활동 내역 기록이 불가능합니다. 언젠가 다시 수행하려면 '일시 중지'가 가능해요. 그래도 포기하시겠습니까?</span>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="grayBtn" data-dismiss="modal">취소</button>
                <button type="button" id="giveUpBtn" onclick="location.href='giveUpPlan.pl?planId=${ plan.planId }'">포기하기</button>
            </div>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>