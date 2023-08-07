<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>일일 활동 내역 수정</title>
    <link rel="stylesheet" href="resources/css/actiondate/actionDetailUpdateForm.css">
</head>
<body>
<!--2023.7.23(일) 0h50 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<%--<script src="resources/js/actiondate/actionDetailRecordForm.js"></script>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>일일 활동 내역 수정</h2>
    <br>
    <div class="object">
        <h3>${ plan.object }</h3>
    </div>
    <br>

    <div class="actionDetailForm" align="center">
        <form id="action-detail-form" action="actionDateUpdate.ad?actionDateId=${ actionDate.actionDateId }" method="post" modelAttribute="actionDate">
            <table border="seagreen">
                <colgroup>
                    <col style="width: 45%">
                    <col style="width: 55%">
                </colgroup>
                <tr>
                    <td class="info">
                        수행 날짜<span class="required">*</span><br>
                        <span class="smallerLetters">
                            (계획된 날짜: ${ actionDate.numOfYear }. ${ actionDate.numOfMonth }. ${ actionDate.numOfDay }
                            <c:choose>
                                <c:when test="${ actionDate.numOfDay == 1 }"> (월) </c:when>
                                <c:when test="${ actionDate.numOfDay == 2 }"> (화) </c:when>
                                <c:when test="${ actionDate.numOfDay == 3 }"> (수) </c:when>
                                <c:when test="${ actionDate.numOfDay == 4 }"> (목) </c:when>
                                <c:when test="${ actionDate.numOfDay == 5 }"> (금) </c:when>
                                <c:when test="${ actionDate.numOfDay == 6 }"> (토) </c:when>
                                <c:otherwise> (일) </c:otherwise>
                            </c:choose>
                            )
                        </span>
                    </td>
                    <td>
                        <input type="date" name="realActionDate" value="${ actionDate.realActionDate }" required>
                    </td>
                </tr>
                <tr>
                    <td class="info">
                        오늘 수행 분량을 기재해 주세요<span class="required">*</span><br>
                        <c:if test="${ plan.isMeasurable }">
                            <span class="smallerLetters">(목표 분량: ${ actionDate.planActionQuantity }${ plan.unit })</span>
                        </c:if>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${ plan.isMeasurable }">
                                <input type="number" name="realActionQuantity" value="${ actionDate.realActionQuantity }" min="1" required> ${ plan.unit }
                            </c:when>
                            <c:otherwise>
                                -
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="info">오늘 활동 소요시간을 기록해 보아요</td>
                    <td><input type="number" name="timeTakenForRealAction" min="1" value="${ actionDate.timeTakenForRealAction }"> 분</td>
                </tr>
                <tr>
                    <td class="info">오늘 활동은 몇 점으로 평가할 수 있을까요?</td>
                    <td>
                        <c:choose>
                            <c:when test="${ actionDate.reviewScore eq 1}">
                                <select name="reviewScore">
                                    <option value="5">5 - 무척 잘했어요!</option>
                                    <option value="4">4 - 열심히 했어요</option>
                                    <option value="3">3 - 그럭저럭 했어요</option>
                                    <option value="2">2 - 조금 어려웠어요</option>
                                    <option value="1" selected>1 - 힘들었어요</option>
                                </select>
                            </c:when>
                            <c:when test="${ actionDate.reviewScore eq 2}">
                                <select name="reviewScore">
                                    <option value="5">5 - 무척 잘했어요!</option>
                                    <option value="4">4 - 열심히 했어요</option>
                                    <option value="3">3 - 그럭저럭 했어요</option>
                                    <option value="2" selected>2 - 조금 어려웠어요</option>
                                    <option value="1">1 - 힘들었어요</option>
                                </select>
                            </c:when>
                            <c:when test="${ actionDate.reviewScore eq 3}">
                                <select name="reviewScore">
                                    <option value="5">5 - 무척 잘했어요!</option>
                                    <option value="4">4 - 열심히 했어요</option>
                                    <option value="3" selected>3 - 그럭저럭 했어요</option>
                                    <option value="2">2 - 조금 어려웠어요</option>
                                    <option value="1">1 - 힘들었어요</option>
                                </select>
                            </c:when>
                            <c:when test="${ actionDate.reviewScore eq 4}">
                                <select name="reviewScore">
                                    <option value="5">5 - 무척 잘했어요!</option>
                                    <option value="4" selected>4 - 열심히 했어요</option>
                                    <option value="3">3 - 그럭저럭 했어요</option>
                                    <option value="2">2 - 조금 어려웠어요</option>
                                    <option value="1">1 - 힘들었어요</option>
                                </select>
                            </c:when>
                            <c:otherwise>
                                <select name="reviewScore">
                                    <option value="5" selected>5 - 무척 잘했어요!</option>
                                    <option value="4">4 - 열심히 했어요</option>
                                    <option value="3">3 - 그럭저럭 했어요</option>
                                    <option value="2">2 - 조금 어려웠어요</option>
                                    <option value="1">1 - 힘들었어요</option>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="info">메모</td>
                    <td><textarea name="memo">${ actionDate.memo }</textarea></td>
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
                <button type="button" onclick="location.href='myPlanDetail.pl?planId=${ plan.planId }'">상세보기로 가기</button>
                <button type="submit" class="greenBtn">저장</button> <!--onclick="location.href='actionDateUpdate.ad?actionDateId='"-->
            </div>
        </form>
    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>