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

            <div id="my-plans-summary">
                <table>
                    <c:forEach var="p" items="${ planList }">
                        <c:if test="${ p.planStatus.toString() eq 'ACTIVE' }">
                            <tr>
                                <td>✔️</td>
                                <td>${ p.object}️ x회 (/ 총 ${ p.totalDurationDays }회) | xx% 진행 중</td>

                            </tr>
                        </c:if>
                    </c:forEach>
                </table>
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
    <br>
    <br>
    <br>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>

</script>

</body>
</html>