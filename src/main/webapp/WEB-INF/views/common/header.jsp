<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <!-- ajax, 제이쿼리 -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- 부트스트랩에서 제공하고 있는 스타일 -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- 부트스트랩에서 제공하고 있는 스크립트 -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="resources/css/common/header.css">
</head>
<body>

<c:if test="${ not empty alertMsg }">
    <script>
        alert("${ alertMsg }")
    </script>
    <c:remove var="alertMsg" scope="session"/>
</c:if>

<div class="header" align="center">
    <div class="logo_area">
        <div class="logo_bg">
            <p class="logo_k">allo</p>
<%--            <p class="logo_e">allo</p>--%>
        </div>
        <div>
            <c:choose>
                <c:when test="${ empty loginUser }">
                    <!-- 로그인 전 -->
                    <div class="service_area">
                        <a href="loginPage.me">로그인</a>
                        <a href="enroll.me">회원 가입</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- 로그인 후 -->
                    <div class="service_area">
                        <a href="logout.me">로그아웃</a>
                        <a href="myPage.cmm">My Page</a>
                    </div>

                    <div class="navi_area">
                        <div class="group_navi">
                            <ul class="list_navi">
                                <li class="navi_item"><a href="#" class="link navi">HOME</a></li>
                                <li class="navi_item"><a href="saladListView.cmm" class="link navi">나의 일정</a></li>
                                <li class="navi_item"><a href="communityList.co" class="link navi">Statistics</a></li>
                                    <%--                <li class="navi_item"><a href="list.no" class="link navi">My Page</a></li>--%>
                            </ul>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

<%--    <div class="navi_area">--%>
<%--        <div class="group_navi">--%>
<%--            <ul class="list_navi">--%>
<%--                <li class="navi_item"><a href="#" class="link navi">HOME</a></li>--%>
<%--                <li class="navi_item"><a href="saladListView.cmm" class="link navi">나의 일정</a></li>--%>
<%--                <li class="navi_item"><a href="communityList.co" class="link navi">Statistics</a></li>--%>
<%--&lt;%&ndash;                <li class="navi_item"><a href="list.no" class="link navi">My Page</a></li>&ndash;%&gt;--%>
<%--            </ul>--%>
<%--        </div>--%>
<%--    </div>--%>

</div>

</body>
</html>