<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>일정 상세 보기</title>
    <link rel="stylesheet" href="resources/css/plan/myPlanDetailView.css">
</head>
<body>
<!--2023.7.28(금) 5h15 파일 생성 + 작업 시작-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<%--<script src="resources/js/plan/myPlanDetailView.js"></script>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>일정 상세 보기</h2>
    <br>
    <div class="object">
        <h3>${ plan.object }</h3>
    </div>
    <br>

    <div id="my-plan-detail-stats">
        <br>
        <p style="font-weight: bold; font-size: larger">
            <c:if test="${ plan.status.toString() eq 'ACTIVE' and plan.isChild }">
                <div class="smallerLetters" style="font-weight: bold; background-color: darkgreen; color: lightgreen">[일시 중지 후 이어서 하고 있어요]</div>
            </c:if>

            ${ plan.startDate } ~ ${ plan.deadlineDate } (${ plan.totalDurationDays }일) 기간 중<br>
            ${ plan.frequencyDetail } 총 ${ plan.totalNumOfActions }회 동안<br>
            매 회 ${ plan.quantityPerDay }${ plan.unit }만큼~
        </p>
        <br>

        <c:choose>
            <c:when test="${ plan.status.toString() eq 'ACTIVE'}">
                - 오늘까지 진행 분량 ${ statPlan.accumulatedRealActionQuantity}${ plan.unit } / 오늘까지 계획했었던
                분량 ${ statPlan.accumulatedPlanActionQuantity }${ plan.unit }
                <c:choose>
                    <c:when test="${ statPlan.quantityDifferenceBetweenPlanAndReal lt 0 }">
                        → 계획보다 ${ statPlan.quantityDifferenceBetweenPlanAndReal * (-1) }${ plan.unit }만큼 앞서 있어요 👍<br>
                    </c:when>
                    <c:when test="${ statPlan.quantityDifferenceBetweenPlanAndReal gt 0 }">
                        → 계획보다 ${ statPlan.quantityDifferenceBetweenPlanAndReal }${ plan.unit }만큼 뒤처져 있어요 🌱<br>
                    </c:when>
                    <c:otherwise>
                        → 계획대로 잘 진행하고 있어요 💯<br>
                    </c:otherwise>
                </c:choose>

                - 목표 달성까지는 ${ statPlan.quantityToEndPlan }${ plan.unit } (${ statPlan.ratioOfQuantityToEndPlan }%) 남았어요!<br>
                <br>
                - 오늘까지 ${ statPlan.accumulatedNumOfActions} 회 수행했고, ${ statPlan.numOfActionsToEndPlan }회 남았습니다. 파이팅입니다 🍀<br>
                - 매 회 활동에 평균적으로 ${ statPlan.averageTimeTakenForRealAction }분이 소요되고 있어요
            </c:when>
            <c:when test="${ plan.status.toString() eq 'COMPLETE'}">
                - ${ plan.lastStatusChangedAt }자 완료했어요! 🎉
                <br>
                <br>
                - 매 회 활동에 평균적으로 ${ statPlan.averageTimeTakenForRealAction }분이 소요되었어요
            </c:when>
            <c:when test="${ plan.status.toString() eq 'PAUSE'}">
                - ${ plan.lastStatusChangedAt }자 일시 중지한 상태에요<br>
                - ${ statPlan.periodDaysBeforePause }일 동안 ${ statPlan.accumulatedRealActionQuantity }${ plan.unit } 진행하고 있었어요
                <span class="smallerLetters">(계획했던 분량: ${ statPlan.accumulatedPlanActionQuantityBeforePause }${ plan.unit })</span>
                <br>
                <br>
                - 아직 목표 달성까지 ${ statPlan.numOfActionsToEndPlan }회, ${ statPlan.quantityToEndPlan }${ plan.unit } (${ statPlan.ratioOfQuantityToEndPlan }%) 남았어요<br>
                - 매 회 활동에 평균적으로 ${ statPlan.averageTimeTakenForRealAction }분이 소요되었어요
            </c:when>
            <c:otherwise>
                - ${ plan.lastStatusChangedAt }자 중도 포기한 활동이에요<br>
                - 총 ${ statPlan.accumulatedNumOfActions }회 ${ statPlan.accumulatedRealActionQuantity }${ plan.unit } 수행했었어요
            </c:otherwise>
        </c:choose>


        <br>
        <br>
    </div>

    <!--2023.7.31(월) 3h35 여기에도 활동기간의 달력 표시하고자 함-->
    <div class="calendar" align="center">
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
        <h4> 일정 목록 </h4>
<%--        * 수행 여부를 체크하면 기본적으로 수행 예정 분량이 실제 수행 분량으로 기록됩니다<br>--%>
<%--        * 상세 기록 버튼을 클릭해서 수행 소요 시간과 메모를 기억해 보세요~--%>
        <table class="actionDatesListTable" border="black" align="center">
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
                <td>수행 분량</td>
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
                            <td>-</td>
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
                            <td>${ day.realActionQuantity }</td>
                            
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