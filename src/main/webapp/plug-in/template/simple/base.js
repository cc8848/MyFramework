//获取数据列表
function getDataList() {
    ajaxJson("${ actionUrl}?datagrid", $("#searchForm").serialize(), function (data) {
        if (data) {
            $("#dataTable").datagrid("loadData", data.dataList);
            $("#pager").pagination({total: data.totalCount, pageSize: data.pagesize, pageNumber: data.pageNum});
        }
    });
}
//打开更新对话框
function showUpdateDia() {
    var row = $("#dataTable").datagrid("getChecked");
    if (row.length == 0) {
        alertMsg("请选择条目");
        return false;
    }
    if (row.length > 1) {
        alertMsg("只能选择一条!");
        return false;
    }
    showFrameDialog("editDia", "editFrame", "${pageContext.request.contextPath}/webpage/wsda_jh/wsda_jh_update.jsp?id=" + row[0].id);
}
//打开新增对话框
function showAddDia() {
    showFrameDialog("editDia", "editFrame", "${pageContext.request.contextPath}/webpage/wsda_jh/wsda_jh_update.jsp");
}
//删除
function delData(id) {
    $.messager.confirm("提示", "确定删除", function (d) {
        if (d)
            ajaxJson("${ actionUrl}?del", {id: id}, function (data) {
                slideMsgInfo(data.msg);
                getDataList();
            });
    });

}
//批量删除
function mulDelData() {
    var rows = $("#dataTable").datagrid("getChecked");
    if (rows.length == 0) {
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示", "确定删除选中的" + rows.length + "条", function (r) {
        if (r == true) {
            var ids = new Array();
            for (var i = 0; i < rows.length; i++) {
                ids.push(rows[i].id);
            }
            ajaxJson("${actionUrl}?muldel", jQuery.param({ids: ids}, true), function (data) {
                slideMsgInfo(data.msg);
                getDataList();
            });
        }
    });

}