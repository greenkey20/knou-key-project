<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공유 게시판 글 상세 조회</title>
    <link rel="stylesheet" href="resources/css/board/boardDetailView.css">
</head>
<body>
<!--2023.8.7(월) 2h40 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<%--<script src="resources/js/board/boardEnrollForm.js"></script>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>공유 게시판 글 상세 조회</h2>
    <br>
    <div class="object">
        <h4>${ board.title }</h4>
    </div>
    <br>
    <div align="right">작성자 : ${ board.nickname } </div>
    <div align="right">작성일 : ${ board.createdAt } </div>

    <div class="boardDetailTable" align="center">
        <table border="seagreen">
            <colgroup>
                <col style="width: 25%">
                <col style="width: 75%">
            </colgroup>
            <tr>
                <td class="info">상태</td>
                <td >
                    <c:choose>
                        <c:when test="${ board.boardType eq 'PLAN' }"><span style="color: lightgreen; background-color: green"> 진행중 </span></c:when>
                        <c:when test="${ board.boardType eq 'SUCCESS' }"><span style="color: green; background-color: lightgreen"> 완료 </span></c:when>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="info">활동 제목</td>
                <td>${ board.object }</td>
            </tr>
            <tr>
                <td class="info">기간</td>
                <td>
                    ${ plan.startDate } ~ ${ board.createdAt }
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
                    작성일까지 ${ statPlan.accumulatedNumOfActions }회 수행
                    <span class="smallerLetters"> ( / 전체 ${ plan.totalNumOfActions}회),</span><br>
                    작성일까지 ${ statPlan.ratioOfRealActionQuantityTillToday }% 분량 수행
                </td>
            </tr>
            <tr>
                <td class="info">게시글 내용</td>
                <td>${ board.content }</td>
            </tr>
        </table>
        <br>
        <br>
    </div>

    <div align="center">
        <button type="button" onclick="location.href='boardList.bd'">목록으로 가기</button>
        <c:if test="${ loginUser.memberId eq board.memberId }">
            <button type="button" class="grayBtn" onclick="location.href='boardUpdatePage.bd?boardId=${ board.boardId }'">수정</button>
        </c:if>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>