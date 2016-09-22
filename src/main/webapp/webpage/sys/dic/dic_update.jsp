<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp" %>
<c:set var="actionUrl">
    ${pageContext.request.contextPath}/sys/dic.do
</c:set>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp" %>
    <script type="text/javascript">
        window.onload = function () {
            $("#editForm").form("reset");
            var id = '${param.id}';
            if ($.trim(id).length > 0) {
                ajaxJson("${actionUrl}?get", {id: id}, function (data) {
                    if (data) {
                        $("#editForm").form("load", data);
                    }
                });
            }
        };
        //保存
        function saveData() {
            if (!$("#editForm").form("validate"))
                return false;
            ajaxJson("${actionUrl}?save", $("#editForm").serialize(), function (data) {
                window.parent.closeDiaWithMsgWhenSuccess("editDia", data);
                window.parent.getDataList();
            });
        }
    </script>
</head>
<body>
<div class="easyui-layout" fit="true">
    <form id="editForm" class="dForm">
        <input name="id" type="hidden" id="edit_id">
        <table style="width:95%">
            <tr>
                <td style="padding: 5px" align="right" width="40%">字典分类</td>
                <td style="padding: 5px"><input class="input_arch_sea" name="dicTypeDesc" class="easyui-validatebox"
                                                data-options="required:true">
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">字典分类编码：</td>
                <td style="padding: 5px"><input class="input_arch_sea" name="dicType" class="easyui-validatebox"
                                                data-options="required:true">
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">字典值：</td>
                <td style="padding: 5px"><input class="input_arch_sea" name="dicValue" class="easyui-validatebox"
                                                data-options="required:true"></td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">字典编码：</td>
                <td style="padding: 5px"><input class="input_arch_sea" name="dicKey" class="easyui-validatebox"
                                                data-options="required:true"></td>
            </tr>
            <tr>
                <td height="20"></td>
            </tr>
            <tr align="right">
                <td colspan="4"><a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveData()">提交</a></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
