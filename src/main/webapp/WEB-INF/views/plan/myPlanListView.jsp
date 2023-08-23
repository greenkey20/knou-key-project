<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

    <!--2023.7.28(금) 17h 추가-->
    <c:choose>
        <c:when test="${ empty list }">
            일정이 없습니다
            <br>
            <br>
            <div align="center"><button type="button" onclick="location.href='myPlanList.pl'">목록으로 돌아가기</button></div>
        </c:when>
        <c:otherwise>
            <!--2023.7.27(목) 21h10 추가-->
            <!--검색 영역 시작-->
            <div id="search-area" float="right">
                <form action="myPlanList.pl" method="GET">
<%--                    검색 <input type="text" id="form" name="keyword" onkeyup="enterKey();" placeholder="활동 제목으로 검색합니다">--%>
                    <input type="text" id="form" name="keyword" placeholder="활동 제목으로 검색합니다">
                    <span><button type="submit" class="grayBtn">검색</button><span>
                </form>
            </div>
            <!--검색 영역 끝-->

            <br>
            <br>
            <!--테이블 영역 시작-->
            <div>

                <c:forEach var="p" items="${ list }" varStatus="status"> <!--statList도 순회해야 함 + 두 lists 모두 planId desc으로 정렬되어있음-->
                    <div class="object">
                        <h4>${ p.object }</h4>
                    </div>

                    <div class="object" align="right">
                        <input class="planId" type="hidden" name="planId" value="${ p.planId } ">
                        <button class="detailRequest" type="button" onclick="location.href='myPlanDetail.pl?planId=${ p.planId }'">상세 조회</button>
                    </div>

                    <div class="listView">
                        <table class="table table-bordered" align="center">
                            <tbody>
                            <tr>
                                <td class="title">상태</td>
                                <td>
                                    <jsp:useBean id="now" class="java.util.Date" />
                                    <fmt:formatDate value="${ now }" pattern="yyyy-MM-dd" var="today" />

                                    <c:choose>
                                        <c:when test="${ p.status eq 'ACTIVE' }">
                                            <c:choose>
                                                <c:when test="${ p.isMeasurable }">
                                                    <c:choose>
                                                        <c:when test="${ statList[status.index].quantityDifferenceBetweenPlanAndReal lt 0 }">
                                                            ⭐️ 수행 중 <span class="smallerLetters">→ 계획보다 앞서 있어요 👍</span>
                                                        </c:when>
                                                        <c:when test="${ statList[status.index].quantityDifferenceBetweenPlanAndReal gt 0 }">
                                                            ⭐️ 수행 중 <span class="smallerLetters">→ 계획보다 뒤처져 있어요 🌱</span>
                                                        </c:when>
                                                        <c:when test="${ today < p.startDate }">
                                                            아직 시작일이 되지 않았어요
                                                        </c:when>
                                                        <c:otherwise>
                                                            ⭐️ 수행 중 <span class="smallerLetters">→ 계획대로 잘 진행하고 있어요 💯</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${ today < p.startDate }">
                                                            아직 시작일이 되지 않았어요
                                                        </c:when>
                                                        <c:otherwise>
                                                            ⭐️ 수행 중
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${ p.status eq 'COMPLETE' }">
                                            완료했어요!
                                        </c:when>
                                        <c:when test="${ p.status eq 'PAUSE' }">
                                            일시 정지 중이에요
                                            <c:if test="${ p.sizeOfModifiedPlansList gt 0 }">
                                                <span> → </span>
                                                <span style="color: lightgreen; background-color: green"> 새로 계산된 내역으로 이어서 하고 있어요 </span>
                                            </c:if>
                                        </c:when>
                                        <c:when test="${ p.status eq 'GIVEUP' }">
                                            포기했어요 ㅠㅠ
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="title">기간</td>
                                <td>${ p.startDate } ~ ${ p.deadlineDate }</td>
                            </tr>
                            <tr>
                                <td class="title">수행 빈도</td>
                                <td>${ p.frequencyDetail } (총 ${ p.totalNumOfActions }회)</td>
                            </tr>
                            <tr>
                                <td class="title">매 회 수행 분량</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${ p.isMeasurable }">${ p.quantityPerDay } ${ p.unit }</c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="title">진행률</td>
                                <td>
                                    ${ statList[status.index].accumulatedNumOfActions }회 진행,
                                    <c:if test="${ p.isMeasurable }">
                                        <br>
                                        현재 ${ statList[status.index].accumulatedRealActionQuantity } ${ p.unit }
                                        <span class="smallerLetters">(/전체 ${ p.totalQuantity } ${ p.unit }) </span>
                                        전체 분량의 ${ statList[status.index].ratioOfRealActionQuantityTillToday }% 완료
                                    </c:if>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <br>
                    </div>
                </c:forEach>
            </div>
            <!--테이블 영역 끝-->

            <!--2023.7.27(목) 21h30 페이징 영역 시작-->
            <div align="center">
                <ul class="pagination justify-content-center">
                    <!--이전-->
                    <c:choose>
                        <c:when test=" ${ planList.first }"></c:when>
                        <c:otherwise>
                            <li class="page-item"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=0">처음</a></li>
                            <li class="page-item"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=${ planList.number - 1}">⬅️</a></li>
                        </c:otherwise>
                    </c:choose>

                    <!--페이지 그룹-->
                    <c:forEach begin="${ startBlockPage }" end="${ endBlockPage }" var="i">
                        <c:choose>
                            <c:when test="${ planList.pageable.pageNumber + 1 == i}">
                                <li class="page-item disabled"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=${ i - 1}">${ i }</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=${ i - 1}">${ i }</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <!--다음-->
                    <c:choose>
                        <c:when test=" ${ planList.last }"></c:when>
                        <c:otherwise>
                            <li class="page-item"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=${ planList.number + 1}">➡️</a></li>
                            <li class="page-item"><a class="page-link" href="myPlanList.pl?keyword=${ param.keyword }&page=${ planList.totalPages - 1}">마지막</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
            <!--페이징 영역 끝-->
        </c:otherwise>
    </c:choose>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>