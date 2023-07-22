<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>allo 메인</title>
    <link rel="stylesheet" href="resources/css/common/main.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>

<div class="outer">
    <br>
    <br>
    <h3>무엇을 계획하고 계신가요?</h3>
    <br>
    <button onclick="location.href='calculatorNew.pl'">새로 계산하기</button>
    <br>
    <br>
    <br>
    <br>
</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"/>

<script>

</script>

</body>
</html>