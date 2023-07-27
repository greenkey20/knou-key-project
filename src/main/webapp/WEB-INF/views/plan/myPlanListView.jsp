<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>나의 일정</title>
    <link rel="stylesheet" href="resources/css/plan/myPlanListView.css">
</head>
<body>
<!--2023.7.24(월) 23h30-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/plan/myPlanListView.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>나의 일정</h2>
    <br>

    <c:forEach var="p" items="${ list }">
        <div class="object">
            <h3>${ p.object }</h3>
        </div>

        <div class="object">
            <input class="planId" type="hidden" name="planId" value="${ p.planId } ">
            <button class="detailRequest" type="button">상세 조회</button>
        </div>

        <div class="listView">
            <table class="table table-bordered" align="center">
                <tbody>
                    <tr>
                        <td class="title">상태</td>
                        <td>
                            <c:choose>
                                <c:when test="${ p.status eq 'ACTIVE' }">
                                        수행 중 (일정대로 진행하고 있어요 등등)<!--통계 데이터 만든 다음에 작성-->
                                </c:when>
                                <c:when test="${ p.status eq 'COMPLETE' }">
                                    완료했어요!
                                </c:when>
                                <c:when test="${ p.status eq 'PAUSE' }">
                                    일시 정지 중이에요
                                </c:when>
                                <c:otherwise>
                                    포기했어요 ㅠㅠ
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="title">기간</td>
                        <td>
                            ${ p.startDate } ~ ${ p.deadlineDate } 
                                <span>
                                    <c:if test="${ !p.hasDeadline }">(종료 기간을 지정하지 않았어요)</c:if>
                                </span>
                            <br>
                            (총 ${ p.totalNumOfActions }회)
                        </td>
                    </tr>
                    <tr>
                        <td class="title">수행 빈도</td>
                        <td>${ p.frequencyDetail }</td>
                    </tr>
                    <tr>
                        <td class="title">매 회 수행 분량</td>
                        <td>${ p.quantityPerDay } ${ p.unit } </td>
                    </tr>
                    <tr>
                        <td class="title">진행률</td>
                        <td><!--통계 데이터 만든 다음에 작성--></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </c:forEach>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>