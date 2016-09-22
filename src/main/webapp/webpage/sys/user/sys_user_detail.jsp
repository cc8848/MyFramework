<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/user.do
</c:set>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        window.onload=(function(){
                $("#editForm").form("reset");
            var id='${ param.id}';
            if(jQuery.trim(id).length>0){
                ajaxJson("${ actionUrl}?get",{id:id},function(data){
                    if(data){
                            data["depName"]=data.dep.depName;
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
    <table width="60%" align="center">
         <tr>
                    <td style="padding: 5px" width="20%" align="right">部门：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox' disabled='disabled' name='depName'  />
                    </td>
                </tr>
                                                <tr>
                    <td style="padding: 5px" align="right">用户名：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox' disabled='disabled'  name='username'  />
                    </td>
                </tr>
                                                <tr>
                    <td style="padding: 5px" align="right">密码：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox' disabled='disabled'  name='passwd'  />
                    </td>
                </tr>
                <tr>
                    <td style="padding: 5px" align="right">昵称：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox' disabled='disabled'  name='realname'  />
                    </td>
                </tr>
                <tr>
                    <td style="padding: 5px" align="right">用户类型：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox' disabled='disabled'  name='realname'  data-options="url:'${pageContext.request.contextPath}/sys/dic.do?dicList&dicKey=dic_key:dic_type=\'userType\'' , method: 'get', valueField:'cKey',textField:'cValue',panelHeight:'auto'"/>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 5px" align="right">状态：</td>
                    <td style="padding: 5px">
                        <input class='easyui-combobox' disabled='disabled' name='state'  data-options="valueField:'value',textField:'text',data:[{'value':'1','text':'正常'},{'value':'2','text':'已删除'}]"  style="width:200px;height:25px;border:1px solid #c1c1c1;"/>
                    </td>
                </tr>


            </table>
</form>
</div>
</body>
</html>
