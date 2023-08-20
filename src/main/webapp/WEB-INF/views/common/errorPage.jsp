<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>에러 페이지</title>
    <link rel="stylesheet" href="resources/css/common/errorPage.css">
</head>
<body>
<%--<jsp:include page="/WEB-INF/views/common/header.jsp"/>--%>

<div class="outer"> <!--header 아래 모든 부분 감싸는 div-->
    <br>
    <div align="center">
        <img src="https://cdn2.iconfinder.com/data/icons/oops-404-error/64/208_balloon-bubble-chat-conversation-sorry-speech-256.png">
        <br><br>
        <h4>${ errorMsg }</h4> <!--style="font-weight:bold;"-->
        <br><br><br>
        <button type="button" onclick="history.back();">이전 페이지로 이동</button>
        <button type="button" class="grayBtn" onclick="location.href='/key-project'">홈페이지로 이동</button>
    </div>
    <br>
</div> <!--header 아래 모든 부분 감싸는 div 'outer' 영역 끝-->

<%--<jsp:include page="/WEB-INF/views/common/footer.jsp"/>--%>

</body>
</html>