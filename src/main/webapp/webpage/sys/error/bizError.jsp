<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>参数错误！</title>
    <%@include file="/include/head.jsp"%>
</head>
<body style="text-align: center;">
    发生了错误：<br/>
    <c:out value="${ex.message}"  />
</body>
</html>
