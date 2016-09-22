<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
   ${pageContext.request.contextPath}/sys/template.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>基础模板管理</title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        var context='${pageContext.request.contextPath}';
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
            showBussinessWindow("更新","${pageContext.request.contextPath}/webpage/sys/base_template/template_update.jsp?id="+row[0].id);
        }
        //打开新增对话框
        function showAddDia(){
            showBussinessWindow("新增","${pageContext.request.contextPath}/webpage/sys/base_template/template_update.jsp");
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
            showBussinessWindow("详请","${pageContext.request.contextPath}/webpage/sys/base_template/template_detail.jsp?id="+row[0].id);
        }
        //操作按钮
        function buttonCz(value,row,index){
            return "<a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        }
        function showCodeConfig(){
            var row=$("#dataTable").datagrid("getChecked");
            if(row.length==0){
                alertMsg("请选择条目");
                return false;
            }
            if(row.length>1){
                alertMsg("只能选择一条!");
                return false;
            }
            $("#codeForm").form("reset");
            $("#code_id").val(row[0].id);
            $("#codDia").dialog("open");
        }
        function genereateCode(){
            ajaxJson("${actionUrl}?genereatecode",$("#codeForm").serialize(),function(data){
                closeDiaWithMsgWhenSuccess("codDia",data);
            });
        }
    </script>
</head>
<body class="body_s">
    <div class="easyui-layout" title="模板信息" style="width: 900px;height: 650px" fit="true">
        <div data-options="region:'north'" class="search_frame"  align="center" >
            <div class="easyui-panel"  fit="true">
            <form id="searchForm">
                <input type="hidden" name="pageNum" id="pageNum" value="1" />
                <table>
					<tr>
						<td class="head_search" colspan="4">
							<img src="${pageContext.request.contextPath}/plug-in/main/images/icon_aj_search.png" style="vertical-align:middle"/>&nbsp;&nbsp;<b class="head_font">模板信息查询</b>
						</td>
					</tr>
                    <tr>
                        <td align="right">模板名称：</td><td><input class="input_arch_sea" class='easyui-validatebox textbox' name='tableName'  /></td>
                        <td align="right">模板描述：</td><td><input class="input_arch_sea" class='easyui-validatebox textbox' name='tableContent'  /></td>
                    </tr>

                    <tr>
                        <td colspan="4">&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="4">
                            <a href="javascript:void(0);" class="btn_search" class="easyui-linkbutton" onclick="getDataList()">查询</a>&nbsp;&nbsp;<a href="javascript:void(0);"  class="btn_reset" class="easyui-linkbutton"   onclick="getDataList()">重&nbsp;置</a>
                        </td>
                    </tr>
               </table>
            </form>
          </div>
        </div>
        <div data-options="region:'center'" style="height:400px;margin:20px 0 0 0;" align="center" fit="true">
            <div class="data_list">
            <table id="dataTable" class="easyui-datagrid"   align="center" data-options="toolbar:'#tools',height:350" fit="true">
                <thead>
                <tr>
                    <th field="id" data-options="checkbox:true,width:200"></th>
                    <th field='tableName' data-options="width:200" >模板名称</th>
                    <th field='tableContent' data-options="width:200"  >模板描述</th>
                    <th field="cz"   data-options="width:100,formatter: function(value,row,index){ return buttonCz(value,row,index);}" >操作</th>
                </tr>
                </thead>
            </table>
            </div>
            <div class="easyui-panel" style="height: 38px;">
                <div id="pager" class="easyui-pagination" data-options="showRefresh:false,showPageList:false,total:0,onSelectPage: function(pageNumber, pageSize){ $('#pageNum').val(pageNumber); getDataList();}"></div>
            </div>
        </div>
    </div>


<div id="tools" class="list_btn">
	<ul>
		<li class="list_btn_name_role">
			<img src="${pageContext.request.contextPath}/plug-in/main/images/icon_aj_list.png" style="vertical-align:middle"/>&nbsp;&nbsp;<b class="head_font">数据列表</b>
		</li>
		<li class="list_a_role">
			<a href="javascript:void(0);"  class="easyui-linkbutton" iconCls="icon-add" onclick="showAddDia()"><img src="${pageContext.request.contextPath}/plug-in/main/images/icon_list_add.png" style="vertical-align:middle"/>添加</a>
			<a href="javascript:void(0);"  class="easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateDia()"><img src="${pageContext.request.contextPath}/plug-in/main/images/icon_list_upd.png" style="vertical-align:middle"/>修改</a>
			<a href="javascript:void(0);"  class="easyui-linkbutton" iconCls="icon-remove" onclick="mulDelData()"><img src="${pageContext.request.contextPath}/plug-in/main/images/icon_list_del.png" style="vertical-align:middle"/>批量删除</a>
			<a href="javascript:void(0);"  class="easyui-linkbutton" iconCls="icon-edit" onclick="showDetail()"><img src="${pageContext.request.contextPath}/plug-in/main/images/icon_list_detail.png" style="vertical-align:middle"/>查看详情</a>
		
		</li>
	</ul>

</div>
<div id="editDia" title="更新" class="easyui-dialog" data-options="closed:true,width:900,height:600">
    <iframe scrolling="auto" id="editFrame"  frameborder="0" onload="iFrameHeightSize('editFrame','editDia',0)" src="" style="width:100%;height:100%;"></iframe>
</div>

<div id="detailDia" title="详情" class="easyui-dialog" data-options="closed:true,width:900,height:600">
    <iframe scrolling="auto" id="detailFrame"  frameborder="0"  onload="iFrameHeightSize('detailFrame','detailDia',0)" src="" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
