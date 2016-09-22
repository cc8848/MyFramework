<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/sys/code/tableinf.do
</c:set>
<html> 
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        window.onload=(function(){
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
                });
            }
        });
        //保存
        function saveData(){
            var datagridIdArry=[{name:"fields",gridId:"jcxx_t"}];
            if(!$("#editForm").form("validate")||!endEdit())
                return false;
            var d=JSON.stringify(getOneToManyData("editForm",datagridIdArry));
            ajaxJson("${actionUrl}?jsonSave",{jsonData:d},function(data){
                window.parent.closeDiaWithMsgWhenSuccess("editDia",data);
                window.parent.getDataList();
            });
        }
        // 添加条目
        function addItem(){
            $('#jcxx_t').datagrid('appendRow',{fieldVersion:1.0,fieldOrder:$('#jcxx_t').datagrid('getRows').length+1,isNull:1,isInsert:1,isQuery:1,isShowList:1,isShow:1});
            var  editIndex = $('#jcxx_t').datagrid('getRows').length-1;
            $('#jcxx_t').datagrid('beginEdit', editIndex);
        }
        //删除条目
        function deleteItem(){
            var rows=$('#jcxx_t').datagrid("getChecked");
            if(rows.length==0){
                alertMsg("请选择条目");
                return false;
            }else{

                $.messager.confirm("提示","确定删除",function(r){
                    if(r){
                        var ids=new Array();
                        for(var i=0;i<rows.length;i++){
                            if(rows[i].id)
                                if($.trim(rows[i].id.length)>0){
                                    ids.push(rows[i].id);
                                }
                            var rowIndex=$('#jcxx_t').datagrid("getRowIndex",rows[i]);
                            $('#jcxx_t').datagrid("cancelEdit",rowIndex)
                                        .datagrid("deleteRow",rowIndex);
                        }
                        ajaxJson("${actionUrl}?delFields", $.param({ids:ids},true),function(data){

                        });
                    }
                })
            }
        }
        function endEdit(){
            var  editIndex = $('#jcxx_t').datagrid('getRows').length-1;
            for(var i=0;i<=editIndex;i++){
                if($('#jcxx_t').datagrid('validateRow', i))
                    $('#jcxx_t').datagrid('endEdit', i);
                else
                    return false;
            }
           // $('#jcxx_t').datagrid("acceptChanges");
            return true;

        }
        /**
         *将form中的普通表单数据及datagrid中的数据组合起来
         * @param formId
         * @param datagridIdArry
         */
        function getOneToManyData(formId,datagridIdArry){
            var data={};
            var formData=$("#"+formId).serializeArray();
            for(var i=0;i<formData.length;i++){
                data[formData[i]["name"]]=formData[i]["value"];
            }
            for(var i=0;i<datagridIdArry.length;i++){
                var rows=$("#"+datagridIdArry[i]["gridId"]).datagrid("getChanges","inserted");
                var uprows=$("#"+datagridIdArry[i]["gridId"]).datagrid("getChanges","updated");
                rows=rows.concat(uprows);
                data[datagridIdArry[i]["name"]]=rows;
            }
            return data;
        }

        function checkboxFormat(val){
            if(val=='1'||val==1)
                return "是";
            else
                return "否";
        }
        /**
         *开始编辑
         */
        function editItem(){
            var rows=$('#jcxx_t').datagrid("getChecked");
            if(rows.length==0){
                alertMsg("请选择条目");
                return false;
            }
            for(var i=0;i<rows.length;i++){
                var index= $('#jcxx_t').datagrid('getRowIndex', rows[i]);
                $('#jcxx_t').datagrid('beginEdit', index);
            }


        }
        function formatQueryModel(val){
            if("single"==val)
                return "单一查询";
            else
                return "范围查询";
        }
        //设置模板字段
        function setTemplateFields(templateId){
            ajaxJson("${pageContext.request.contextPath}/sys/template.do?gettemplatefields",{id:templateId},function(data){
                if(data){
                    for(var i=0;i<data.length;i++){
                        data[i].id='';
                        $("#jcxx_t").datagrid("appendRow",data[i]);
                        var  editIndex = $('#jcxx_t').datagrid('getRows').length-1;
                        $('#jcxx_t').datagrid('beginEdit', editIndex);
                    }
                }

            });
        }
    </script>
