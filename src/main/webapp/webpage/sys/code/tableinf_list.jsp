<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
   ${pageContext.request.contextPath}/sys/code/tableinf.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>表信息管理</title>
    <%@include file="/include/head.jsp"%>
    <script type="text/javascript" >
        var context='${pageContext.request.contextPath}';
        window.onload=(function(){
            getDataList();
            /*ajaxJson("${actionUrl}?templateList",{},function(data){
               $("#code_t").combotree("loadData",data);
            });*/
        })//初始化
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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/tableinf_update.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","更新数据");
            openEasyuiDialog("editDia");

          //  showBussinessWindow("更新","${pageContext.request.contextPath}/webpage/sys/code/tableinf_update.jsp?id="+row[0].id);
        }
        //打开新增对话框
        function showAddDia(){
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/tableinf_update.jsp");
            $("#editDia").dialog("setTitle","新增数据");
            openEasyuiDialog("editDia");
           // showBussinessWindow("新增","${pageContext.request.contextPath}/webpage/sys/code/tableinf_update.jsp");
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
            $("#editFrame").attr("src","${pageContext.request.contextPath}/webpage/sys/code/tableinf_detail.jsp?id="+row[0].id);
            $("#editDia").dialog("setTitle","数据详情");
            openEasyuiDialog("editDia");
           // showBussinessWindow("详情","${pageContext.request.contextPath}/webpage/sys/code/tableinf_detail.jsp?id="+row[0].id);
        }
        //操作按钮
        function buttonCz(value,row,index){
            return "<a href='javascript:void(0);' onclick=\"delData('"+row.id+"')\">[删除]</a>";
        }
        //同步表实体
        function syncTables(){
            var rows=$("#dataTable").datagrid("getChecked");
            if(rows.length==0){
                if(!confirm("确定同步全部？"))
                    return false;
            }

                    var ids=new Array();
                    for( var i=0;i<rows.length;i++){
                        ids.push(rows[i].id);
                    }
                    ajaxJson("${actionUrl}?generateEntityClass", $.param({ids:ids},true),function(data){
                        if(assertAjaxSuccess(data))
                        slideMsgInfo(data.msg);
                    });



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
            $("#basePackageName").val("business."+row[0].tableName);
            $("#viewFolder").val("business/"+row[0].tableName);
            $("#codDia").dialog("open");
        }
        function genereateCode(){
            ajaxJson("${actionUrl}?genereatecode",$("#codeForm").serialize(),function(data){
                if(data.success)
                   closeDiaWithMsgWhenSuccess("codDia",data);
                else
                   alertMsg(data.msg);
            });
        }
    </script>
</head>
<body class="easyui-layout" title="表信息" style="width: 900px;height: 540px" fit="true">
<div data-options="region:'north'" align="center" style="overflow:hidden;">
    <form id="searchForm">
        <input type="hidden" name="pageNum" id="pageNum" value="1"/>
        <table width="100%">
            <tr height="20">
                <td class="head_search" colspan="20" style="text-align:left;height:20px;padding:10px 0 0 0;">
                    &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据查询</b>
                </td>
            </tr>
            <tr>
                <td align="right">表名称：</td>
                <td><input class='easyui-validatebox' name='tableName_'/></td>
                <td align="right">表描述：</td>
                <td><input class='easyui-validatebox' name='tableContent_'/></td>
                <td align="right">视图类型：</td>
                <td><input class='easyui-combobox' name='tableViewType'
                           data-options="valueField:'id',textField:'value',data:[{value:'单表',id:'single'},{value:'主表',id:'main'}]"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="getDataList()">查询</a>&nbsp;&nbsp;<a
                        href="javascript:void(0);"  class="easyui-linkbutton" onclick="getDataList()">重置</a>
                </td>
            </tr>

        </table>
    </form>
