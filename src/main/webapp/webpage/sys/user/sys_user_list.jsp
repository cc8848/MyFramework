<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
   ${pageContext.request.contextPath}/sys/user.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>用户管理</title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        var context='${ pageContext.request.contextPath}';
        $(function(){ getDataList();})//初始化
        //获取数据列表
        function getDataList(){
            ajaxJson("${ actionUrl}?datagrid",$("#searchForm").serialize(),function(data){
                if(data){
                    $("#dataTable").datagrid("loadData",data.dataList);
                    $("#pager").pagination({total:data.totalCount,pageSize:data.pagesize,pageNumber:data.pageNum});
                }
            });
        }
        //打开更新对话框
        function showUpdateDia(){
            var row=$("#dataTable").datagrid("getChecked");
            if(row.length==0){
                alertMsg("请选择条目");
                return false;
            }
            if(row.length>1){
                alertMsg("只能选择一条!");
                return false;
            }
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/user/sys_user_update.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","新增数据");
            openEasyuiDialog("editDia");
        }
        //打开新增对话框
        function showAddDia(){
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/user/sys_user_update.jsp");
            $("#editDia").dialog("setTitle","新增数据");
            openEasyuiDialog("editDia");
        }
        //删除
        function delData(id){
            $.messager.confirm("提示","确定删除",function(d){
                if(d)
                    ajaxJson("${ actionUrl}?del",{id:id},function(data){
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
            });

        }
        //批量删除
        function mulDelData(){
            var rows=$("#dataTable").datagrid("getChecked");
            if(rows.length==0){
                alertMsg("请选择条目");
                return false;
            }
            jQuery.messager.confirm("提示","确定删除选中的"+rows.length+"条",function(r){
                if(r==true){
                    var ids=new Array();
                    for(var i=0;i<rows.length;i++){
                        ids.push(rows[i].id);
                    }
                    ajaxJson("${ actionUrl}?muldel",jQuery.param({ids:ids},true),function(data){
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
                }
            });

        }
        //查看详情
        function showDetail(){
            var row=$("#dataTable").datagrid("getChecked");
            if(row.length==0){
                alertMsg("请选择条目");
                return false;
            }
            if(row.length>1){
                alertMsg("只能选择一条!");
                return false;
            }
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/user/sys_user_detail.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","新增数据");
            openEasyuiDialog("editDia");
        }
        //操作按钮
        function buttonCz(value,row,index){
            return "<a href='javascript:void(0);' onclick=\"showRole('"+row.id+"')\">[角色信息]&nbsp;</a><a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        }
        //显示用户角色信息
        function showRole(userId){
            ajaxJson("${pageContext.request.contextPath}/sys/role.do?role_user_tree",{pid:0,userId:userId},function(data){
                $("#roleTree").tree("loadData",data);
                $("#role_userId").val(userId);
                openEasyuiDialog("roleDia");
            });
        }
        /**
         *保存授权信息
         */
        function saveRole(){
            var userId=$("#role_userId").val();
            var checkNodes=$("#roleTree").tree("getChecked");
            var data=[];
            for(var i=0;i<checkNodes.length;i++){
                data.push({userId:userId,roleId:checkNodes[i].id});
            }
            var jsonData=JSON.stringify(data);
            ajaxJson("${pageContext.request.contextPath}/sys/role.do?saveroleuser",{jsonData:jsonData,userId:userId},function(data){
                closeDiaWithMsgWhenSuccess("roleDia",data);
            });
        }
        //重置
        function resetForm(){
            $("#searchForm").form("reset");
        }
    </script>
</head>
<body class="easyui-layout" title="用户管理" style="width: 900px;height: 650px" fit="true">
    <div data-options="region:'north'" align="center" style="overflow:hidden;">
        <form id="searchForm">
            <input type="hidden" name="pageNum" id="pageNum" value="1"/>
            <table width="100%" >
                <tr height="20">
                    <td class="head_search" colspan="20" style="text-align:left;height:20px;padding:10px 0 0 0;">
                        &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据查询</b>
                    </td>
                </tr>
                <tr height="30">
                    <td width="10%" align="right">部门：</td>
                    <td align="left">
                        <input class='easyui-combotree' name='depId'
                               data-options="url:'${pageContext.request.contextPath}/sys/dep.do?gettree'"/>
                    </td>
                    <td width="10%" align="right">用户名：</td>
                    <td align="left"><input class='easyui-validatebox' name='username_'/></td>
                    <td width="10%" align="right">状态：</td>
                    <td><input class='easyui-combobox' name='state'
                               data-options="valueField:'value',textField:'text',data:[{'value':'1','text':'正常'},{'value':'2','text':'已删除'}]"/>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="6"
                        style="border-bottom:1px solid #d8d8d8;padding:5px 25px 10px 0;height:25px;">
                        <a href="javascript:void(0);" class="easyui-linkbutton" onclick="getDataList(true)">查询</a>&nbsp;&nbsp;
                        <a href="javascript:void(0);" class="easyui-linkbutton" onclick="resetForm()">重置</a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'center'" style="height:500px;" align="center">
        <table id="dataTable" class="easyui-datagrid" align="center" fit="true"
               data-options="toolbar:'#tools',onSortColumn:function(sort,order){$('#_order').val(sort+':'+order);getDataList();}">
            <thead>
            <tr>
                <th field="id" data-options="checkbox:true"></th>
                <th field='dep'
                    data-options="width:150,formatter:function(value,row,index){return row.dep.depName; }">所属部门
                </th>
                <th field='userType' data-options="width:150,editor:{type:'validatebox'},formatter: function(value,row,index){ return getDicValue('dic_key:dic_type=\'userType\'','','',value);}">用户类型</th>
                <th field='username' data-options="width:150,editor:{type:'validatebox'}">用户名</th>
                <th field='realname' data-options="width:150,editor:{type:'validatebox'}">昵称</th>
                <th field='state'
                    data-options="width:150,formatter:function(value,row,index){ if(value=='1') return '正常'; else if(value=='2') return '已删除'; else return ''; },editor:{type:'combobox',required:true,options:{valueField:'value',textField:'text',data:[{'value':'1','text':'正常'},{'value':'2','text':'已删除'}]}}">
                    状态
                </th>

                <th field="cz"
                    data-options="formatter: function(value,row,index){ return buttonCz(value,row,index);}">操作
                </th>
            </tr>
            </thead>
        </table>

    </div>
    <div  data-options="region:'south'" >
        <div class="easyui-panel" style="height: 40px;">
            <div id="pager" class="easyui-pagination" data-options="showRefresh:false,showPageList:false,total:0,onSelectPage: function(pageNumber, pageSize){ $('#pageNum').val(pageNumber); getDataList();}"></div>
        </div>
    </div>

<div id="tools" style="padding: 5px">
    <table width="100%">
        <tr height="20">
            <td class="head_search" colspan="20" style="text-align:left;padding:0 0 10px 0;">
                &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据列表</b>
            </td>
        </tr>
    </table>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="showAddDia()">添加</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateDia()">修改</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="mulDelData()">批量删除</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="showDetail()">详情</a>
</div>

<div id="editDia" title="更新" class="easyui-dialog" data-options="closed:true,width:800,height:500">
    <iframe scrolling="auto" id="editFrame"  frameborder="0"  src="" style="width:99%;height:99%;"></iframe>
</div>

<div id="detailDia" title="详情" class="easyui-dialog" data-options="closed:true,width:800,height:500">
    <iframe scrolling="auto" id="detailFrame"  frameborder="0"   src="" style="width:99%;height:99%;"></iframe>
</div>
<div style="display: none">
    <div id="roleDia" align="center" title="角色信息"  class="easyui-dialog" data-options="closed:true,width:600,height:400">
        <div style="height: 340px;">
            <input type="hidden" id="role_userId" name="role_userId"/>
            <ul id="roleTree"  class="easyui-tree" data-options="checkbox:true,cascadeCheck:false" ></ul>
        </div>
        <div align="center">
            <a href="javascript:void(0);" class="easyui-linkbutton" onclick="saveRole()">保存</a>
        </div>
    </div>
</div>
</body>
</html>
