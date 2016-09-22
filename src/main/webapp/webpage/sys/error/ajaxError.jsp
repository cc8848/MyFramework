<%@ page contentType="application/json;charset=UTF-8" language="java"%>
<%Exception e = (Exception) request.getAttribute("ex");%>
{"success":false,"error":"<%=e.getClass().getSimpleName()%>","msg":"<%=e.getMessage()%>"}