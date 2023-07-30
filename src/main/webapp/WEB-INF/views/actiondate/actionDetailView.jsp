<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>일일 활동 내역 조회</title>
    <link rel="stylesheet" href="resources/css/actiondate/actionDetailView.css">
</head>
<body>
<!--2023.7.31(월) 5h 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<%--<script src="resources/js/actiondate/actionDetailView.js"></script>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>일일 활동 내역 조회</h2>
    <br>
    <div class="object">
        <h3>${ plan.object }</h3>
    </div>
    <br>

    <div class="actionDetailView" align="center">
        <table border="seagreen">
            <colgroup>
                <col style="width: 25%">
                <col style="width: 75%">
            </colgroup>
            <tr>
                <td class="info">오늘의 날짜</td>
                <td>${ actionDate.realActionDate }</td>
            </tr>
            <tr>
                <td class="info">오늘 수행 분량</td>
                <td>
                    ${ actionDate.realActionQuantity }${ plan.unit }
                    <span class="smallerLetters">(목표 분량: ${ actionDate.planActionQuantity }${ plan.unit })</span>
                </td>
            </tr>
            <tr>
                <td class="info">오늘 활동 소요시간</td>
                <td>
                    <c:choose>
                        <c:when test="${ not empty actionDate.timeTakenForRealAction }">
                            ${ actionDate.timeTakenForRealAction }분
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="info">오늘의 평가</td>
                <td>
                    <c:choose>
                        <c:when test="${ not empty actionDate.reviewScore }">
                            <c:choose>
                                <c:when test="${ actionDate.reviewScore eq 5 }">⭐⭐⭐️ 무척 잘했어요!</c:when>
                                <c:when test="${ actionDate.reviewScore eq 4 }">⭐⭐ 열심히 했어요</c:when>
                                <c:when test="${ actionDate.reviewScore eq 3 }">⭐⭐ 그럭저럭 했어요</c:when>
                                <c:when test="${ actionDate.reviewScore eq 2 }">⭐ 조금 어려웠어요</c:when>
                                <c:otherwise>⭐ 힘들었어요</c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td class="info">메모</td>
                <td>
                    <c:choose>
                        <c:when test="${ not empty actionDate.memo }">${ actionDate.memo}</c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
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
        <input type="hidden" name="actionDateId" value="${ actionDate.actionDateId }">

        <div align="center">
            <button type="button" class="grayBtn" onclick="window.history.back()">뒤로 가기</button>
            <button type="button" data-toggle="modal" data-target="#deleteForm">삭제</button> <!--id="clickModal" -->
<%--            <button type="button" onclick="location.href='actionDetailDelete.ad?planID=${ plan.planId }&actionDateId=${ actionDate.actionDateId }'">삭제</button>--%>
            <button type="button" class="greenBtn" onclick="location.href='actionDetailUpdatePage.ad?planID=${ plan.planId }&actionDateId=${ actionDate.actionDateId }'">수정</button>
        </div>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<!-- The Modal : deleteForm -->
<div class="modal delete-form fade" id="deleteForm">
    <div class="modal-dialog">
        <div class="modal-content">

            <!-- Modal Header -->
            <div class="modal-header">
                <h4 class="modal-title">일일 활동 내역 삭제</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>

            <!-- Modal body -->
            <div class="modal-body">
                <ul id="deleteList"></ul>
                <span>이 일일 활동 기록을 정말로 삭제하시겠습니까?</span>
            </div>

            <!-- Modal footer -->
            <div class="modal-footer">
                <button type="button" class="grayBtn" data-dismiss="modal">취소</button>
                <form method="post" action="actionDetailDelete.ad" id="postForm">
                    <input type="hidden" name="planId" value="${ plan.planId }">
                    <input type="hidden" name="actionDateId" value="${ actionDate.actionDateId }">
                    <button type="submit" id="deleteSubmit">삭제</button>
                </form>
            </div>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>