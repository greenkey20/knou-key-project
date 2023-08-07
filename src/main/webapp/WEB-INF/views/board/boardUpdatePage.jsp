<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>공유 게시판 글 수정</title>
    <link rel="stylesheet" href="resources/css/board/boardUpdatePage.css">
</head>
<body>
<!--2023.8.7(월) 3h20 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>공유 게시판 글 수정</h2>
    <br>

    <div class="boardUpdatePage" align="center">
        <form id="board-update-page" action="boardUpdate.bd?boardId=${ board.boardId }" method="post" modelAttribute="board">
            <table border="seagreen">
                <colgroup>
                    <col style="width: 25%">
                    <col style="width: 75%">
                </colgroup>
                <tr>
                    <td class="info">상태</td>
                    <td>
                        <c:choose>
                            <c:when test="${ board.boardType eq 'PLAN' }"><span style="color: lightgreen; background-color: green"> 진행중 </span></c:when>
                            <c:when test="${ board.boardType eq 'SUCCESS' }"><span style="color: green; background-color: lightgreen"> 완료 </span></c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="info">게시글 제목<span class="required">*</span></td>
                    <td><input type="text" name="title" value="${ board.title }}" required width="100%"></td>
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
                        <c:if test="${ plan.isMeasurable }">
                            오늘까지 ${ statPlan.ratioOfRealActionQuantityTillToday }% 분량 수행
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td class="info">게시글 내용<span class="required">*</span></td>
                    <td><textarea class="contentTextArea" name="content">${ board.content }</textarea></td>
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

            <input type="hidden" name="planId" value="${ board.planId }">

            <div align="center">
                <button type="button" onclick="location.href='boardList.bd'">목록으로 가기</button>
                <button type="submit" class="greenBtn">수정</button> <!--onclick="location.href='boardUpdate.bd?boardId='"-->
            </div>
        </form>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>