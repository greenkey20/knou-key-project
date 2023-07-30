<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>allo 메인</title>
    <link rel="stylesheet" href="resources/css/common/main.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="outer">

    <c:if test="${ not empty loginUser }">
        <h3> ${ loginUser.nickname } 님, 어서 오세요!</h3>
        <br>

        <c:if test="${ not empty planList }">
            <h4>현재 수행 중인 활동</h4>

            <div align="right"><button class="greenBtn" onclick="location.href='myPlanList.pl'">나의 일정 보기</button></div>
            <br>

            <div id="my-plans-summary">
                <br>
                <table>
                    <c:forEach var="p" items="${ planList }">
                        <c:if test="${ p.status.toString() eq 'ACTIVE' }">
                            <tr>
                                <td class="mainCheck">✔️</td>
                                <td><span class="boldFont">${ p.object }️ x</span>/총 ${ p.totalDurationDays }회 | xx% 분량 ( <span class="boldFont">x</span>/총 ${ p.totalQuantity }${ p.unit } 진행 중</td> <!--loginMemberDto 만들어서 필요한 통계 자료 받아와야 함 + Member 객체 속성 중 일부만 받아와도 됨-->
                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
                <br>
                <br>
            </div>
        </c:if>
    </c:if>

    <br>
    <br>
    <h3>어떤 활동을 계획하고 계신가요?</h3>
    활동 계획을 세워보아요~
    <br>
    <br>
    <button onclick="location.href='calculatorNew.pl'">새로 계산하기</button>
    <br>
    <div class="smallerLetters">(계산 결과를 '나의 일정'에 추가하기 위해서는<br>로그인이 필요합니다)</div>
    <br>
    <br>
    <br>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>

</script>

</body>
</html>