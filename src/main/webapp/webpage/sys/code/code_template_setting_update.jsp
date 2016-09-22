<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/code/setting.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <%@include file="/include/code/simple/view_ext.jsp" %>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/common_method.js" ></script>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/simple/method.js" ></script>
    <script type="text/javascript" >
        window.onload=(function(){
            var id='${ param.id}';
            loadFormData(id);
        });

    </script>
</head>
<body >
<div align="center">
<form id="editForm">
    <input name="id" type="hidden" id="edit_id">
    <table align="center">
        <tr>

            <td style="padding: 5px">生成类路径：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox'  style="width: 300px"  name='javaPath'  />
            </td>
        </tr>
        <tr>
            <td style="padding: 5px">生成视图路径：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox' style="width: 300px"  name='viewPath'  />
            </td>
         </tr>
         <tr>
        </tr>
        <tr align="center">
            <td colspan="4"><a href="javascript:void(0);"    class=" easyui-linkbutton" onclick="saveData()">提交</a></td>
        </tr>
    </table>
</form>
</div>
</body>
</html>
