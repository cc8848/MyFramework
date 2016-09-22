<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<pro:authFilter buttonCode="add" actionUrl="${_modulesLink}" >
    <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-add" onclick="showAddDia()">添加</a>
</pro:authFilter>
<pro:authFilter buttonCode="update" actionUrl="${_modulesLink}" >
    <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-edit" onclick="showUpdateDia()">修改</a>
</pro:authFilter>
<pro:authFilter buttonCode="muldel" actionUrl="${_modulesLink}" >
    <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-remove" onclick="mulDelData()">批量删除</a>
</pro:authFilter>
<pro:authFilter buttonCode="detail" actionUrl="${_modulesLink}" >
    <a href="javascript:void(0);"  class=" easyui-linkbutton" iconCls="icon-edit" onclick="showDetail()">查看详情</a>
</pro:authFilter>
