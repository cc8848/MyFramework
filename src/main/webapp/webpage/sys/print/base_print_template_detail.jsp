<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/print.do
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

                    <td style="padding: 5px">模板名称：</td>
                    <td style="padding: 5px">
                        <input class='easyui-validatebox' disabled='disabled'  name='mName'  />
                    </td>

 
                    <td style="padding: 5px">表名：</td>
                    <td style="padding: 5px">
                        <input class='easyui-validatebox' disabled='disabled'  name='mTablename'  />
                    </td>

                </tr>
                <tr>
 
                    <td style="padding: 5px">字段key：</td>
                    <td style="padding: 5px">
                        <input class='easyui-validatebox' disabled='disabled'  name='mField'  />
                    </td>

 
                    <td style="padding: 5px">字段值：</td>
                    <td style="padding: 5px">
                        <input class='easyui-validatebox' disabled='disabled'  name='mValue'  />
                    </td>

                </tr>
                <tr>
         </tr>
    </table>
</form>
</div>
</body>
</html>
