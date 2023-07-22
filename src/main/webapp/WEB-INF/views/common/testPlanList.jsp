<%@page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>게시판 목록</title>
    <style>
        table {
            width: 100%;
            border: 1px solid #444444;
            border-collapse: collapse;
        }
        table th {
            border: 1px solid #444444;
            text-align: center;
            height: 40px;
            background-color: dodgerblue;
            color: cornsilk;
        }
        table td {
            border: 1px solid #444444;
            text-align: center;
        }
    </style>
</head>
<body>
<div style="text-align: center;">
    <h1>게시판 목록</h1>
    <table style="width: 700px; margin: auto">
        <tr>
            <th style="width: 10%">번호</th>
            <th style="width: 50%">object</th>
            <th style="width: 15%">frequency detail</th>
            <th style="width: 15%">created at</th>
            <th style="width: 10%">status</th>
        </tr>
        <c:forEach var="plan" items="${planList}">
            <tr>
                <td>${plan.planId}</td>
                <td style="text-align: left"><a href="getBoard?seq=${plan.planId}">${plan.object}</a></td>
                <td>${plan.frequencyDetail}</td>
                <td><fmt:formatDate value="${plan.createdAt}" pattern="yyyy-MM-dd"></fmt:formatDate> </td>
                <td>${plan.status}</td>
            </tr>
        </c:forEach>
    </table>
    <a href="insertBoard">새글 등록</a>
</div>
</body>
</html>