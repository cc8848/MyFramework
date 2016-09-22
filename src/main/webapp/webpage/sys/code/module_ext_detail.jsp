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
                $("#editForm").form("reset");
            var id='${ param.id}';
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
        });
        //查看文件按钮
        function showViewFile(value,row,fieldName,showType){
            var button="";

            if($.trim(value).length<=0)
            value="upload/"+row.id+"/"+fieldName;
            button="<a href='#' onclick=\" showFileList('"+value+"','"+showType+"')\">[查看文件]</a>";

            return button;
        }
    </script>
</head>
<body>
<div class="easyui-layout" fit="true"  >
  <div data-options="region:'north'" class="search_frame" align="center"  >
    <form id="editForm">
    <input name="id" type="hidden" id="edit_id">
    <table align="center">
    <tr>
                <td style="padding: 5px">组件名称：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' disabled='disabled'  name='moduleName'  />
                </td>
                <td style="padding: 5px">组件代号：</td>
                <td style="padding: 5px">
                    <input class='easyui-validatebox' disabled='disabled'  name='moduleCode'  />
                </td>
                <td style="padding: 5px">默认显示：</td>
                <td style="padding: 5px">
                    <input type='checkbox' disabled='disabled'  name='defaultShow' value='1' />
                </td>
                </tr>
                <tr>
    </tr>
    </table>
    </form>
  </div>
  <div  data-options="region:'center'" >
    <div id="subListDiv" class="easyui-tabs"  >
            <div  title="扩展组件详情">
                <table id="moduleExtDetails"   class="easyui-datagrid" >
                    <thead>
                            <th field='moduleLink' data-options="width:270" >组件链接</th>
                            <th field='moduleType' data-options="width:100,formatter:function(value,row,index){ if(value=='button') return '按钮'; else if(value=='view') return '视图'; else return ''; }" >组件类型</th>
                    </thead>
                </table>
            </div>

    </div>

</div>
</body>
</html>
