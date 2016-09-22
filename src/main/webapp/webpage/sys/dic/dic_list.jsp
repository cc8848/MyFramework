<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/dic.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>字典管理</title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        window.onload=(function(){ getDataList();})//初始化
        //获取数据列表
        function getDataList(){
            ajaxJson("${actionUrl}?datagrid",$("#searchForm").serialize(),function(data){
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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/dic/dic_update.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","更新数据");
            openEasyuiDialog("editDia");
          //  showBussinessWindow("更新","${pageContext.request.contextPath}/webpage/sys/dic/dic_update.jsp?id="+row[0].id);
        }
        //打开新增对话框
        function showAddDia(){
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/dic/dic_update.jsp");
            $("#editDia").dialog("setTitle","新增");
            openEasyuiDialog("editDia");
           // $("#editDia").dialog("href","/webpage/sys/dic/dic_update.jsp").dialog("open");
            //showBussinessWindow("新增","${pageContext.request.contextPath}/webpage/sys/dic/dic_update.jsp");
        }
        //删除
        function delData(id){
            $.messager.confirm("提示","确定删除",function(r){
               if(r){
                    ajaxJson("${actionUrl}?del",{id:id},function(data){
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
               }
            });

        }
        //批量删除
        function mulDelData(){
            var rows=$("#dataTable").datagrid("getChecked");
            if(rows.length==0){
                alertMsg("请选择条目");
                return false;
            }
            $.messager.confirm("提示","确定删除选中的"+rows.length+"条",function(r){
                if(r){
                    var ids=new Array();
                    for( var i=0;i<rows.length;i++){
                        ids.push(rows[i].id);
                    }
                    ajaxJson("${actionUrl}?muldel", $.param({ids:ids},true),function(data){
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
                }
            })

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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/dic/dic_update.jsp");
            $("#editDia").dialog("setTitle","详情");
            openEasyuiDialog("editDia");
            //$("#detailDia").dialog("open").dialog("reload","${actionUrl}?detail&id="+row[0].id);
            showBussinessWindow("详情","${actionUrl}?detail&id="+row[0].id);
        }
        //操作按钮
        function buttonCz(value,row,index){
                return "<a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        }

    </script>
</head>
<body class="easyui-layout" title="字典管理" style="width: 900px;height: 650px" fit="true">
<div data-options="region:'north'" align="center" style="overflow:hidden;">
    <form id="searchForm">
        <input type="hidden" name="pageNum" id="pageNum" value="1"/>
        <table width="100%">
            <tr height="20">
                <td class="head_search" colspan="20" style="text-align:left;height:20px;padding:10px 0 0 0;">
                    &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据查询</b>
                </td>
            </tr>
            <tr height="30">
                <td width="10%"></td>
                <td width="10%"> &nbsp;&nbsp;&nbsp;&nbsp;字典分类：</td>
                <td align="left"><input  name="dicTypeDesc" class="easyui-validatebox"/></td>
                <td>字典值：</td>
                <td><input name="dicValue" class="easyui-validatebox"/></td>
                <td width="10%"></td>
            </tr>

            <tr height="30">
                <td align="center" colspan="20" nowrap style="padding:0 0 15px 0;border-bottom:1px solid #d8d8d8">
                    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="getDataList(true)">查&nbsp;询</a>&nbsp;&nbsp;<a
                        href="javascript:void(0);" class="easyui-linkbutton" onclick="getDataList()">重&nbsp;置</a>
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
            <th field="id" data-options="checkbox:true,width:80"></th>
            <th field="dicTypeDesc" data-options="width:150">字典分类</th>
            <th field="dicType" data-options="width:150">字典分类编码</th>
            <th field="dicValue" data-options="width:200">字典值</th>
            <th field="dicKey" data-options="width:150">字典编码</th>
            <th field="cz"
                data-options="width:80,formatter: function(value,row,index){ return buttonCz(value,row,index);}">操作
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
</div>
<div id="editDia" title="更新字典" class="easyui-dialog" data-options="closed:true,width:500,height:400">
<iframe scrolling="auto" id="editFrame"  frameborder="0"   src="" style="width:99%;height:99%;"></iframe>
</div>

<div id="detailDia" title="详情" class="easyui-dialog" data-options="closed:true,width:500">
    <iframe scrolling="auto" id="detailFrame"  frameborder="0"  src="" style="width:99%;height:99%;"></iframe>
</div>
</body>
</html>
