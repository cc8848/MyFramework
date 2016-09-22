<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.code.entity.TableHeadBean" %>
<%@ page import="com.code.entity.TableFieldBean" %>
<%@ page import="com.code.util.ViewUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.code.util.ViewUtilFactory" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
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
    List<TableHeadBean> subHeadList=(List<TableHeadBean>)request.getAttribute("subHeadList");
    if(subHeadList==null){
        subHeadList=new ArrayList<TableHeadBean>();
    }
    ViewUtil viewUtil= ViewUtilFactory.getInstance("easyui");
%>
<!DOCTYPE html>
<html>
<head>
    <title>数据更新</title>
    <%@include file="/include/head.jsp"%>
    <%@include file="/include/code/one2many/view_ext.jsp" %>
    <script  type="text/javascript" src="${pageContext.request.contextPath}/plug-in/code_js/common_method.js" ></script>

</head>
<body>
<%
    if(CollectionUtils.isEmpty(subHeadList)){
        %>
<form id="editForm">
    <input name="id" type="hidden" id="edit_id">
    <table align="center">
        <tr>

            <%
                int count=0;
                for(TableFieldBean fieldBean:fieldBeanList){
                    if(fieldBean.isInsert()){
                        count++;
            %>
            <td style="padding: 5px"><%=fieldBean.getFieldContent()%>：</td>
            <td style="padding: 5px"><%=(viewUtil.getFieldInput(fieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%></td>
            <%
                if(count%3==0){
            %>
        </tr>
        <tr >
            <%
                        }
                    }
                }
            %>
        </tr>
        <tr align="center">
            <td colspan="6"><a href="javascript:void(0);"  class=" easyui-linkbutton" onclick="saveData(datagridIdArry)">提交</a></td>
        </tr>
    </table>
</form>
<%
    }else{
%>
<div class="easyui-layout"  fit="true"  >
    <div data-options="region:'north'" style="height: 120px;" align="center"  >
        <form id="editForm">
            <input name="id" type="hidden" id="edit_id">
            <table align="center">
                <tr>

                    <%
                        int count=0;
                        for(TableFieldBean fieldBean:fieldBeanList){
                            if(fieldBean.isInsert()){
                                count++;
                    %>
                    <td style="padding: 5px"><%=fieldBean.getFieldContent()%>：</td>
                    <td style="padding: 5px"><%=(viewUtil.getFieldInput(fieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%></td>
                    <%
                        if(count%3==0){
                    %>
                </tr>
                <tr >
                    <%
                                }
                            }
                        }
                    %>
                </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south'" style="height: 30px;"  align="center" >
        <a href="javascript:void(0);"  class=" easyui-linkbutton" onclick="saveData(datagridIdArry)">保存</a>
    </div>
<%
    int index=0;
    if(CollectionUtils.isNotEmpty(subHeadList)){
       for(TableHeadBean subHeadBean:subHeadList){
           index++;
%>
    <div  data-options="region:'center'" >
        <div id="subListDiv" class="easyui-tabs" >
            <div  title="<%=subHeadBean.getTableContent()%>">
                <table id="<%=subHeadBean.getTableName()%>"   class="easyui-datagrid" data-options="toolbar:'#tb<%=index%>'">
                    <thead>
                    <th field="id" data-options="checkbox:true"></th>
           <%
             List<TableFieldBean> tableFieldBeans=subHeadBean.getFields();
             if(CollectionUtils.isNotEmpty(tableFieldBeans)){
                 for(TableFieldBean tableFieldBean:tableFieldBeans){
                     if(!tableFieldBean.isInsert()) continue;
           %>
             <%=(viewUtil.getEditFieldShow(tableFieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%>
           <%
                   }
             }
           %>
                    </thead>
                </table>

            </div>
            <div id="tb<%=index%>">
                <a href="javascript:void(0)" class=" easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addItem('<%=subHeadBean.getTableName()%>')">增加</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editItem('<%=subHeadBean.getTableName()%>')">修改</a>
                <a href="javascript:void(0)" class=" easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteItem('<%=subHeadBean.getTableName()%>')">删除</a>
            </div>
        </div>

    </div>
 <%
         }
     }
   }
 %>
</div>
<script type="text/javascript" >
    var datagridIdArry=new Array();
<c:forEach  var="subHead" items="${subHeadList}">
    datagridIdArry.push('${subHead.tableName}');
</c:forEach>
    var id='${id}';
    if($.trim(id).length>0)
      loadDetail(id,datagridIdArry);
    //子表文件上传按钮
    function list_file_upload(id,name,val,showType){
        return "<a href=\"#\"  onclick=\"fileList_sub('"+id+"','"+name+"','"+val+"','"+showType+"')\">[附件]</a>";
    }
    function saveData(datagridIdArry){
        if(!saveDataFilter()){
            return false;
        }
        if(datagridIdArry&&datagridIdArry.length>0){
            if(!$("#editForm").form("validate")||!endEdit())
                return false;
        }else{
            if(!$("#editForm").form("validate"))
                return false;
        }
        var d="";
        if(datagridIdArry.length>0)
            d=JSON.stringify(getOneToManyData("editForm",datagridIdArry));
        else
            d=formData2Json("editForm");
        ajaxJson(actionUrl+"?save",{jsonData:d},function(data){
            if(assertAjaxSuccess(data))
                slideMsgInfo(data.msg);
            loadDetail(data.dataMap.id,datagridIdArry);
        });
    }
</script>
</body>
</html>
