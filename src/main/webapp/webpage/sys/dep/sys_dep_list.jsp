<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
   ${pageContext.request.contextPath}/sys/dep.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>部门信息表</title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        var context='${ pageContext.request.contextPath}';
        $(function(){

            getDataList();})//初始化
        //获取数据列表
        function getDataList(){
            ajaxJson("${ actionUrl}?gettree",{},function(data){
                if(data){
                    $("#depTree").tree("loadData",data);

                }
            });
        }
        //打开更新对话框
        function showUpdateDia(){
            var row=$("#depTree").tree("getSelected");
            if(!row){
                alertMsg("请选择条目");
                return false;
            }
            if(row.length>1){
                alertMsg("只能选择一条!");
                return false;
            }
            var pNode=$("#depTree").tree("getParent",row.target);
            $("#editForm").form("clear");
            if(pNode)
                $("#pName").val(pNode.text);
            ajaxJson("${actionUrl}?get",{id:row.id},function(data){
                if(data){
                    $("#editForm").form("load",data);
                    $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
                }

            });
        }
        //打开新增对话框
        function showAddDia(){
            showFrameDialog("editDia","editFrame","${pageContext.request.contextPath}/webpage/sys/dep/sys_dep_update.jsp");
        }
        //批量删除
        function mulDelData(){
            var rows=$("#depTree").tree("getSelected");
            if(!rows){
                alertMsg("请选择条目");
                return false;
            }
            $.messager.confirm("提示","确定删除",function(d){
                if(d){
                    var node=$("#depTree").tree("getSelected");
                    var childNode=$("#depTree").tree("getChildren",node.target);
                    if(childNode.length>0){
                        alertMsg("当前部门有下级部门，不能删除！");
                        return false;
                    }
                    ajaxJson("${actionUrl}?del",{id:rows.id},function(data){
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
        function saveData(){
            if(!$("#editForm").form("validate"))
                return false;
            ajaxJson("${actionUrl}?save",$("#editForm").serialize(),function(data){
                closeDiaWithMsgWhenSuccess("editDia",data);
                getDataList();
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
            showFrameDialog("editDia","editFrame","${pageContext.request.contextPath}/webpage/sys/dep/sys_dep_detail.jsp?id="+row[0].id);
        }
        //操作按钮
        function buttonCz(value,row,index){
            return "<a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        }
        //添加条目
        function addItem(){
            var node=$('#depTree').tree('getSelected');
            $("#editForm").form("clear");
            if(node){
                $("#pName").val(node.text);
                $("#pid").val(node.id);
            }
            $("#editDia").dialog("open").dialog("vcenter").dialog("hcenter");
        }

    </script>
</head>
<body  class="body_s ">

    <div align="left"  class="easyui-panel" style="width: 950px;height: 600px" fit="true" >
		 <div class="list_btn" style="height:40px;border-bottom:1px solid #bebebe;margin:0 0 15px 0;">
			   <ul >
					<li class="list_btn_name" >
						&nbsp;&nbsp;<b class="head_font" style="font-size:13px;">部门信息列表</b>
					</li>
					<li id="tools" class="list_a" style="padding:0 10px 0 0;">
						 <a href="javascript:void(0);"    class="easyui-linkbutton" iconCls="icon-add"  onclick="addItem()">添加</a>
						 <a href="javascript:void(0);"   class="easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateDia()">修改</a>
						 <a href="javascript:void(0);"   class="easyui-linkbutton" iconCls="icon-remove"  onclick="mulDelData()">删除</a>
						 <%--    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" style="width: auto"  onclick="showDetail()">查看详情</a> --%> 
					</li>
			    </ul>
		   </div>
	 
        <ul id="depTree"  class="easyui-tree" ></ul>
    </div>
	


<div id="editDia" title="更新部门信息" class="easyui-dialog" data-options="closed:true,width:500">
    <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table style="width:90%">
	    	<tr>
				<td height="20"></td>
			</tr>
            <tr>
                <td style="padding: 5px" align="right">上级部门：</td>
                <td style="padding: 5px">
                    <input class="input_arch_sea" type="easyui-validatebox"  disabled  id="pName"  />
                    <input type="hidden"  name='pid' id="pid"  />
                </td>
            </tr>



            <tr>
                <td style="padding: 5px" align="right">部门名称：</td>
                <td style="padding: 5px">
                    <input class="input_arch_sea" class='easyui-validatebox'  data-options="required:true"  name='depName'  />
                </td>
            </tr>

            <tr>
                <td style="padding: 5px" align="right">部门ID：</td>
                <td style="padding: 5px">
                    <input class="input_arch_sea" type="easyui-validatebox"  name="depLogoId" />
                </td>
            </tr>

            <tr>
                <td style="padding: 5px" align="right">部门描述：</td>
                <td style="padding: 5px">
                    <input class="input_arch_sea" class='easyui-validatebox'   name='depDesc'  />
                </td>
            </tr>
            <tr>
                <td style="padding: 5px" align="right">部门排序：</td>
                <td style="padding: 5px">
                    <input class="input_arch_sea" class='easyui-numberbox'   name='xh'  />
                </td>
            </tr>
            <tr >
                <td colspan="4" align="right" ><a href="javascript:void(0);"    class="easyui-linkbutton" onclick="saveData()">提交</a></td>
            </tr>
			<tr>
				<td height="20"></td>
			</tr>
        </table>
    </form>
</div>

<div id="detailDia" title="部门详情" class="easyui-dialog" data-options="closed:true,width:500">
    <iframe scrolling="auto" id="detailFrame"  frameborder="0"  src="" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
