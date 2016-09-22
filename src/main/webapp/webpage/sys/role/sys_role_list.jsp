<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/include/include.jsp" %>
<c:set var="actionUrl">
    ${pageContext.request.contextPath}/sys/role.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>角色管理</title>
    <%@include file="/include/head.jsp" %>
    <script type="text/javascript">
        var context = '${ pageContext.request.contextPath}';
        $(function () {
            getDataList();
        })//初始化
        //获取数据
        function getDataList() {
            ajaxJson("${ actionUrl}?gettree", {}, function (data) {

                $("#roleTree").tree("loadData", data);

            });
        }
        //打开更新对话框
        function showUpdateDia() {
            var row = $("#roleTree").tree("getSelected");
            if (!row) {
                alertMsg("请选择条目");
                return false;
            }
            if (row.length > 1) {
                alertMsg("只能选择一条!");
                return false;
            }
            var pNode = $("#roleTree").tree("getParent", row.target);
            $("#editForm").form("clear");
            ajaxJson("${actionUrl}?get", {id: row.id}, function (data) {
                if (data) {
                    $("#pid").combotree("reload");
                    if (pNode)
                        $("#pid").combotree("setValue", pNode.id);
                    $("#editForm").form("load", data);
                    $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
                }

            });
        }
        //批量删除
        function mulDelData() {
            var rows = $("#roleTree").tree("getSelected");
            if (!rows) {
                alertMsg("请选择条目");
                return false;
            }
            $.messager.confirm("提示", "确定删除", function (d) {
                if (d) {
                    var node = $("#roleTree").tree("getSelected");
                    var childNode = $("#roleTree").tree("getChildren", node.target);
                    if (childNode.length > 0) {
                        alertMsg("当前角色有下级，不能删除！");
                        return false;
                    }
                    ajaxJson("${actionUrl}?del", {id: rows.id}, function (data) {
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
                }

            });
        }
        //添加条目
        function addItem() {
            var node = $('#roleTree').tree('getSelected');
            $("#editForm").form("clear");
            $("#pid").combotree("reload");
            if (node) {
                if (node.id != 0)
                    $("#pid").combotree("setValue", node.id);
            }
            $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
        }
        //保存
        function saveData() {
            if (!$("#editForm").form("validate"))
                return false;
            ajaxJson("${ actionUrl}?save", $("#editForm").serialize(), function (data) {
                closeDiaWithMsgWhenSuccess("editDia", data);
                getDataList();
            });
        }
        //查看授权用户
        function showUser() {
            var rows = $("#roleTree").tree("getSelected");
            if (!rows) {
                alertMsg("请选择条目");
                return false;
            }
            ajaxJson("${pageContext.request.contextPath}/sys/user.do?role_user", {roleId: rows.id}, function (data) {
                $("#roleUserTable").datagrid("loadData", data);
                $("#roleUserRoleId").val(rows.id);
                openEasyuiDialog("roleUserDia");
            });

        }
        /**
         *重新加载用户角色关系表
         * @param roleId
         */
        function reloadRoleUser(roleId) {
            ajaxJson("${pageContext.request.contextPath}/sys/user.do?role_user", {roleId: roleId}, function (data) {
                $("#roleUserTable").datagrid("loadData", data);
            });
        }
        //删除用户的角色权限
        function delRoleUser() {
            var roleId = $("#roleUserRoleId").val();
            var checkedRow = $("#roleUserTable").datagrid("getChecked");
            if (checkedRow.length == 0) {
                alertMsg("请选择条目");
            }
            var userIds = [];

            for (var i = 0; i < checkedRow.length; i++) {
                userIds.push(checkedRow[i].id);
            }
            ajaxJson("${actionUrl}?delroleuser", $.param({userIds: userIds, roleId: roleId}, true), function (data) {
                reloadRoleUser($("#roleUserRoleId").val());
                slideMsgInfo(data.msg);
            })
        }
        //向角色中添加用户
        function addRoleUser() {
            ajaxJson("${pageContext.request.contextPath}/sys/user.do?allusers", {}, function (data) {
                $("#userTable").datagrid("loadData", data);
                openEasyuiDialog("userDia")
            });
        }
        function saveUser() {
            var rows = $("#userTable").datagrid("getChecked");
            if (rows.length == 0) {
                alertMsg("请选择要授权的用户");
                return false;
            }
            var data = [];
            var roleId = $("#roleUserRoleId").val();
            for (var i = 0; i < rows.length; i++) {
                data.push({roleId: roleId, userId: rows[i].id});
            }
            var jsonData = JSON.stringify(data);
            ajaxJson("${pageContext.request.contextPath}/sys/role.do?saveroleuser", {jsonData: jsonData}, function (data) {
                reloadRoleUser(roleId);
                closeDiaWithMsgWhenSuccess("userDia", data);
            });
        }
        //查看菜单
        function showMenu() {
            var rows = $("#roleTree").tree("getSelected");
            if (!rows) {
                alertMsg("请选择条目");
                return false;
            }
            $("#menu_roleId").val(rows.id);
            ajaxJson("${actionUrl}?roleresourcetree", {roleId: rows.id}, function (data) {
                $("#menuTree").tree("loadData", data);
                openEasyuiDialog("menuDia");
            });
        }
        /**
         *保存权限信息
         */
        function saveResurce() {
            var nodes = $("#menuTree").tree("getChecked");
            var roleId = $("#menu_roleId").val();
            var data = [];
            for (var i = 0; i < nodes.length; i++) {
                data.push({roleId: roleId, menuId: nodes[i].id});
            }
            var jsonData = JSON.stringify(data);
            ajaxJson("${actionUrl}?saveresource", {jsonData: jsonData, roleId: roleId}, function (data) {
                slideMsgInfo(data.msg);

            });
        }
        //保存按钮权限
        function saveButtonResource() {
            var rows = $("#buttonTable").datagrid("getChecked");
            var roleId = $("#menu_roleId").val();
            var button_method = "";
            for (var i = 0; i < rows.length; i++) {
                button_method += rows[i].buttonCode + ":" + rows[i].methodName + ";";
            }
            var menuId = $("#button_menuId").val();
            ajaxJson("${actionUrl}?updatebutton", {
                button_method: button_method,
                roleId: roleId,
                menuId: menuId
            }, function (data) {
                slideMsgInfo(data.msg);
                ajaxJson("${actionUrl}?roleresourcetree", {roleId: roleId}, function (data) {
                    $("#menuTree").tree("loadData", data);
                });
            });

        }
        function getButtons(node) {
            ajaxJson("${pageContext.request.contextPath}/sys/menu.do?getbuttons", {menuId: node.id}, function (data) {
                $("#buttonTable").datagrid("loadData", data);
                $("#button_menuId").val(node.id);
                var button_methods = node.attributes["button_method"];
                var rows = $("#buttonTable").datagrid("getRows");
                if (button_methods && data.length > 0) {
                    var button_method = button_methods.split(";");
                    for (var i = 0; i < button_method.length; i++) {
                        var button = button_method[i].split(":")[0];
                        for (var y = 0; y < rows.length; y++) {
                            if (rows[y].buttonCode == button) {
                                $("#buttonTable").datagrid("checkRow", $("#buttonTable").datagrid("getRowIndex", rows[y]));
                            }
                        }
                    }
                }
            });
        }
    </script>
</head>
<body class="body_s">

<div class="easyui-panel" style="width:900px;height: 650px" fit="true">

    <div id="tools" class="list_btn" style="height:40px;border-bottom:1px solid #bebebe;margin:0 0 15px 0;">
        <ul>
            <li class="list_btn_name_role">
                &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px;">角色管理</b>
            </li>
            <li id="tools1" class="list_a_role">
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="addItem()">添加</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit"
                   onclick="showUpdateDia()">修改</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove"
                   onclick="mulDelData()">删除</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search"
                   onclick="showUser()">授权用户</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search"
                   onclick="showMenu()">查看权限</a>
            </li>
        </ul>
    </div>
    <ul id="roleTree" class="easyui-tree"></ul>
</div>

<div id="editDia" title="更新" class="easyui-dialog" data-options="closed:true,width:600,height:400">
    <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table style="width:90%">
            <tr>
                <td height="20"></td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right" width="45%">上级角色：</td>
                <td style="padding: 5px">
                    <input name='pid' id="pid" class="easyui-combotree" data-options="url:'${actionUrl}?gettree'"/>
                </td>
            </tr>

            <tr>
                <td style="padding: 5px" align="right">角色编号：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='roleCode'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">角色名称：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='roleName'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">角色描述：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='roleDesc'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">排序号：</td>
                <td style="padding: 5px">
                    <input class='easyui-numberbox' name='xh'/>
                </td>
            </tr>
            <tr style="display: none">
                <td style="padding: 5px" align="right">允许上级拥有本角色权限：</td>
                <td style="padding: 5px">
                    <input type='checkbox' name='preThrough' value='1'/>
                </td>
            </tr>
            <tr>
                <td height="20"></td>
            </tr>
            <tr align="center">
                <td colspan="2" align="right"><a href="javascript:void(0);" class="easyui-linkbutton"
                                                 onclick="saveData()">提交</a></td>
            </tr>
        </table>
    </form>
</div>
<div style="display: none">
    <div id="roleUserDia" align="center" title="授权用户信息" class="easyui-dialog"
         data-options="closed:true,width:600,height:400">
        <table id="roleUserTable" class="easyui-datagrid" style="width:598px;height:360px"
               data-options="toolbar:'#pertools'">
            <thead>
            <tr align="center">
                <th field="id" data-options="checkbox:true"></th>
                <th field="username" data-options="width:150">用户名</th>
                <th field="realname" data-options="width:150">昵称</th>
                <th field="dep" data-options="width:150,formatter:function(value,row,index){return value.depName; }">
                    所属部门
                </th>
            </tr>
            </thead>
        </table>
        <input type="hidden" id="roleUserRoleId"/>
    </div>
    <div id="pertools" style="padding:10px 0 5px 0;height:40px">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="addRoleUser()">增加</a>
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="delRoleUser()">删除</a>
    </div>
    <div id="userDia" align="center" title="授权用户信息" class="easyui-dialog" data-options="closed:true,width:600,height:400">
        <table id="userTable" class="easyui-datagrid" style="width:580px;height:340px">
            <thead>
            <tr align="center">
                <th field="id" data-options="checkbox:true"></th>
                <th field="username" data-options="width:150">用户名</th>
                <th field="realname" data-options="width:150">昵称</th>
                <th field="dep" data-options="width:150,formatter:function(value,row,index){return value.depName; }">
                    所属部门
                </th>
            </tr>
            </thead>
        </table>
        <div align="center">
            <a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveUser()">授权</a>
        </div>
    </div>
    <div id="menuDia" align="center" title="权限信息" class="easyui-dialog" data-options="closed:true,width:800,height:450">
        <div class="easyui-layout" style="width: 798px;height: 300px;">
            <div data-options="region:'center'" style="width:180px;height:300px;">
                <input type="hidden" id='menu_roleId'/>
                <ul style="padding:0;margin:0">
                    <li class="list_btn_name"
                        style="border-bottom:1px solid #d8d8d8;padding:10px 0 10px 5px;margin:0 0 10px 0;">
                        &nbsp;&nbsp;<b class="head_font">菜单信息</b>
                    </li>
                </ul>
                <ul id="menuTree" class="easyui-tree"
                    data-options="checkbox:true,cascadeCheck:false,onClick:function(node){if(node.checked){getButtons(node);}}"></ul>

            </div>
            <div data-options="region:'east',split:false" title="按钮信息" style="width:420px;height:200px;">

                <table id="buttonTable" class="easyui-datagrid" data-options="toolbar:'#buttonTools'"
                       style="height: 200px;">
                    <thead>
                    <tr align="center">
                        <th field="id" data-options="checkbox:true"></th>
                        <th field="menuId" data-options="hidden:true"></th>
                        <th field="buttonName" width="120">按钮名称</th>
                        <th field="buttonCode" width="120">按钮编码</th>
                        <th field="methodName" width="120">响应方法</th>
                    </tr>
                    </thead>
                </table>
                <input type="hidden" id='button_menuId'/>
            </div>
        </div>
        <div align="center" style="margin:10px 0 0 0;border-top:1px solid #d8d8d8;padding:10px 0 0 10px;">
            <a href="javascript:void(0);" style="float:left" class="easyui-linkbutton"
               onclick="saveResurce()">保存菜单权限</a>
        </div>
    </div>
    <div id="buttonTools" style="padding:10px 0 10px 0;">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save" onclick="saveButtonResource()">保存按钮权限</a>
    </div>
</div>
</body>
</html>
