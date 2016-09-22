<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>

</head>
<body>
<div class="easyui-layout" fit="true">
<table>
    <tr>
        <td style="padding: 5px">字典分类：</td>
        <td style="padding: 5px">${bean.dicTypeDesc}</td>
        <td style="padding: 5px">字典分类编码：</td>
        <td style="padding: 5px">${bean.dicType}
        </td>
    </tr>
    <tr>
        <td style="padding: 5px">字典值：</td>
        <td style="padding: 5px">${bean.dicValue}</td>
        <td style="padding: 5px">字典编码：</td>
        <td style="padding: 5px">${bean.dicKey}</td>
    </tr>
</table>
</div>
</body>
</html>
