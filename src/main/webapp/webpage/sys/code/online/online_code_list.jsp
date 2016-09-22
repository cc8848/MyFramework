<%@ page import="com.code.entity.TableHeadBean" %>
<%@ page import="java.util.List" %>
<%@ page import="com.code.entity.TableFieldBean" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.code.util.ViewUtil" %>
<%@ page import="com.code.util.ViewUtilFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" scope="request">
    ${pageContext.request.contextPath}/onlineCode/${tableName}.do
</c:set>
<%
    TableHeadBean headBean=(TableHeadBean)request.getAttribute("tableHead");
    List<TableFieldBean> fieldBeanList=new ArrayList<TableFieldBean>(0);
    if(headBean!=null){
        fieldBeanList=headBean.getFields();
    }
    if(fieldBeanList==null){
        fieldBeanList=new ArrayList<TableFieldBean>(0);
    }
    ViewUtil viewUtil= ViewUtilFactory.getInstance("easyui");
%>
<!DOCTYPE html>
<html>
<head>
    <title>数据列表</title>
    <%@include file="/include/head.jsp"%>
    <%@include file="/include/code/one2many/view_ext.jsp" %>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/common_method.js" ></script>
    <script type="text/javascript" >
        window.onload=(function(){ getDataList();})//初始化
        //操作按钮
        function buttonCz(value,row,index){
            var button="";
        <pro:authFilter buttonCode="del" actionUrl="${_modulesLink}" >
                    button="<a href='javascript:void(0);' onclick=\"del('"+row.id+"')\">[删除]</a>";
        </pro:authFilter>
            return button;
        }
        //下载按钮
        function showDownLoadFile(filePath){
            var button="";
        <pro:authFilter buttonCode="download" actionUrl="${_modulesLink}" >
            if($.trim(filePath).length>0)
                button="<a href='javascript:void(0);' onclick=\"downLoadFile('"+filePath+"')\">[下载文件]</a>";
        </pro:authFilter>
            return button;
        }
        //查看文件按钮
        function showViewFile(value,row,fieldName,showType){
            var button="";

        <pro:authFilter buttonCode="printFile" actionUrl="${_modulesLink}" >
            showType+="&printFile=printFile";
        </pro:authFilter>
        <pro:authFilter buttonCode="copyFile" actionUrl="${_modulesLink}" >
            showType+="&copyFile=copyFile";
        </pro:authFilter>
        <pro:authFilter buttonCode="downloadFile" actionUrl="${_modulesLink}" >
            showType+="&downloadFile=downloadFile";
        </pro:authFilter>
        <pro:authFilter buttonCode="viewfile" actionUrl="${_modulesLink}" >
            if($.trim(value).length<=0)
                value="upload/"+row.id+"/"+fieldName;
            button="<a href='javascript:void(0);' onclick=\"showFileList('"+value+"','"+showType+"')\">[查看文件]</a>";
        </pro:authFilter>
            return button;
        }

    </script>
</head>
<body class="easyui-layout"  style="width: 900px;height: 650px" fit="true">
<div data-options="region:'north'"  align="center" style="overflow: hidden">
    <form id="searchForm" method="post">
        <%--预定义条件--%>
        <c:forEach items="${_conditions}" var="_condition">
            <c:forEach items="${_condition.value}" var="_val">
                <input type="hidden" name="${_condition.key}"  value="${_val}" />
            </c:forEach>
        </c:forEach>
        <input type="hidden" name="pageNum" id="pageNum" value="1" />
        <input type="hidden" id="_order" name="orders" />
        <table width="100%" >
            <tr height="10">
                <td class="head_search" colspan="20" style="text-align:left;height:20px;padding:10px 0 0 0;">
                    &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据查询</b>
                </td>
            </tr>
            <tr height="30">
                <%
                    int count=0;
                    for(TableFieldBean fieldBean:fieldBeanList){
                        if(fieldBean.isQuery()){
                            count++;
                %>
                <td width="10%"  align="right"><%=fieldBean.getFieldContent()%>：</td><td><%=(viewUtil.getFieldQuery(fieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%></td>
                <%
                    if(count%3==0){
                %>
            </tr>
            <tr height="30">
                <%
                            }
                        }
                    }
                %>
            </tr>
            <tr align="center">
                <td colspan="6" style="border-bottom:1px solid #d8d8d8;padding:5px 25px 10px 0;height:25px;">
                    <a href="javascript:void(0);" class=" easyui-linkbutton" onclick="getDataList()">查询</a>
                    &nbsp;&nbsp;<a href="javascript:void(0);"  class="easyui-linkbutton"   onclick="resetForm()">重置</a>
                </td>
            </tr>
        </table>
    </form>
</div>
<div data-options="region:'center'" style="height:500px;" align="center">

    <table id="dataTable"  class="easyui-datagrid" fit="true"  align="center" data-options="toolbar:'#tools',onSortColumn:function(sort,order){$('#_order').val(sort+':'+order);getDataList();}">
        <thead data-options="frozen:true">
        <tr>
            <th field="id" data-options="checkbox:true"></th>
        </tr>
        </thead>
        <thead>
        <tr>
    <%
        for(TableFieldBean tableFieldBean:fieldBeanList){
            if(tableFieldBean.isShowList()){
     %>
          <%=(viewUtil.getFieldShow(tableFieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%>
     <%
            }
        }
    %>
            <th field="cz"   data-options="formatter: function(value,row,index){ return buttonCz(value,row,index);}" >操作</th>
        </tr>
        </thead>
    </table>
</div>
<div  data-options="region:'south'" >
    <div class="easyui-panel" style="height: 38px;overflow: hidden">
        <div id="pager" class="easyui-pagination" data-options="showRefresh:false,showPageList:false,total:0,onSelectPage: function(pageNumber, pageSize){ $('#pageNum').val(pageNumber); getDataList();}"></div>
    </div>
</div>
<div id="tools" style="padding: 5px">
    <pro:authFilter buttonCode="add" actionUrl="${_modulesLink}" >
        <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-add" onclick="showUpdateView(false)">添加</a>
    </pro:authFilter>
    <pro:authFilter buttonCode="update" actionUrl="${_modulesLink}" >
        <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateView(true)">修改</a>
    </pro:authFilter>
    <pro:authFilter buttonCode="muldel" actionUrl="${_modulesLink}" >
        <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-remove" onclick="dels()">批量删除</a>
    </pro:authFilter>
    <pro:authFilter buttonCode="detail" actionUrl="${_modulesLink}" >
        <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-edit" onclick="showDetailView()">查看详情</a>
    </pro:authFilter>
    <%@include file="/include/code/business_button_ext.jsp"%>
</div>
<%@include file="/include/code/business_dialog.jsp" %>
<%@include file="/include/code/business_footer_ext.jsp" %>
</body>
</html>
