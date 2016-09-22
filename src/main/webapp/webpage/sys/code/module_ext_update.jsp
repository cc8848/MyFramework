<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/business/module_ext.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>扩展组件管理更新</title>
    <%@include file="/include/head.jsp"%>
    <%@include file="/include/code/one2many/view_ext.jsp" %>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/common_method.js" ></script>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/one2many/method.js" ></script>
    <script type="text/javascript" >
        var datagridIdArry=new Array();
        datagridIdArry.push('moduleExtDetails');
        window.onload=(function(){
            var id='${ param.id}';
            loadData(id);
        });
        function loadData(id){
           $("#editForm").form("reset");
            if(jQuery.trim(id).length>0){
                ajaxJson("${ actionUrl}?get",{id:id},function(data){
                    if(data){
                            $("#editForm").form("load",data);
                                ajaxJson("${actionUrl}?moduleExtDetails",{"moduleExtEntity.id":id},function(data1){
                                        $("#moduleExtDetails").datagrid("loadData",data1);
                                });

                    }
                });
            }
        }

        //子表文件上传按钮
        function list_file_upload(id,name,val,showType){
            return "<a href=\"#\"  onclick=\"fileList_sub('"+id+"','"+name+"','"+val+"','"+showType+"')\">[附件]</a>";
        }
        function setFiles(datagridId,editIndex){
            var combobox=getEasyuiDatagridEditor(datagridId,editIndex,"moduleLink").target;
            ajaxJsonWithCache("${actionUrl}?getModulesFiles",{},function(data){
                combobox.combobox("loadData",data);
            });
        }
        //添加条目
        function addItem(datagridId){
            $('#'+datagridId).datagrid('appendRow',{});
            var  editIndex = $('#'+datagridId).datagrid('getRows').length-1;
            $('#'+datagridId).datagrid('beginEdit', editIndex);
            setFiles(datagridId,editIndex);
        }
        /**
         *编辑行
         **/
        function editItem(daId){
            var rows=$('#'+daId).datagrid("getChecked");
            if(rows.length==0){
                alertMsg("请选择条目");
                return false;
            }
            for(var i=0;i<rows.length;i++){
                var index= $('#'+daId).datagrid('getRowIndex', rows[i]);
                $('#'+daId).datagrid('beginEdit', index);
                setFiles(daId,index);
            }
        }
    </script>
</head>
<body>
<div class="easyui-layout"  fit="true"  >
   <div data-options="region:'north'" style="height: 60px;" align="center"  >
    <form id="editForm">
    <input name="id" type="hidden" id="edit_id">
    <table align="center">
    <tr>
                <td style="padding: 5px">组件名称：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox'   name='moduleName'  />
                </td>
                 <td style="padding: 5px">组件代号：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox'   name='moduleCode'  />
                </td>
                 <td style="padding: 5px">默认显示：</td>
                <td style="padding: 5px">
                    <select class="easyui-combobox"   name='defaultShow'>
                        <option value="1">是</option>
                        <option value="">否</option>
                    </select>
                </td>
                 </tr>
                <tr>
    </tr>
    </table>
    </form>
</div>
    <div data-options="region:'south'" style="height: 30px;"  align="center" >
        <a href="javascript:void(0);"    class=" easyui-linkbutton" onclick="saveData()">保存</a>
    </div>
<div  data-options="region:'center'" >
<div id="subListDiv" class="easyui-tabs" >
    <div  title="扩展组件详情">
        <table id="moduleExtDetails"   class="easyui-datagrid" data-options="toolbar:'#tb1'">
            <thead>
                <th field="id" data-options="checkbox:true"></th>
                <th field='moduleLink' data-options="width:270,editor:{type:'combobox',options:{required:true,valueField:'cKey',textField:'cKey'}}" >组件链接</th>
                <th field='moduleType' data-options="width:100,formatter:function(value,row,index){ if(value=='button') return '按钮'; else if(value=='view') return '视图'; else return ''; },editor:{type:'combobox',options:{valueField:'value',textField:'text',options:{required:true},data:[{'value':'button','text':'按钮'},{'value':'view','text':'视图'}]}}" >组件类型</th>
            </thead>
        </table>


    </div>
    <div id="tb1">
        <a href="javascript:void(0)" class=" easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addItem('moduleExtDetails')">增加</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editItem('moduleExtDetails')">修改</a>
        <a href="javascript:void(0)" class=" easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteItem('moduleExtDetails')">删除</a>
    </div>
</div>

</div>
</div>
<div id="fileDia" title="浏览文件" class="easyui-dialog" data-options="closed:true,width:800,height:500">
    <iframe scrolling="auto" id="fileFrame"  frameborder="0"   src="" style="width:100%;height:100%;"></iframe>
</div>
</body>
</html>
