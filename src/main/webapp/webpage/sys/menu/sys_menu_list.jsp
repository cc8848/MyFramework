<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/include/include.jsp" %>
<c:set var="actionUrl">
    ${pageContext.request.contextPath}/sys/menu.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>菜单信息</title>
    <%@include file="/include/head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/ztree/css/demo.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/plug-in/ztree/css/zTreeStyle/zTreeStyle.css"
          type="text/css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/plug-in/ztree/js/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript">
        var context = '${ pageContext.request.contextPath}';
        $(function () {
            getDataList();
        })//初始化
        //获取数据列表
        function getDataList() {
            ajaxJson("${ actionUrl}?gettree", {}, function (data) {
                $.fn.zTree.init($("#treeDemo"), setting, data);
                /*$("#menuTree").tree("loadData",data);*/

            });
        }
        //打开更新对话框
        function showUpdateDia() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            if (rows.length > 1) {
                alertMsg("只能选择一个条目");
                return false;
            }
            var row = rows[0];
            $("#editForm").form("clear");
            ajaxJson("${actionUrl}?get", {id: row.id}, function (data) {
                if (data) {

                    $("#editForm").form("load", data);
                    $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
                    $("#pid").combotree("reload");
                    $("#pid").combotree("setValue", row.pid);
                }

            });
        }
        //批量删除
        function mulDelData() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            if (rows.length > 1) {
                alertMsg("只能选择一个条目");
                return false;
            }
            var row = rows[0];
            $.messager.confirm("提示", "确定删除", function (d) {
                if (d) {
                    var childNode = row.children;
                    if (childNode) {
                        alertMsg("当前菜单有下级菜单，不能删除！");
                        return false;
                    }

                    ajaxJson("${actionUrl}?del", {id: row.id}, function (data) {
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
                }

            });

        }
        /**
         *保存数据
         * @returns {boolean}
         */
        function saveData() {
            if (!$("#editForm").form("validate"))
                return false;
            ajaxJson("${actionUrl}?save", $("#editForm").serialize(), function (data) {
                closeDiaWithMsgWhenSuccess("editDia", data);
                getDataList();
            });
        }
        //添加条目
        function addItem() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length > 1) {
                alertMsg("只能选择一个父节点");
                return false;
            }
            var node = null;
            if (rows.length > 0)
                node = rows[0];
            $("#editForm").form("clear");
            $("#pid").combotree("reload");
            if (node) {
                if (node.id != 0)
                    $("#pid").combotree("setValue", node.id);
            }
            $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
        }
        /**
         *查看按钮权限
         */
        function showButtonPer() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            if (rows.length > 1) {
                alertMsg("只能选择一个条目");
                return false;
            }
            var row = rows[0];

            ajaxJson("${actionUrl}?getbuttons", {menuId: row.id}, function (data) {
                $("#perTable").datagrid("loadData", data);
                openEasyuiDialog("perDia");
            });
        }
        /**
         *保存按钮权限
         */
        function saveButtonPer() {
            endEditDatagridRow("perTable");
            var rows = $("#perTable").datagrid("getChanges", "inserted");
            var uprows = $("#perTable").datagrid("getChanges", "updated");
            rows = rows.concat(uprows);
            var jsonData = JSON.stringify(rows);
            ajaxJson("${actionUrl}?savebuttons", {jsonData: jsonData}, function (data) {
                slideMsgInfo(data.msg);
                $("#perTable").datagrid("acceptChanges");
                if (data.success)
                    $("#perDia").dialog("close");
            });
        }
        /**
         *增加按钮权限
         */
        function addButtonPer() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            if (rows.length > 1) {
                alertMsg("只能选择一个条目");
                return false;
            }
            $('#perTable').datagrid('appendRow', {menuId: rows[0].id});
            var editIndex = $('#perTable').datagrid('getRows').length - 1;
            $('#perTable').datagrid('beginEdit', editIndex);
        }
        /**
         *修改按钮权限
         */
        function editButtonPer() {
            var rows = $('#perTable').datagrid("getChecked");
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            editDatagridRow("perTable");
        }
        /**
         *删除按钮权限
         */
        function delButtonPer() {
            deleteDatagridRow("${actionUrl}?delbuttons", "perTable", function (data) {
                slideMsgInfo(data.msg);
            })
        }
        var setting = {
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        //获取ztreeobject
        function getZtreeObj() {
            return $.fn.zTree.getZTreeObj("treeDemo");
        }
        function zTreeOnClick(event, treeId, treeNode) {
            return false;
        }
        function moduleConfig() {
            var rows = getZtreeObj().getSelectedNodes();
            if (rows.length == 0) {
                alertMsg("请选择条目");
                return false;
            }
            $("#modulesDiv").empty();
            for (var i = 0; i < rows.length; i++) {
                $("#modulesDiv").append("<input type='hidden' name='tableName' value='" + rows[i].menuLink + "'/>");
            }
            $("#modulesForm").submit();
            openEasyuiDialog("moduleDia");
        }
        //关闭组件对话框
        function closeMoudules() {
            $("#moduleDia").dialog("close");
        }
    </script>
</head>
<body class="body_s">

<div align="left" class="easyui-panel" style="width: 950px;height: 600px" fit="true">

    <div id="tools2" class="list_btn" style="height:40px;border-bottom:1px solid #bebebe;margin:0 0 15px 0;">
        <ul>
            <li class="list_btn_name">
                &nbsp;&nbsp;<b class="head_font" style="font-size:13px;">菜单列表</b>
            </li>
            <li id="tools" class="list_a" style="padding:0 10px 0 0;">
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="addItem()">添加</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit"
                   onclick="showUpdateDia()">修改</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove"
                   onclick="mulDelData()">删除</a>
                <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="showButtonPer()">按钮权限</a>
                <%--<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="moduleConfig()">组件管理</a>--%>
            </li>
        </ul>
    </div>
    <ul id="treeDemo" class="ztree" style="height: 80%;"></ul>
    <%--<ul id="menuTree"  class="easyui-tree"  data-options="checkbox:true"></ul>--%>
</div>

<div id="editDia" title="更新" class="easyui-dialog" data-options="closed:true,width:600,height:400">
    <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table style="width:80%">
            <tr>
                <td height="20"></td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right" width="50%">上级菜单：</td>
                <td style="padding: 5px">
                    <input name='pid' id="pid" class="easyui-combotree" data-options="url:'${actionUrl}?gettree'"/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">菜单名称：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='text' data-options="required:true"/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td style="padding: 5px" align="right">图标：</td>--%>
            <%--<td style="padding: 5px">--%>
            <%--<input class='easyui-combobox'  name='pic'  data-options="valueField:'value',textField:'text',data:[--%>
            <%--{'value':'icon_xxtx.png','text':'消息'},--%>
            <%--{'value':'icon_xxwj.png','text':'文件框'},--%>
            <%--{'value':'icon_dbsx.png','text':'文件夹'},--%>
            <%--{'value':'icon_wjgl.png','text':'文档夹'},--%>
            <%--{'value':'icon_sjtj.png','text':'统计'},--%>
            <%--{'value':'icon_12.png','text':'回收站'},--%>
            <%--{'value':'icon_1.png','text':'系统设置'},--%>
            <%--{'value':'icon_0.png','text':'收藏夹'},--%>
            <%--{'value':'icon_2.png','text':'代码生成器'},--%>
            <%--{'value':'icon_6.png','text':'流程管理'},--%>
            <%--{'value':'icon_dajs.png','text':'档案检索'},--%>
            <%--{'value':'icon_kfgl.png','text':'库房管理'},--%>
            <%--{'value':'icon_5.png','text':'网上业务指导'},--%>
            <%--{'value':'icon_ztgl.png','text':'新闻'},--%>
            <%--{'value':'icon_dagl.png','text':'档案管理'},--%>
            <%--{'value':'icon_dajy.png','text':'档案借阅'},--%>
            <%--{'value':'icon_dygxb.png','text':'对应关系表'},--%>
            <%--{'value':'icon_lj.png','text':'立卷管理'},--%>
            <%--{'value':'icon_rzzl.png','text':'日终整理'},--%>
            <%--{'value':'icon_xxcj.png','text':'信息采集'},--%>
            <%--{'value':'icon_xxwj.png','text':'合卷管理'},--%>
            <%--{'value':'icon_ywlx.png','text':'业务类型管理'},--%>
            <%--{'value':'icon_wjccgl.png','text':'文件存储管理'}--%>
            <%--]" />--%>
            <%--</td>--%>
            <%--</tr>--%>
            <tr>
                <td style="padding: 5px" align="right">链接：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='menuLink'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">排序号：</td>
                <td style="padding: 5px">
                    <input class='easyui-numberbox' name='xh'/>
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">响应id：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' name='resid'/>
                </td>
            </tr>
            <%--<tr>--%>
            <%--<td style="padding: 5px" >菜单类型：</td>--%>
            <%--<td style="padding: 5px">--%>
            <%--<input class='easyui-combobox'  name='lx'  data-options="valueField:'value',textField:'text',data:[{'value':'text','text':'文本'},{'value':'input','text':'输入框'},{'value':'number','text':'数值'},{'value':'set','text':'系统菜单'}]" />--%>
            <%--</td>--%>
            <%--</tr>--%>
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
<div id="perDia" align="center" title="按钮权限" class="easyui-dialog" data-options="closed:true,width:600,height:450">
    <table id="perTable" class="easyui-datagrid" style="width: 598px;height:380px" data-options="toolbar:'#pertools'">
        <thead>
        <tr align="center">
            <th field="id" data-options="checkbox:true"></th>
            <th field="menuId" data-options="hidden:true"></th>
            <th field="buttonName" data-options="width:150,editor:'validatebox'">按钮名称</th>
            <th field="buttonCode" data-options="width:150,editor:'validatebox'">按钮编码</th>
            <th field="methodName" data-options="width:150,editor:'validatebox'">响应方法</th>
        </tr>
        </thead>
    </table>
    <div align="center">
        <a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveButtonPer()">保存</a>
    </div>
</div>
<div id="pertools" style="padding:10px 0 0 0;height:40px">
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="addButtonPer()">添加</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="editButtonPer()">修改</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="delButtonPer()">删除</a>
</div>
<div id="moduleDia" title="组件管理" class="easyui-dialog" style="width: 800px;height: 600px;" data-options="closed:true">
    <iframe scrolling="auto" id="moduleFrame" name="moduleFrame" frameborder="0" src=""
            style="width:99%;height:99%;"></iframe>
</div>
<div style="display: none">
    <form id="modulesForm" method="post"
          action="${pageContext.request.contextPath}/business/module_ext.do?getAllAndCheckedList" target="moduleFrame">
        <div id="modulesDiv" style="display: none">

        </div>
    </form>
</div>
</body>
</html>
