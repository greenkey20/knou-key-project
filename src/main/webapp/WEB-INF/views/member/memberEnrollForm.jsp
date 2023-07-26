<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 가입</title>
    <link rel="stylesheet" href="resources/css/member/memberEnrollForm.css">
</head>
<body>
<!--2023.7.26(수) 21h5 파일 생성 + 작업 시작-->

<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<script src="resources/js/member/memberEnrollForm.js"></script>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->

    <h2>회원 가입</h2>
    <br>

    <div class="loginForm" align="center">
        <form action="newMemberInsert.me" method="post" modelAttribute="member">
            <table>
                <tr>
                    <td class="info">이메일(아이디)<span class="required">*</span></td>
                    <td>
                        <span>
                            <input id="emailInput" type="email" name="email" placeholder="example@green.com" required>
                            <button type="button" onclick="idCheck();">중복 확인</button>
                        </span>
                        <div id="email-check-msg"></div>
                    </td>
                </tr>
                <tr>
                    <td class="info">닉네임<span class="required">*</span></td>
                    <td>
                        <span>
                            <input type="text" name="nickname" placeholder="닉네임을 입력해 주세요" required>
                            <button type="button" onclick="nicknameCheck();">중복 확인</button>
                        </span>
                        <div id="nickname-check-msg"></div>
                    </td>
                </tr>
                <tr>
                    <td class="info">비밀번호<span class="required">*</span>
                    <td>
                        <span><input id="password1" type="password" name="password" placeholder="영문자, 숫자, 특수문자(!@#$%^) 모두 포함하여 총 8~15자로 입력하세요" required></span>
                        <div class="error_next_box" id="error_password1"></div>
                    </td>
                </tr>
                <tr>
                    <td class="info">비밀번호 확인<span class="required">*</span></td>
                    <td>
                        <span><input id="password2" type="password" placeholder="위에 입력한 비밀번호를 다시 한 번 입력해 주세요"></span>
                        <div class="error_next_box" id="error_password2"></div>
                    </td>
                </tr>
                <tr>
                    <td class="info">나이</td>
                    <td><input type="number" name="age" min="1" max="120"></td>
                </tr>
                <tr>
                    <td class="info">성별</td>
                    <td>
                        <input type="radio" name="gender" value="MALE">남성
                        <input type="radio" name="gender" value="FEMALE">여성
                        <input type="radio" name="gender" value="OTHERS">기타
                    </td>
                </tr>
            </table>
            <br>
            <br>
            <button type="submit" class="greenBtn">회원 가입</button>
        </form>

    </div>

</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

</body>
</html>