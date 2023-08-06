<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공유 게시판 글 작성</title>
    <link rel="stylesheet" href="resources/css/board/boardEnrollForm.css">
</head>
<body>
<!--2023.8.6(일) 6h30 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<%--<script src="resources/js/board/boardEnrollForm.js"></script>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>공유 게시판 글 작성</h2>
    <br>
    <div class="object">
        <h4>나의 활동 내역을 공유해 보아요!</h4>
    </div>
    <br>

    <%--    <p>* 표시는 필수 입력 사항입니다.</p>--%>
    <div class="boardEnrollForm" align="center">
        <form id="board-enroll-form" action="newBoardInsert.bd" method="post" modelAttribute="board">
            <table border="seagreen">
                <colgroup>
                    <col style="width: 25%">
                    <col style="width: 75%">
                </colgroup>
                <tr>
                    <td class="info">상태</td>
                    <td style="color: lightgreen; background-color: green">
                        <c:choose>
                            <c:when test="${ plan.status eq 'ACTIVE' }">진행 중</c:when>
                            <c:when test="${ plan.status eq 'COMPLETE' }">완료</c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="info">게시글 제목<span class="required">*</span></td>
                    <td><input type="text" name="title" placeholder="게시글의 제목을 입력해 주세요" required width="460px"></td>
                </tr>
                <tr>
                    <td class="info">활동 제목</td>
                    <td>${ plan.object }</td>
                </tr>
                <tr>
                    <td class="info">기간</td>
                    <td>
                        ${ plan.startDate } ~ 현재
                        <span class="smallerLetters">(${ plan.deadlineDate } 종료 예정, 총 기간: ${ plan.totalDurationDays }일)</span>
                    </td>
                </tr>
                <tr>
                    <td class="info">수행 빈도</td>
                    <td>${ plan.frequencyDetail }</td>
                </tr>
                <tr>
                    <td class="info">현재 진행률</td>
                    <td>
                        오늘까지 ${ statPlan.accumulatedNumOfActions }회 수행
                        <span class="smallerLetters"> ( / 전체 ${ plan.totalNumOfActions}회),</span><br>
                        오늘까지 ${ statPlan.ratioOfRealActionQuantityTillToday }% 분량 수행
                    </td>
                </tr>
                <tr>
                    <td class="info">게시글 내용<span class="required">*</span></td>
                    <td><textarea class="contentTextArea" name="content" placeholder="활동 중 좋았던 점, 배운 점, 자랑스러운 점, 힘들었던 점, 실행 팁 등을 자유롭게 작성해 주세요"></textarea></td>
                </tr>
            </table>
            <br>
            <br>

            <%-- loginUser 속성 값 있으면 아래와 같이 작성자 정보 입력 --%>
            <c:choose>
                <c:when test="${ not empty loginUser }">
                    <input type="hidden" name="memberId" value="${ loginUser.memberId }">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="memberId" value="">
                </c:otherwise>
            </c:choose>

            <input type="hidden" name="planId" value="${ plan.planId }">
            <c:choose>
                <c:when test="${ plan.status eq 'ACTIVE' }"><input type="hidden" name="boardType" value="PLAN"></c:when>
                <c:when test="${ plan.status eq 'COMPLETE' }"><input type="hidden" name="boardType" value="SUCCESS"></c:when>
            </c:choose>

            <div align="center">
                <button type="button" onclick="window.history.back()">뒤로 가기</button>
                <button type="submit" class="greenBtn">저장</button> <!--onclick="location.href='newBoardInsert.bd'"-->
            </div>
        </form>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>