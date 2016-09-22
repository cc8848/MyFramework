<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/template.do
</c:set>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        $(function(){
            $("#editForm").form("reset");
            var id='${param.id}';
            if($.trim(id).length>0){
                ajaxJson("${actionUrl}?get",{id:id},function(data){
                    if(data){
                        $("#editForm").form("load",data);
                        ajaxJson("${actionUrl}?subdatagrid",{"head.id":id},function(data1){
                            $("#jcxx_t").datagrid("loadData",data1);
                        });
                    }
                })
            }

        });
        function checkboxFormat(val){
            if(val=='1'||val==1)
                return "是";
            else
                return "否";
        }
        function formatQueryModel(val){
            if("single"==val)
                return "单一查询";
            else
                return "范围查询";
        }
    </script>
</head>
<body class="body_s">
<div class="easyui-layout" fit="true"  >
    <div data-options="region:'north'" class="search_frame" align="center"  >
        <div class="easyui-panel"  fit="true">
        <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table class="add_tab">
			<tr>
				<td class="head_search_add" colspan="4" >
					<img src="${pageContext.request.contextPath}/plug-in/main/images/icon_aj_search.png" style="vertical-align:middle"/>&nbsp;&nbsp;<b class="head_font">基础模板详细信息</b>
				</td>
			</tr>
			<tr>
				<td height="5" colspan="4">
				</td>
			</tr>
            <tr>
                <td align="right" width="15%">模板名称：</td>
                <td >
                    <input class='easyui-validatebox textbox' disabled="disabled" name='tableName' data-options="required:true"  />
                </td>
                <td align="right">模板描述：</td>
                <td >
                    <input class='easyui-validatebox textbox'  disabled="disabled" name='tableContent'  data-options="required:true" />
                </td>
            </tr>
			<tr>
				<td height="10" colspan="4">
				</td>
			</tr>
        </table>
        </form>
        </div>
    </div>
    <div id="jcxxDiv"  data-options="region:'center'" fit="true">
        <div class="edit_list">
        <table id="jcxx_t"  title="字段信息"  class="easyui-datagrid"  fit="true">
            <thead>
            <tr>
                <th colspan="9"><b>基础信息</b></th>
                <th colspan="8"><b>页面展示</b></th>
                <th colspan="4"><b>字典管理</b></th>
                <th colspan="2"><b>外键</b></th>
            </tr>
            <tr align="center">
                <th field="id" data-options="checkbox:true,hidden:true"></th>
                <th  field="fieldContent"  data-options="width:100,editor:{type:'validatebox',options:{required:true}}">字段描述</th>
                <th field="fieldName" data-options="width:100,editor:{type:'validatebox',options:{required:true}}">字段名称</th>
                <th  field="fieldType" data-options="width:100,editor:{type:'combobox',options:{required:true,valueField:'text',textField:'text',data:[{text:'int'},{text:'float'},{text:'long'},{text:'string'},{text:'file'},{text:'date'},{text:'timestamp'}]}}">字段类型</th>
                <th  field="fieldLength" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">字段长度</th>
                <th  field="fieldOrder" data-options="width:100,editor:{type:'numberbox',options:{}}">字段排序</th>
                <th  field="fieldVersion" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">字段版本</th>
                <th  field="isKey" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">主键</th>
                <th  field="isNull" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">允许空值</th>
                <th field="showType" data-options="width:100,editor:{type:'combobox',options:{required:true,valueField:'text',textField:'text',data:[{text:'text'},{text:'number'},{text:'date'},{text:'combobox'},{text:'checkbox'}]}}">显示类型</th>
                <th field="showLength" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">显示长度</th>
                <th field="fieldValidType"  data-options="width:100,editor:{type:'validatebox'}">验证规则</th>
                <th field="isInsert" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示录入</th>
                <th field="isQuery" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示查询</th>
                <th field="isShowList" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示列表</th>
                <th field="isShow" data-options="align:'center',width:70,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示详情</th>
                <th field="queryModel" data-options="width:100,formatter:function(value,row,index){return formatQueryModel(value) ;},editor:{type:'combobox',options:{required:true,valueField:'val',textField:'text',data:[{val:'single',text:'单一查询'},{val:'group',text:'范围查询'}]}}">查询方式</th>
                <th field="dicKey" data-options="width:100,editor:{type:'validatebox'}">字典key</th>
                <th field="dicValue" data-options="width:100,editor:{type:'validatebox'}">字典value</th>
                <th field="dicTable" data-options="width:100,editor:{type:'validatebox'}">引用表名</th>
                <th field="fieldDefault" data-options="width:130,editor:{type:'validatebox'}">字段默认/字典替换项</th>
                <th field="mainId"  data-options="width:100,editor:{type:'validatebox'}">主表id</th>
                <th field="mainTable"  data-options="width:100,editor:{type:'validatebox'}">主表名称</th>
            </tr>
            </thead>
        </table>
        </div>
    </div>
 </div>
</div>
</body>
</html>
