<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/business/module_ext.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>扩展组件管理</title>
    <%@include file="/include/head.jsp"%>
    <style>
        .tborder{
            border-collapse:collapse;

        }
        .tborder tr td {
            border:1px solid #9a9a9a;
            padding: 15px;
        }
    </style>
    <script type="text/javascript">

        function checkAll(){
            var checkboxes=$(".checkb");
            if($("#checka").attr("checked")){
                checkboxes.attr("checked",true);
            }else{
                checkboxes.attr("checked",false);
            }
        }
        function updateModule(){
            var table=$(".sTable");
            var tableNames=new Array();
            for(var i=0;i<table.length;i++){
                tableNames.push(table[i].value);
            }
            var checkedIds=$(".checkb:checked");
            var idArray=new Array();
            if(checkedIds.length>0){
                for(var i=0;i<checkedIds.length;i++){
                    idArray.push(checkedIds.eq(i).val());
                }
            }
            ajaxJson("${actionUrl}?updateModuleRule", $.param({tableName:tableNames,moduleIds:idArray},true),function(data){
                alertMsg(data.msg);
                if(data.success){
                    window.parent.closeMoudules();
                }
            });
        }
    </script>
</head>
<body>
<form action="${actionUrl}?updateModuleRule" method="post">
    <table align="center" style="padding: 5px;width: 100%" class="tborder">
        <tr>
            <td ><input type="checkbox" id="checka" onclick="checkAll()" >
                 <c:forEach var="tableName" items="${tableNames}" >
                     <input type="hidden" class="sTable" name="tableName" value="${tableName}"/>
                 </c:forEach>
            </td>
            <td>组件名称</td>
        </tr>
        <c:forEach var="data" items="${dataList}">
            <tr>
                <td><input type="checkbox" class="checkb" ${data.checked?'checked':''} name="ids" value="${data.id}"></td>
                <td>${data.moduleName}</td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="2" align="center">
                <a class="easyui-linkbutton" onclick="updateModule()">确定</a>
            </td>
        </tr>
    </table>
</form>

</body>
</html>