</head>
<body class="body_s">
<div class="easyui-layout"  fit="true"  >
    <div data-options="region:'north'" align="center"   style="overflow: hidden" >
    <form id="editForm">
        <input name="id" type="hidden" id="edit_id">
        <table  >
            <tr>
                <td align="right">表名称：</td>
                <td >
                    <input class='easyui-validatebox  ' name='tableName' data-options="required:true"  />
                </td>

                <td align="right">表描述：</td>
                <td >
                    <input class='easyui-validatebox  ' name='tableContent'  data-options="required:true" />
                </td>
                <td align="right">视图类型：</td>
                <td >
                    <input class='easyui-combobox' name='tableViewType' data-options="required:true,valueField:'id',textField:'value',data:[{value:'单表',id:'single'},{value:'主表',id:'main'}]" />
                </td>
            </tr>
            <%--<tr>--%>
				<%--<td align="right">视图类型：</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-combobox' name='tableViewType' data-options="required:true,valueField:'id',textField:'value',data:[{value:'单表',id:'single'},{value:'主表',id:'main'}]" />--%>
                <%--</td>--%>

                <%--<td align="right">基础模板</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-combobox' id="basetemplate" name='basetemplate' data-options="valueField:'id',textField:'tableContent',multiple:true,url:'${pageContext.request.contextPath}/sys/template.do?gettemplatename',method:'get',onSelect:function(row){setTemplateFields(row.id);}" />--%>
                <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
                <%--<td align="right">档号字段英文名</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-validatebox'  name='dhField'/>--%>
                <%--</td>--%>
                <%--<td align="right">档号规则</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-validatebox'  name='dhFieldRule'/>--%>
                <%--</td>--%>
            <%--</tr>--%>
            <%--<tr>--%>
                <%--<td align="right">是否检索</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-combobox' id="isIndex" name='isIndex' data-options="valueField:'id',textField:'value',data:[{value:'是',id:'1'},{value:'否',id:'0'}]" />--%>
                <%--</td>--%>
                <%--&lt;%&ndash;<td align="right">是否动态实体</td>--%>
                <%--<td >--%>
                    <%--<input class='easyui-combobox'  name='dyEntity' data-options="valueField:'id',textField:'value',data:[{value:'是',id:'1'},{value:'否',id:'0'}]" />--%>
                <%--</td>&ndash;%&gt;--%>
			<%--</tr>--%>
        </table>
     </form>
     </div>
    <div id="jcxxDiv"  data-options="region:'center'"  >
        <table id="jcxx_t"   class="easyui-datagrid" data-options="toolbar:'#tb'" fit="true">
            <thead>
            <tr style="background:#f2f3f7">
                <th colspan="9"><b>基础信息</b></th>
                <th colspan="8"><b>页面展示</b></th>
                <th colspan="4"><b>字典管理</b></th>
                <th colspan="2"><b>外键</b></th>
            </tr>
            <tr align="center">
                <th field="id" data-options="checkbox:true"></th>
                <th  field="fieldContent"  data-options="width:100,editor:{type:'validatebox',options:{required:true}}">字段描述</th>
                <th field="fieldName" data-options="width:100,editor:{type:'validatebox',options:{required:true}}">字段名称</th>
                <th  field="fieldType" data-options="width:100,editor:{type:'combobox',options:{required:true,valueField:'text',textField:'text',data:[{text:'int'},{text:'float'},{text:'long'},{text:'string'},{text:'file'},{text:'date'},{text:'timestamp'}]}}">字段类型</th>
                <th  field="fieldLength" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">字段长度</th>
                <th  field="fieldOrder" data-options="width:100,editor:{type:'numberbox',options:{}}">字段排序</th>
                <th  field="fieldVersion" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">字段版本</th>
                <th  field="isKey" data-options="align:'center',width:45,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">主键</th>
                <th  field="isNull" data-options="align:'center',width:100,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">允许空值</th>
                <th field="showType" data-options="width:100,editor:{type:'combobox',options:{required:true,valueField:'cKey',textField:'cValue',url:'${pageContext.request.contextPath}/sys/code/tableinf.do?viewerKeys'}}">显示类型</th>
                <th field="showLength" data-options="width:100,editor:{type:'numberbox',options:{precision:1}}">显示长度</th>
                <th field="fieldValidType"  data-options="width:100,editor:{type:'validatebox'}">验证规则</th>
                <th field="isInsert" data-options="align:'center',width:100,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示录入</th>
                <th field="isQuery" data-options="align:'center',width:100,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示查询</th>
                <th field="isShowList" data-options="align:'center',width:100,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示列表</th>
                <th field="isShow" data-options="align:'center',width:100,formatter:function(value,row,index){return checkboxFormat(value);},editor:{type:'checkbox',options:{on:'1',off:'0'}}">显示详情</th>
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
    <div data-options="region:'south'" >
        <div style=" height: 25px;" align="center">
            <a href="javascript:void(0);"   class="easyui-linkbutton" onclick="saveData()">保存</a>
        </div>
    </div>
</div>
<div id="tb"class="list_btn">
	<ul>
		<li class="list_btn_name">
			<img src="${pageContext.request.contextPath}/plug-in/main/images/icon_aj_list.png" style="vertical-align:middle"/>&nbsp;&nbsp;<b class="head_font">字段信息</b>
		</li>
		<li class="list_a">
			<a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addItem()">添加</a>
			<a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editItem()">修改</a>
			<a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteItem()">删除</a>
			
		</li>
	</ul>

</div>

</body>
</html>
