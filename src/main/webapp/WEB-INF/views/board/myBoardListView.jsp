<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>나의 게시글 보기</title>
    <link rel="stylesheet" href="resources/css/board/myBoardListView.css">
</head>
<body>
<!--2023.8.24(목) 22h50 파일 생성 + 작업 시작-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/board/myBoardListView.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>나의 게시글 보기</h2>
    <br>

    <!--검색 영역 시작-->
    <div id="search-area" float="right">
        <form action="myBoardList.bd" method="GET">
            <input type="text" id="board-form" name="keyword" placeholder="게시글 제목이나 내용으로 검색합니다">
            <span><button type="submit" class="grayBtn">검색</button><span>
        </form>
    </div>
    <br>
    <br>
    <!--검색 영역 끝-->

    <!--테이블 영역 시작-->
    <div class="boardTableArea" align="center">
        <table border="seagreen" align="center">
            <colgroup>
                <col style="width: 5%">
                <col style="width: 10%">
                <col style="width: 25%">
                <col style="width: 40%">
                <col style="width: 15%">
                <col style="width: 5%">
            </colgroup>
            <tr bgcolor="#90ee90">
                <td class="info">글번호</td>
                <td class="info">상태</td>
                <td class="info">제목</td>
                <td class="info">활동명</td>
                <td class="info">작성일</td>
                <td class="info">조회수</td>
            </tr>

            <c:choose>
                <c:when test=" ${ empty boardDtolist } ">
                    <tr>
                        <td colspan="6" align="center">게시글이 없습니다</td>
                        <br>
                        <br>
                        <div align="center"><button type="button" onclick="location.href='myBoardList.bd'">목록으로 돌아가기</button></div>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="b" items="${ boardDtolist }">
                        <tr>
                            <td>${ b.boardId }</td>
                            <td>
                                <c:choose>
                                    <c:when test="${ b.boardType eq 'PLAN' }"><span style="color: lightgreen; background-color: green"> 진행중 </span></c:when>
                                    <c:when test="${ b.boardType eq 'SUCCESS' }"><span style="color: green; background-color: lightgreen"> 완료 </span></c:when>
                                </c:choose>
                            </td>
                            <td><a class="selectedBoard" href="boardDetail.bd?boardId=${ b.boardId }">${ b.title }</a></td>
                            <td>${ b.object }</td>
                            <td>${ b.createdAt }</td>
                            <td>${ b.readCount }</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
        <br>
        <br>
    </div>
    <!--테이블 영역 끝-->

    <!--2023.8.7(월) 0h55 페이징 영역 시작-->
    <div align="center">
        <ul class="pagination justify-content-center">
            <!--이전-->
            <c:choose>
                <c:when test=" ${ boardList.first }"></c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=0">처음</a></li>
                    <li class="page-item"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=${ boardList.number - 1}">⬅️</a></li>
                </c:otherwise>
            </c:choose>

            <!--페이지 그룹-->
            <c:forEach begin="${ startBlockPage }" end="${ endBlockPage }" var="i">
                <c:choose>
                    <c:when test="${ boardList.pageable.pageNumber + 1 == i}">
                        <li class="page-item disabled"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=${ i - 1}">${ i }</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=${ i - 1}">${ i }</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <!--다음-->
            <c:choose>
                <c:when test=" ${ boardList.last }"></c:when>
                <c:otherwise>
                    <li class="page-item"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=${ boardList.number + 1}">➡️</a></li>
                    <li class="page-item"><a class="page-link" href="myBoardList.bd?keyword=${ param.keyword }&page=${ boardList.totalPages - 1}">마지막</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
    <!--페이징 영역 끝-->

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>