<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp" %>
<c:set var="actionUrl">
    ${pageContext.request.contextPath}/sys/user.do
</c:set>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp" %>
    <script type="text/javascript">

        window.onload = (function () {
            $("#editForm").form("reset");
            var id = '${ param.id}';
            if (id != null && id.length != 0) {
                if (jQuery.trim(id).length > 0) {
                    ajaxJson("${ actionUrl}?get", {id: id}, function (data) {
                        if (data) {
                            $("#editForm").form("load", data);
                            $("#dep").combotree("setValue", data.dep.id);
                        }
                    });
                }
            }
        });
        //保存
        function saveData() {
            if (!$("#editForm").form("validate"))
                return false;
            ajaxJson("${ actionUrl}?save", $("#editForm").serialize(), function (data) {
                window.parent.closeDiaWithMsgWhenSuccess("editDia", data);
                window.parent.getDataList();
            });
        }
        //生成用户秘钥
        function generateUserKey() {
            ajaxJson("${actionUrl}?generateUserKey", {}, function (data) {
                if (assertAjaxSuccess(data))
                    $("#userKey").val(data);
            });
        }
    </script>
</head>
<body>
<div class="easyui-layout" fit="true">
    <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table width="80%" align="center" cellpadding="0" cellspacing="0" border="0" style="margin-top: 20px">
            <tr>
                <td style="padding: 5px" align="right">所属部门：</td>
                <td style="padding: 5px">
                    <input class='easyui-combotree' name='dep.id' id="dep"
                           data-options="url:'${pageContext.request.contextPath}/sys/dep.do?gettree',required:true"/>
                </td>
                <td style="padding: 5px" align="right">用户名：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='username'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">密码：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' type="password" name='passwd'/>
                </td>
                <td style="padding: 5px" align="right">昵称：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='realname'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">状态：</td>
                <td style="padding: 5px">
                    <input class='easyui-combobox' name='state' value="1"
                           data-options="valueField:'value',textField:'text',data:[{'value':'1','text':'正常'},{'value':'2','text':'已删除'}],required:true"/>
                </td>
                <td style="padding: 5px" align="right">用户类型：</td>
                <td style="padding: 5px">
                    <input class='easyui-combobox' name='userType'
                           data-options="url:'${pageContext.request.contextPath}/sys/dic.do?dicList&dicKey=dic_key:dic_type=\'userType\'' , method: 'get', valueField:'cKey',textField:'cValue',panelHeight:'auto',required:true"/>
                </td>
            </tr>
            <tr align="center">
                <td colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveData()">提交</a></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
