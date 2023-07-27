<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>활동 계획 계산 결과</title>
    <link rel="stylesheet" href="resources/css/plan/newPlanResultView.css">
</head>
<body>
<!--2023.7.24(월) 0h55 파일 생성 + 작업 시작-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/plan/newPlanResultView.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>활동 계획 계산 결과</h2>
    <br>

    <div id="new-plan-calculator-result">
        ${ savedPlan.object } ${ savedPlan.totalQuantity}${ savedPlan.unit} 목표 달성을 위해서는

        <!--startDate가 null인 경우 vs 날짜 있는 경우-->
        <!--처리 상태(answer)가 "N"인 경우 '미처리' vs "Y"인 경우 '처리 완료' 배지 표시-->
        <c:choose>
            <c:when test="${ !savedPlan.hasStartDate }">
                오늘부터 시작한다면
            </c:when>
            <c:otherwise>
                ${ savedPlan.startDate }부터 시작하여
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${ savedPlan.hasDeadline }">
                ${ savedPlan.deadlineDate }까지 ${ savedPlan.totalDurationDays } 일 기간 중
            </c:when>
            <c:otherwise>

            </c:otherwise>
        </c:choose>
        ${ savedPlan.frequencyDetail }, 총 ${ savedPlan.totalNumOfActions }회/일
        매번 ${ savedPlan.quantityPerDay }${ savedPlan.unit}만큼 수행해야 합니다.
    </div>

    <div class="calendar" align="center">
        <br>
        <h4> 활동일 목록 예시 </h4>
        <table class="actionDatesListTable" border="black" align="center">
            <thead>
                <tr>
                    <td>No</td>
                    <td>활동 수행 예정일</td>
                    <td>수행 예정 분량</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="day" items="${ actionDatesList }" varStatus="status">
                <c:if test="${ day.dateType.toString() eq 'ACTION' }">
                    <tr>
                    <td class="holiday"> ${ status.count } </td>
<%--                        <td> <input type="checkbox" class="check"> </td>--%>
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
                    </tr>
                </c:if>
                </c:forEach>
            </tbody>
        </table>
        <br>
    </div>

    <div class="actionDatesListList">

    </div>

    <!--2023.7.24(월) 15h-->
    <!--하단 버튼-->
    <!--form 태그에
    1. onsubmit 속성 = 메서드 호출 리턴 값(true/false)에 따라 submit 수행 여부 제어 (-> 이건 여기에서 적용할 필요 없는 것 같아 생략 -> 18h5 나의 생각 = 아닌가? 필요해서 '새로 계산' 버튼 클릭 시 500error?)
    2. action 속성 대신, button에 formaction 속성 줌(값 = 요청 url) -> form 태그 안에서 여러 개 버튼별 원하는 요청을 각기 다르게 할 수 있음-->
    <form method="post" action="myNewPlanInsert.pl" modelAttribute="plan">
        <c:if test="${ !savedPlan.hasStartDate }">
            <span>시작일 지정하고 </span>
            <input type="date" name="startDate" required>
            <br>
        </c:if>

        <input id="hidden-member-id" type="hidden" name="memberId" value="${ loginUser.memberId }">
        <input type="hidden" name="planId" value="${ savedPlan.planId }">

        <div align="center">
            <button type="submit" onsubmit="return checkLogin();" class="greenBtn">나의 일정에 추가</button>
            <!--onclick="location.href='myNewPlanInsert.pl'"-->
            <button type="button" onclick="location.href='calculatorNew.pl'">새로 계산하기</button>
        </div>
    </form>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>