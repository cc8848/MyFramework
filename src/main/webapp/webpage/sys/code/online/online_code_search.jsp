<%@ page import="com.code.entity.TableFieldBean" %>
<%@ page import="com.code.entity.TableHeadBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.code.util.ViewUtil" %>
<%@ page import="com.code.util.ViewUtilFactory" %><%
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
        <tr height="20">
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
            <td width="10%"  align="right">保管单位：</td><td><%=(viewUtil.getFieldShow(fieldBean)+"").replace("${pageContext.request.contextPath}",request.getContextPath())%></td>
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