</div>
<div data-options="region:'center'" style="height:500px;margin:0 0 0 0;" align="center" fit="true">
    <table id="dataTable" class="easyui-datagrid" align="center" fit="true"
           data-options="toolbar:'#tools',onSortColumn:function(sort,order){$('#_order').val(sort+':'+order);getDataList();}">
        <thead>
        <tr>
            <th field="id" data-options="checkbox:true,width:200"></th>
            <th field='tableName' data-options="width:200">表名称</th>
            <th field='tableContent' data-options="width:200">表描述</th>
            <th field='tableViewType'
                data-options="width:200,formatter: function(value,row,index){if('single'==value) return '单表';else return '主表';}">
                视图类型
            </th>
            <%-- <th field='dyEntity'  data-options="width:100,formatter: function(value,row,index){if(value=='1') return '是';else return '否';}" >是否动态实体</th>--%>
            <th field='isIndex'
                data-options="width:100,formatter: function(value,row,index){if('1'==value) return '是';else return '否';}">
                是否检索
            </th>
            <th field="cz"
                data-options="width:100,formatter: function(value,row,index){ return buttonCz(value,row,index);}">操作
            </th>
        </tr>
        </thead>
    </table>


</div>
<div data-options="region:'south'">
    <div class="easyui-panel" style="height: 38px;">
        <div id="pager" class="easyui-pagination"
             data-options="showRefresh:false,showPageList:false,total:0,onSelectPage: function(pageNumber, pageSize){ $('#pageNum').val(pageNumber); getDataList();}"></div>
    </div>
</div>

<div id="tools" style="padding: 5px">
    <table width="100%">
        <tr height="20">
            <td class="head_search" colspan="20" style="text-align:left;padding:0 0 10px 0;">
                &nbsp;&nbsp;&nbsp;&nbsp;<b class="head_font" style="font-size:13px">数据列表</b>
            </td>
        </tr>
    </table>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" onclick="showAddDia()">添加</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateDia()">修改</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" onclick="mulDelData()">批量删除</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" onclick="showDetail()">详情</a>
    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-ok" onclick="showCodeConfig()">生成代码</a>
</div>
<div id="editDia" title="更新" class="easyui-dialog" style="width:900px;height:500px;" data-options="closed:true">
    <iframe scrolling="auto" id="editFrame"  frameborder="0"  src="" style="width:99%;height:99%;"></iframe>
</div>


<div id="codDia" align="center" title="代码生成器配置" class="easyui-dialog" data-options="closed:true,width:500,height:300,modal:true">
    <form id="codeForm">
            <input name="headId" type="hidden" id="code_id">
            <table align="center">
                <tr>
                    <td style="padding: 5px" align="right">包名：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox  ' id="basePackageName" name='basePackageName' data-options="required:true"  />
                    </td>
                </tr>
                <tr>
                    <td style="padding: 5px" align="right">视图文件夹：</td>
                    <td style="padding: 5px">
                        <input class="input_arch_sea" class='easyui-validatebox  ' id="viewFolder" name='viewFolder'  data-options="required:true" />
                    </td>
                </tr>
                <%--<tr>
                    <td style="padding: 5px" align="right">代码模板选择：</td>
                    <td style="padding: 5px">
                        <input  class='easyui-combotree' id="code_t" name='code_t' style="width: 300px" multiple   />
                    </td>
                </tr>--%>
                <tr>
                    <td style="padding: 5px" align="right">代码类型：</td>
                    <td style="padding: 5px">
                        ENTITY:<input type="checkbox" name="codeType" value="entity" checked="checked" />
                        DAO:<input type="checkbox"  name="codeType"  disabled="disabled" value="entity" />
                        SERVICE:<input type="checkbox"  name="codeType"  value="service" checked="checked" />
                        ACTION:<input type="checkbox"  name="codeType"  value="action" checked="checked" />
                        VIEW:<input type="checkbox"  name="codeType"  value="view" checked="checked" />
                    </td>
                </tr>
                <tr>
                    <td style="padding: 5px" colspan="2" align="center">
                        <a href="javascript:void(0);"    class="easyui-linkbutton" onclick="genereateCode()">保存</a>
                    </td>
                </tr>
            </table>
    </form>
</div>

</body>
</html>
