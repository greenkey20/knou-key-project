<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Page</title>
    <link rel="stylesheet" href="resources/css/member/myPage.css">
</head>
<body>
<!--2023.8.24(목) 1h10-->
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/member/myPage.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->
    <h2>My Page</h2>
    <h3> 반가워요, ${ loginUser.nickname } 님!</h3>
    email: ${ loginUser.email }
    <br>

    <div class="menuList">
        <table class="table table-bordered" align="center">
            <tr class="title">
                <td>🍀</td>
                <td><a href="myPlanList.pl">나의 활동 전체 보기</a></td>
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="myTodayPlanList.pl">나의 오늘의 일정 보기</a></td>
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="myBoardList.bd">나의 게시글 보기</a></td> <!--myBoardList.bd-->
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="myPlanScrapList.sc">내가 스크랩한 활동 보기</a></td> <!--myPlanScrapList.sc-->
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="updateMemberInfo.me">회원 정보 수정</a></td>
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="#">고객 센터</a></td>
            </tr>
            <tr class="title">
                <td>🍀</td>
                <td><a href="logout.me">로그아웃</a></td>
            </tr>
        </table>
    </div>
</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>