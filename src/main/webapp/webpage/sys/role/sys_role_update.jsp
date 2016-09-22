<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/role.do
</c:set>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        $(function(){
            $("#editForm").form("reset");
            var id='${ param.id}';
            if(jQuery.trim(id).length>0){
                ajaxJson("${ actionUrl}?get",{id:id},function(data){
                    if(data){
                        $("#editForm").form("load",data);
                    }
                });
            }
        });
        //保存
        function saveData(){
            if(!$("#editForm").form("validate"))
                return false;
            ajaxJson("${ actionUrl}?save",$("#editForm").serialize(),function(data){
                window.parent.closeDiaWithMsgWhenSuccess("editDia",data);
                window.parent.getDataList();
            });
        }
    </script>
</head>
<body>
<div class="easyui-layout" fit="true">
<form id="editForm">
    <input name="id" type="hidden" id="edit_id">
    <table>
        <tr>
            <td style="padding: 5px">pid：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox'   name='pid'  />
            </td>
        </tr>
        <tr>
            <td style="padding: 5px">角色编号：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox'   name='roleCode'  />
            </td>
        </tr>
        <tr>
            <td style="padding: 5px">角色名称：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox'   name='roleName'  />
            </td>
        </tr>
        <tr>
            <td style="padding: 5px">角色描述：</td>
            <td style="padding: 5px">
                <input class='easyui-validatebox'   name='roleDesc'  />
            </td>
        </tr>
        <tr>
            <td style="padding: 5px">允许上级拥有本角色权限：</td>
            <td style="padding: 5px">
                <input type='checkbox'   name='preThrough' value='1' />
            </td>
        </tr>
        <tr align="center">
            <td colspan="2"><a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveData()">提交</a></td>
        </tr>
    </table>
</form>
</div>
</body>
</html>
