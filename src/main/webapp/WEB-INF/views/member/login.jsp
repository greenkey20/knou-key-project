<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <link rel="stylesheet" href="resources/css/member/login.css">
</head>
<body>
<!--2023.7.26(수) 20h50 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/member/login.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>로그인</h2>
    <br>

    <div class="loginForm" align="center">
        <form action="login.me" method="post" modelAttribute="member">
            <table>
                <tr>
                    <td>아이디</td>
                    <td><input type="email" name="email" placeholder="아이디(이메일)를 입력해 주세요"></td>
                </tr>
                <tr>
                    <td>비밀번호</td>
                    <td><input type="password" name="password" placeholder="비밀번호를 입력해 주세요"></td>
                </tr>
            </table>

            <br>
            <a href="enroll.me" class="link">회원 가입</a> | <a href="findIdPw.me" class="link">ID/PW 찾기</a>

            <br>
            <br>
<%--            <input type="hidden" name="memberId" value="${ joinMemberId }">--%>
            <button type="submit" class="greenBtn" value="login">로그인</button>
        </form>

    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>