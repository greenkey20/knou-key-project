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

<script src="resources/js/plan/myPlanDetailView.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>일정 상세 보기</h2>
    <br>
    <div class="object">
        <h3>${ plan.object }</h3>
    </div>
    <br>

    <div id="my-plan-detail-stats">
        ${ plan.frequencyDetail } 총 ${ plan.totalNumOfActions }회 동안
        매 회 ${ plan.quantityPerDay }${ plan.unit }만큼~
        <br>
        - 오늘까지 진행 분량 ${ plan.accumulatedRealActionQuantity}${ plan.unit } / 오늘까지 계획했었던 분량 ${ plan.accumulatedPlanActionQuantity }${ plan.unit }
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

        - 목표 달성까지는 ${ plan.totalQuantity - plan.accumulatedRealActionQuantity }${ plan.unit } (${ plan.accumulatedRealActionQuantity / plan.totalQuantity * 100}%) 남았어요!<br>
        <br>
        - 오늘까지 ${ plan.accumulatedNumOfActions} 회 수행했고, ${ plan.totalNumOfActions - plan.accumulatedNumOfActions }회 남았습니다. 파이팅입니다 🍀<br>
        - 매 회 ${ quantityPerDay }${ plan.unit } 수행하는 데 평균적으로 ${ plan.averageTimeTakenForRealAction }분이 소요되고 있어요
    </div>

    <div class="calendar" align="center">
        <br>
        <h4> 일정 목록 </h4>
        * 수행 여부를 체크하면 기본적으로 수행 예정 분량이 실제 수행 분량으로 기록됩니다
        * 상세 기록 버튼을 클릭해서 수행 소요 시간과 메모를 기억해 보세요~
        <table class="actionDatesListTable" border="black" align="center">
            <thead>
            <tr>
                <td>No</td>
                <td>날짜</td>
                <td>수행 예정 분량</td>
                <td>수행 여부</td>
                <td>실제 수행 분량</td>
<%--                <td>소요 시간</td>--%>
                <td>상세 기록</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="day" items="${ actionDatesList }" varStatus="status">
                <tr>
                    <td class="holiday"> ${ status.count } </td>
                    <td>
                            ${ day.year }. ${ day.month }. ${ day.date }
                        <c:choose>
                            <c:when test="${ day.day == 1 }"> (월) </c:when>
                            <c:when test="${ day.day == 2 }"> (화) </c:when>
                            <c:when test="${ day.day == 3 }"> (수) </c:when>
                            <c:when test="${ day.day == 4 }"> (목) </c:when>
                            <c:when test="${ day.day == 5 }"> (금) </c:when>
                            <c:when test="${ day.day == 6 }"> (토) </c:when>
                            <c:otherwise> (일) </c:otherwise>
                        </c:choose>
                    </td>
                    <td> ${ day.planActionQuantity } ${ savedPlan.unit } </td>
                    <td class="check"><input type="checkbox" class="checkInput" name="isDone" value="${ plan.planId }"></td> <!--2023.7.29(토) 1h45 이 checkbox value가 무엇이 되어야 하는지 정확히 모르겠다 + 이렇게 html 태그 안에 jsp 쓸 수 있나?-->
                    <td><input class="realQuantity" type="number" name="realActionQuantity"></td> <!--위 체크박스를 선택하면 기본적으로 여기에는 planActionQuantity과 같은 값이 입력됨 vs 이 값 바꾸고 싶으면 number input 요소에 값 조정/입력-->
                    <td><button type="button">상세 기록</button></td> <!--소요 시간 및 메모 기록하려면, 아래 버튼 눌러서 '1일 활동 내역 기록' 화면으로 가야 함-->
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