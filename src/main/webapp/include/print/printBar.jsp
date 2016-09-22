<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="printUrl">
    ${pageContext.request.contextPath}/sys/print.do
</c:set>
<script type="text/javascript" >
    var tableName='${param.tableName}';
   /* $(function(){
        getTemplates();
    });*/
    //获取模板数据
    function loadTemplate(){
        var mName=$("#mName").val();
        if($.trim(mName).length>0)
        ajaxJson("${printUrl}?getTemplate",{tableName:tableName,templateName:mName},function(data){
            if(data){
                for(var i=0;i<data.length;i++){
                    $("#"+data[i].mfield).val(data[i].mvalue);
                }
            }
        });
    }
    //获取模板名称列表
    function getTemplates(templateName){
        $("#mName").empty();
        ajaxJson("${printUrl}?templates",{tableName:tableName},function(data){
            if(data){
                for(var i=0;i<data.length;i++){
                    $("#mName").append("<option value="+data[i]+">"+data[i]+"</option>");
                }
                if(templateName)
                    $("#mName").find("option[value='"+templateName+"']").attr("selected",true);
                loadTemplate();
            }
        });
    }
    //保存模板
    function saveTemplate(templateName){
        var fields=$("._template_field");
        var templates=[];
        var fieldArray=[];
        for(var i=0;i<fields.length;i++){
            fieldArray[fields.eq(i).attr("id")]=fields.eq(i).val();

        }

        for(var f in fieldArray){
            var template={};
            template["mName"]=templateName;
            template["mTablename"]=tableName;
            template["mField"]=f;
            template["mValue"]=fieldArray[f];
            templates.push(template);
        }
        ajaxJson("${printUrl}?jsonSave",{jsonData:JSON.stringify(templates)},function(data){
            if(data.success){
                getTemplates(templateName);
            }
            alertMsg(data.msg);
        });
    }
    //增加模板
    function addTemplate(){
        $.messager.prompt('新增模板', '请输入模板名称', function(r){
            if (r){
                saveTemplate(r);
            }
        });
    }
    //更新模板
    function updateTemplate(){
        var mName=$("#mName").val();
        if($.trim(mName).length==0){
            alertMsg("请选择一个模板");
            return false;
        }
        saveTemplate(mName);
    }
    //删除模板
    function delTemplate(){
        var mName=$("#mName").val();
        if($.trim(mName).length==0){
            alertMsg("请选择一个模板");
            return false;
        }
        ajaxJson("${printUrl}?delTemplate",{tableName:tableName,templateName:mName},function(data){
            if(data){
                alert(data.msg);
            }
        });
    }
    function setField(){
        var fields=$("._template_field");
        for(var i=0;i<fields.length;i++){
            fields.eq(i).parent().html(fields.eq(i).val());
        }
    }
    function doPrintViewAndAutoColumnSize(datagridId){
      //  setField();
        doPrintView();
    }
    function doPrintAndAutoColumnSize(datagridId,orientation,preView){
      //  setField();
        doPrint(orientation,preView)
    }
</script>
<div style="height: 50px;" class="easyui-panel">
  <%--  <select id="mName" name="mName" style="width: 100px;" onchange="loadTemplate()">
        <option value="" >请选择</option>
    </select>
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="addTemplate()">新增模板</a>&nbsp;
    <a href="javascript:void(0);"  class="easyui-linkbutton" onclick="updateTemplate()">修改模板</a>&nbsp;
    <a href="javascript:void(0);"  class="easyui-linkbutton" onclick="delTemplate()">删除模板</a>&nbsp;
   --%> <a href="javascript:void(0);" class="easyui-linkbutton" onclick="doPrintView()">打印预览</a>&nbsp;
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="doPrint(2)">横向打印</a>&nbsp;
    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="doPrint()">打印</a>&nbsp;
</div>