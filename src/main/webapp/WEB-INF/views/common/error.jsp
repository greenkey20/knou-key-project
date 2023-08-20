<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>error</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<br>
<div align="center">
    <img src="https://cdn2.iconfinder.com/data/icons/oops-404-error/64/208_balloon-bubble-chat-conversation-sorry-speech-256.png">
    <br><br>
<%--    <h1 class="mt-4">에러 페이지</h1>--%>

    <div class="panel panel-primary">
        <div class="panel-heading"><p>${title}</p></div>
        <div class="panel-body">
            <code>${description}</code>
            <br>
            <br>
            <a href="/key-project">홈페이지로 이동</a>
        </div>
    </div>
</div>
</body>
</html>
