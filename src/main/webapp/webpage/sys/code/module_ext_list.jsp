<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
   ${pageContext.request.contextPath}/business/module_ext.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>扩展组件管理</title>
    <%@include file="/include/head.jsp"%>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/common_method.js" ></script>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/one2many/method.js" ></script>
    <script type="text/javascript" >
        var context='${ pageContext.request.contextPath}';
        window.onload=(function(){ getDataList();})//初始化


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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/module_ext_update.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","更新数据");
            openEasyuiDialog("editDia");
            // showBussinessWindow("更新","${pageContext.request.contextPath}/webpage/business/module_ext/module_ext_update.jsp?id="+row[0].id);
        }
        //打开新增对话框
        function showAddDia(){
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/module_ext_update.jsp");
            $("#editDia").dialog("setTitle","新增数据");
            openEasyuiDialog("editDia");
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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/module_ext_detail.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","数据详情");
            openEasyuiDialog("editDia");
            //showBussinessWindow("详情","${pageContext.request.contextPath}/webpage/business/module_ext/module_ext_detail.jsp?id="+row[0].id);
        }
        //操作按钮
        function buttonCz(value,row,index){
            var button="";
        <pro:authFilter buttonCode="del" actionUrl="${actionUrl}" >
                    button="<a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        </pro:authFilter>
            return button;
        }
        //下载按钮
        function showDownLoadFile(filePath){
            var button="";
        <pro:authFilter buttonCode="download" actionUrl="${actionUrl}" >
            if($.trim(filePath).length>0)
            button="<a href='javascript:void(0);' onclick=\"downLoadFile('"+filePath+"')\">[下载文件]</a>";
        </pro:authFilter>
            return button;
        }
        //查看文件按钮
        function showViewFile(value,row,fieldName,showType){
            var button="";
        <pro:authFilter buttonCode="viewfile" actionUrl="${actionUrl}" >
            if($.trim(value).length<=0)
            value="upload/"+row.id+"/"+fieldName;
            button="<a href='javascript:void(0);' onclick=\"showFileList('"+value+"','"+showType+"')\">[查看文件]</a>";
        </pro:authFilter>
            return button;
        }

    </script>
</head>
<body class="easyui-layout" title="扩展组件管理" style="width: 900px;height: 650px" fit="true">
        <div data-options="region:'north'" class="search_frame" align="center" >
            <form id="searchForm" method="post">
                <%--预定义条件--%>
                <c:forEach items="${_conditions}" var="_condition">
                    <input type="hidden" name="${_condition.key}"  value="${_condition.value}" />
                </c:forEach>
                <input type="hidden" name="pageNum" id="pageNum" value="1" />
                <input type="hidden" name="_tableName" id="_tableName" value="module_ext" />
                <table >
                    <tr>
                        <td>组件名称：</td><td><input class='easyui-validatebox'   name='moduleName'  /></td>
                        <td>组件代号：</td><td><input class='easyui-validatebox'   name='moduleCode'  /></td>
                        <td>默认显示：</td><td><input type='checkbox'   name='defaultShow' value='1' /></td>
                    </tr>
                    <tr>
                    </tr>
                    <tr>
                        <td colspan="6" align="center">
                            <a href="javascript:void(0);" class="btn_search" class="easyui-linkbutton" onclick="getDataList()">查询</a>
                            &nbsp;&nbsp;<a href="javascript:void(0);"  class="btn_reset" class="easyui-linkbutton"   onclick="resetForm()">重置</a>
                        </td>
                    </tr>
               </table>
            </form>
        </div>
        <div data-options="region:'center'" style="height:500px;margin:20px 0 0 0;" >
            <%@include file="module_ext_list_data_list_cache.jsp"%>
            <div class="easyui-panel" style="height: 38px;">
                <div id="pager" class="easyui-pagination" data-options="showRefresh:false,showPageList:false,total:0,onSelectPage: function(pageNumber, pageSize){ $('#pageNum').val(pageNumber); getDataList();}"></div>
            </div>
        </div>
<div id="tools" style="padding: 5px">
<%@include file="/include/code/business_button_core.jsp"%>
<%@include file="/include/code/business_button_ext.jsp"%>
</div>
<%@include file="/include/code/business_dialog.jsp" %>
<%@include file="/include/code/business_footer_ext.jsp" %>
</body>
</html>
