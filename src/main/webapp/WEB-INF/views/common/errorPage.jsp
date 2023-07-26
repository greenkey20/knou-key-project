<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>errorPage</title>
</head>
<body>

<jsp:include page="header.jsp"/>

<br>
<div align="center">
    <img src="https://cdn2.iconfinder.com/data/icons/oops-404-error/64/208_balloon-bubble-chat-conversation-sorry-speech-256.png">
    <br><br>
    <h4>${ errorMsg }</h4> <!--style="font-weight:bold;"-->
</div>
<br>

<jsp:include page="footer.jsp"/>

</body>
</html>