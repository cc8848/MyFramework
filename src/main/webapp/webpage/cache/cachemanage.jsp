<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/cache.do
</c:set>
<html>
<head>
    <title>缓存管理</title>
    <%@include file="/include/head.jsp"%>
    <script>
        $(function(){getDataList()});
        //操作按钮
        function buttonCz(cName){
            var button="";
            button="<a href='javascript:void(0);' onclick=\"delData('"+cName+"')\">[清理]</a>";
            return button;
        }
        //删除
        function delData(cName){
            $.messager.confirm("提示","确定清理?",function(d){
                if(d)
                    ajaxJson("${ actionUrl}?del",{cName:cName},function(data){
                        slideMsgInfo(data.msg);
                        getDataList();
                    });
            });

        }
        function getDataList(){
            ajaxJson("${ actionUrl}?getcaches",{},function(data){
                if(data){
                    $("#dataTable").datagrid("loadData",data);
                }
            });
        }
    </script>
</head>
<body >
 <div align="center" style="height: 100%;width: 100%">
     <table id="dataTable" title="缓存信息" class="easyui-datagrid" style="height: 300px;width: 500px">
        <thead>
         <tr>
            <th field="cacheName">缓存名称</th>
            <th field="cacheSize">缓存中对象数量</th>
            <th field="memoryStoreSize">占内存数</th>
            <th field="cacheHits">命中数</th>
            <th field="cacheMisses">失误数</th>
            <th field="cz" data-options="formatter: function(value,row,index){ return buttonCz(row.cacheName);}">操作</th>
         </tr>
        </thead>
     </table>
 </div>
</body>
</html>
