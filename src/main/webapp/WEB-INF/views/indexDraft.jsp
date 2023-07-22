<!--
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>allo</title>
</head>
<body>
<h3>INDEX 페이지</h3>
hello, Spring Boot app with JSP!
</body>
</html> -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%
    response.sendRedirect(request.getContextPath()+"/main");
%>
<!--

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



</body>
</html> -->