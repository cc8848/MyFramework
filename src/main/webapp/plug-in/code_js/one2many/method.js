//保存
function saveData(){
    if(!$("#editForm").form("validate")||!endEdit())
        return false;
    var d=JSON.stringify(getOneToManyData("editForm",datagridIdArry));
    ajaxJson(actionUrl+"?jsonSave",{jsonData:d},function(data){
        slideMsgInfo(data.msg);
        loadData(data.dataMap.id);
    });
}
//添加条目
function addItem(datagridId){
    $('#'+datagridId).datagrid('appendRow',{});
    var  editIndex = $('#'+datagridId).datagrid('getRows').length-1;
    $('#'+datagridId).datagrid('beginEdit', editIndex);
}
//删除条目
function deleteItem(datagridId){
    var rows=$('#'+datagridId).datagrid("getChecked");
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
                    var rowIndex=$('#'+datagridId).datagrid("getRowIndex",rows[i]);
                    $('#'+datagridId).datagrid("cancelEdit",rowIndex)
                        .datagrid("deleteRow",rowIndex);
                }
                ajaxJson(actionUrl+"?del"+datagridId, $.param({ids:ids},true),function(data){
                        slideMsgInfo(data.msg);
                });
            }
        })
    }
}
/**
 *编辑行
 **/
function editItem(daId){
    var rows=$('#'+daId).datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    for(var i=0;i<rows.length;i++){
        var index= $('#'+daId).datagrid('getRowIndex', rows[i]);
        $('#'+daId).datagrid('beginEdit', index);
    }
}
function endEdit(){
    for(var id=0;id<datagridIdArry.length;id++){
        var  editIndex = $('#'+datagridIdArry[id]).datagrid('getRows').length-1;
        for(var i=0;i<=editIndex;i++){
            if($('#'+datagridIdArry[id]).datagrid('validateRow', i))
            $('#'+datagridIdArry[id]).datagrid('endEdit', i);
            else
                return false;
        }
    }

    return true;

}
function datagridEndEdit(datagridIdArray){
    for(var id=0;id<datagridIdArray.length;id++){
        var  editIndex = $('#'+datagridIdArray[id]).datagrid('getRows').length-1;
        for(var i=0;i<=editIndex;i++){
            if($('#'+datagridIdArray[id]).datagrid('validateRow', i))
                $('#'+datagridIdArray[id]).datagrid('endEdit', i);
            else
                return false;
        }
    }

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
        var rows=$("#"+datagridIdArry[i]).datagrid("getChanges","inserted");
        var uprows=$("#"+datagridIdArry[i]).datagrid("getChanges","updated");
        rows=rows.concat(uprows);
        data[datagridIdArry[i]]=rows;
    }
    return data;
}
//子表查看文件列表
function fileList_sub(id,name,val,showType){
    var filePath=val;
    if(filePath=='null'||filePath=='undefined'||$.trim(filePath).length==0){
        if(id=='null'||id=='undefined'||id.length==0){
            alertMsg("请先保存数据再上传文件！");
            return false;
        }
        filePath="upload/"+id+"/"+name;
    }
    showFileList(filePath,showType,"update");
}