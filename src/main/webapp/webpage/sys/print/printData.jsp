<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>数据打印</title>
    <%@include file="/include/head.jsp"%>

    <style>
        .tborder{
            border-collapse:collapse;

        }
        .tborder tr td {
            border:1px solid #9a9a9a;
            padding: 15px;
        }
    </style>
    <script>
       <c:if test="${'preView'!= preView}">
        window.onload=function(){
            doPrint();
        }
       </c:if>
    </script>
</head>
<body>
<a class="easyui-linkbutton" onclick="doPrint()">打印</a>
<a class="easyui-linkbutton" onclick="doPrint(2)">横向打印</a>
 <div id="page1" align="center"  style="width: 100%" >
     <%@include file="data_list_view.jsp"%>
 </div>
 <%@include file="/include/print/print.jsp" %>
</body>
</html>