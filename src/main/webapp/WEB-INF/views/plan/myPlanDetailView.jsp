<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        ${ plan.frequencyDetail } 총 ${ plan.totalNumOfActions }회 동안
        매 회 ${ plan.quantityPerDay }${ plan.unit }만큼~
        <br>
        - 오늘까지 진행 분량 ${ plan.accumulatedRealActionQuantity}${ plan.unit } / 오늘까지 계획했었던
        분량 ${ plan.accumulatedPlanActionQuantity }${ plan.unit }
        <c:choose>
            <c:when test="${ plan.accumulatedRealActionQuantity > plan.accumulatedPlanActionQuantity }">
                → 계획보다 ${ plan.accumulatedRealActionQuantity - plan.accumulatedPlanActionQuantity }${ plan.unit }만큼 앞서 있어요 👍<br>
            </c:when>
            <c:when test="${ plan.accumulatedRealActionQuantity < plan.accumulatedPlanActionQuantity }">
                → 계획보다 ${ plan.accumulatedPlanActionQuantity - plan.accumulatedRealActionQuantity }${ plan.unit }만큼 뒤처져 있어요 🌱<br>
            </c:when>
            <c:otherwise>
                → 계획대로 잘 진행하고 있어요 💯<br>
            </c:otherwise>
        </c:choose>

        - 목표 달성까지는 ${ plan.totalQuantity - plan.accumulatedRealActionQuantity }${ plan.unit }
        (${ plan.accumulatedRealActionQuantity / plan.totalQuantity * 100}%) 남았어요!<br>
        <br>
        - 오늘까지 ${ plan.accumulatedNumOfActions} 회 수행했고, ${ plan.totalNumOfActions - plan.accumulatedNumOfActions }회
        남았습니다. 파이팅입니다 🍀<br>
        - 매 회 ${ quantityPerDay }${ plan.unit } 수행하는 데 평균적으로 ${ plan.averageTimeTakenForRealAction }분이 소요되고 있어요
        <br>
        <br>
    </div>

    <div class="calendar" align="center">
        <br>
        <h4> 일정 목록 </h4>
<%--        * 수행 여부를 체크하면 기본적으로 수행 예정 분량이 실제 수행 분량으로 기록됩니다<br>--%>
<%--        * 상세 기록 버튼을 클릭해서 수행 소요 시간과 메모를 기억해 보세요~--%>
        <table class="actionDatesListTable" border="black" align="center">
            <colgroup>
                <col style="width: 10%">
                <col style="width: 25%">
                <col style="width: 10%">
                <col style="width: 15%">
                <col style="width: 25%">
                <col style="width: 15%">
            </colgroup>

            <thead>
            <tr>
                <td width="35px">No</td>
                <td width="140px">날짜</td>
                <td width="50px">수행 여부</td>
                <td width="35px%">수행 분량</td>
                <td width="50px">활동 만족도</td>
                <%--                <td>소요 시간</td>--%>
                <td width="50px">기록</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="day" items="${ actionDatesList }" varStatus="status">
                <tr>
                    <td class="holiday"> ${ status.count } </td>

                    <td>
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
                    </td>

                    <c:choose>
                        <c:when test="${ not day.isDone }">
                            <td class="check">-</td>
                            <td>-</td>
                            <td>-</td>
                            <td><button type="button" onclick="location.href='actionDetailRecordPage.ad?planId=${ plan.planId }&actionDateId=${ day.actionDateId }'">상세 기록</button></td>
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

                            <td><button type="button" onclick="location.href='actionDetailView.ad?planID=${ plan.planId }&actionDateId=${ day.actionDateId }'">상세 기록</button></td> <!--소요 시간 및 메모 기록하려면, 아래 버튼 눌러서 '1일 활동 내역 기록' 화면으로 가야 함-->
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <br>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>