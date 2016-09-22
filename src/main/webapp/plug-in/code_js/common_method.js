
//文件列表
function fileList(name,val,showType){
    var filePath=val;
    if($.trim(filePath).length==0){
        var id=$("#edit_id").val();
        if(id.length==0){
            alertMsg("请先保存数据再上传文件！");
            return false;
        }
        filePath="upload/"+id+"/"+name;
    }
    window.top.showFileList(filePath,showType,"update");
}
/**
 * 查询数据拦截器
 * @returns {boolean}
 */
function getDataListFilter(){
    return true;
}
/**
 * 删除数据拦截器
 * @param id
 * @param state
 * @returns {boolean}
 */
function delDataFilter(id,state){
    return true;
}
//加载数据
function getDataList(back){
    if(back){
        $("#pageNum").val(1);
    }
    if(!getDataListFilter()){
        return false;
    }
    ajaxJson(actionUrl+"?datagrid",$("#searchForm").serialize(),function(data){
        if(assertAjaxSuccess(data)){
            $("#dataTable").datagrid("loadData",data.dataList);
            $("#pager").pagination({total:data.totalCount,pageSize:data.pagesize,pageNumber:data.pageNum});
        }
    });
}
function toPrintBarCode(barcode){
    var url = context+"/barcode.do?printBarcode&msg="+barcode;
    $("#editFrame").attr("src",url);
    $("#editDia").dialog("setTitle","条形码打印");
    openEasyuiDialog("editDia");

}
//加载数据




//删除
function delData(id,state){
    if(!delDataFilter()){
        return false;
    }
    if(!state){
        alertMsg("当前数据不可操作!");
        return false;
    }
    $.messager.confirm("提示","确定删除",function(d){
        if(assertAjaxSuccess(d))
            ajaxJson(actionUrl+"?del",{id:id},function(data){
                slideMsgInfo(data.msg);
                getDataList();
            });
    });

}
/**
 * 批量删除拦截器
 * @returns {boolean}
 */
function mulDelDataFilter(){
    return true;
}
//批量删除
function mulDelData(){
    if(!mulDelDataFilter()){
        return false;
    }
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示","确定删除选中的"+rows.length+"条",function(r){
        JSON.stringify();
        if(r==true){
            var ids=new Array();
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
            ajaxJson(actionUrl+"?muldel",jQuery.param({ids:ids},true),function(data){
                slideMsgInfo(data.msg);
                getDataList();
            });
        }
    });

}
/**
 * 删除数据拦截器
 * @param id
 * @returns {boolean}
 */
function delFilter(id){
    return true;
}
/**
 * 删除数据
 * @param id
 */
function del(id){
    if(!delFilter(id)){
        return false;
    }
    jQuery.messager.confirm("提示","确定删除?",function(r){
        if(r==true){
            ajaxJson(actionUrl+"?del",{ids:id},function(data){
                if(assertAjaxSuccess(data))
                    slideMsgInfo(data.msg);
                getDataList();
            });
        }
    });
}
/**
 *批量删除拦截器
 * @returns {boolean}
 */
function delsFilter(){
    return true;
}
/**
 * 批量删除
 * @returns {boolean}
 */
function dels(){
    if(!delsFilter()){
        return false;
    }
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示","确定删除选中的"+rows.length+"条",function(r){
        if(r==true){
            var ids=new Array();
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
            ajaxJson(actionUrl+"?del",jQuery.param({ids:ids},true),function(data){
                if(assertAjaxSuccess(data))
                    slideMsgInfo(data.msg);
                getDataList();
            });
        }
    });
}
/**
 * 显示新增、更新页面拦截器
 * @param isUpdate
 * @returns {boolean}
 */
function showUpdateViewFilter(isUpdate){
    return true;
}
/**
 * 显示新增、更新页面
 * @param isUpdate
 * @returns {boolean}
 */
function showUpdateView(isUpdate){
    if(!showUpdateViewFilter(isUpdate)){
        return false;
    }
    var url=actionUrl+"?update";
    if(isUpdate){
        var row=$("#dataTable").datagrid("getChecked");
        if(row.length==0){
            alertMsg("请选择条目");
            return false;
        }
        if(row.length>1){
            alertMsg("只能选择一条!");
            return false;
        }
        url+="&id="+row[0].id;
        $("#editDia").dialog("setTitle","更新数据");
    }else{
        $("#editDia").dialog("setTitle","新增数据");
    }
    $("#editFrame").attr("src",url);
    openEasyuiDialog("editDia");
}
/**
 * 显示详情页面拦截器
 * @returns {boolean}
 */
function showDetailViewFilter(){
   return true;
}
/**
 * 显示详情页面
 * @returns {boolean}
 */
function showDetailView(){
    if(!showDetailViewFilter()){
        return true;
    }
    var url=actionUrl+"?detail";
    var row=$("#dataTable").datagrid("getChecked");
        if(row.length==0){
            alertMsg("请选择条目");
            return false;
        }
        if(row.length>1){
            alertMsg("只能选择一条!");
            return false;
        }
        url+="&id="+row[0].id;
        $("#editDia").dialog("setTitle","数据详情");
    $("#editFrame").attr("src",url);
    openEasyuiDialog("editDia");
}
/**
 * 加载详情数据
 * @param id
 * @param subTableNames
 */
function loadDetail(id,subTableNames){
    $("#editForm").form("reset");
    if(jQuery.trim(id).length>0){
        ajaxJson(actionUrl+"?get",{id:id},function(data){
            if(assertAjaxSuccess(data)){
                $("#editForm").form("load",data);
                for(var i=0;i<subTableNames.length;i++){
                    var subTableName=subTableNames[i];
                    ajaxJson(actionUrl+"?subList",{"subTableName":subTableName,"pid":id},function(data1){
                        $("#"+subTableName).datagrid("loadData",data1);
                    });
                }
            }
        });
    }
}
/**
 * 保存数据拦截器
 * @returns {boolean}
 */
function saveDataFilter(){
    return true;
}
/**
 * 保存数据
 * @returns {boolean}
 */
function saveData(datagridIdArry){
    if(!saveDataFilter()){
        return false;
    }
    if(datagridIdArry&&datagridIdArry.length>0){
        if(!$("#editForm").form("validate")||!endEdit())
            return false;
    }else{
        if(!$("#editForm").form("validate"))
            return false;
    }
    var d="";
    if(datagridIdArry.length>0)
        d=JSON.stringify(getOneToManyData("editForm",datagridIdArry));
    else
        d=formData2Json("editForm");
    ajaxJson(actionUrl+"?save",{jsonData:d},function(data){
        if(assertAjaxSuccess(data))
            slideMsgInfo(data.msg);
        loadDetail(data.dataMap.id,datagridIdArry);
    });
}


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
        data[toFieldName(datagridIdArry[i])+"s"]=rows;
    }
    return data;
}
function toFieldName(str){
    var str2="";
    var flag=false;
    for(var i=0;i<str.length;i++){
        if(str.charAt(i)=="_"&&i>0){
            flag=true;
        }else{
            if(flag){
                str2+=str.charAt(i).toUpperCase();
                flag=false;
            }else{
                str2+=str.charAt(i);
            }
        }
    }
    return str2;
}
//文件列表
function showFileList(filePath,showType,type){
   // var url=context+"/file.do?toupload&&actionUrl="+actionUrl+"&&filePath="+encodeURI(filePath);
  /*  if(showType)
        url+="&&showType="+showType;
    if(type)
        url+="&&type="+type;
    showFrameDialog("editDia","editFrame",url);*/
    window.top.showFileList(filePath,showType,type);
}
/**
 *查看文件
 * @param filePath
 */
function viewFile(filePath){
    var file=context+"/file.do?view&&filePath="+encodeURI(filePath);
   // showFrameDialog("editDia","editFrame",file);
    window.top.viewFile(filePath);
}
//重置
function resetForm(){
    $("#searchForm").form("reset");
}

function showPicButton(cj,ywid,ywlx){
    return "<a href='javascript:void(0);' onclick=\"showPic('"+cj+"','"+ywid+"','"+ywlx+"')\">[查看]</a>";
}


function showPic(colName,ywid,ywlx){
   var url=context+"/archives_cj/cj.do?showCj&id="+ywid+"&ywlx="+ywlx;
    $("#editFrame").attr("src",url);
    $("#editDia").dialog("setTitle","查看");
    openEasyuiDialog("editDia");
}

//采集
function toCj(colName,ywIdColName,lxName){
    var data=formData2Array("editForm");
    var id = data["ywid"];
    var ywlx = data["ywlx"];
    $("#"+colName).val(ywlx);
    if($.trim(id).length==0){
        alertMsg("业务ID为空，不能采集!");
        return false;
    }
    if($.trim(ywlx).length==0){
        alertMsg("业务类型为空，不能采集!");
        return false;
    }
    saveData();
    var url=context+"/front/yw_file.do?showCj&id="+id+"&ywlx="+ywlx;
    window.open(url);
//    window.showModalDialog(url,
//     window,"dialogWidth=950px;dialogHeight=600px;center=yes");
}
/**
 * 装盒
 * @param entityName
 * @param colName
 * @param colValue
 * @returns {boolean}
 */
function showArchivesBox(){
    var entityName=$("#_tableName").val();
    var colName="sfzh";
    var colValue="1";
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }

    var ids=new Array();
    ids.push(entityName);
    ids.push(colName);
    ids.push(colValue);
    for(var i=0;i<rows.length;i++){
        ids.push(rows[i].id);
    }

    var sjid=null;
    var flag=1
    var url="businesscore/kfgl_kfxx.do?findKFList&&sjid="+sjid+"&&ids="+ids.toString()+"&&flag="+flag;
    window.parent.showBussinessWindow("立体库房",url);
}
/**
 * 收藏
 * @param flage
 * @returns {boolean}
 */
function showFavorite(){
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示","确定收藏选中的"+rows.length+"条",function(r){
        if(r==true){
            var ids=new Array();
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
            var tablename=rows[0].table_name;
            ajaxJson(context+"/businesscore/dagl_scj.do?favorite", $.param({tablename:tablename,ids:ids},true),function(data){
                alertMsg(data.msg);
                getDataList();
            });
        }
    });
}
/**
 * 取消收藏
 */
function cancel(){
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示","确定将选中的"+rows.length+"条取消收藏",function(r){
        if(r==true){
            var ids=new Array();
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
            var tablename=$("#_tableName").val();
            ajaxJson(context+"/businesscore/dagl_scj.do?cancel", $.param({tablename:tablename,ids:ids},true),function(data){
                alertMsg(data.msg);
                var jj=data.dataMap.ids2;
                var idArray=new Array();
                for(var i=0;i<jj.length;i++){
                    idArray.push(jj[i]);
                }
                $("#ids").val(idArray);
                loadDataList();
            });
        }
    });
}

//还原
function reduction(){
    var rows=$("#dataTable").datagrid("getChecked");
    if(rows.length==0){
        alertMsg("请选择条目");
        return false;
    }
    jQuery.messager.confirm("提示","确定将选中的"+rows.length+"条还原",function(r){
        if(r==true){
            var ids=new Array();
            for(var i=0;i<rows.length;i++){
                ids.push(rows[i].id);
            }
            var tablename=$("#_tableName").val();
            ajaxJson(context+"/businesscore/dagl_hsz.do?reduction", $.param({tablename:tablename,ids:ids},true),function(data){
                alertMsg(data.msg);
                getDataList();
            });
        }
    });
}
//彻底删除
function deleteData() {
    var rows = $("#dataTable").datagrid("getChecked");
    if (rows.length == 0) {
        alertMsg("请选择条目");
        return false;
    }

    jQuery.messager.confirm("提示", "确定将选中的" + rows.length + "条彻底删除", function (r) {
        if(r){
            var ids = new Array();
            for (var i = 0; i < rows.length; i++) {
                if(!checkDataState(rows[i])){
                    alertMsg("部分数据不可操作，请重新选择！");
                    return false;
                }
                ids.push(rows[i].id);
            }
            var tablename = $("#_tableName").val();
            ajaxJson(context + "/businesscore/dagl_hsz.do?clear", $.param({tablename: tablename, ids: ids}, true), function (data) {
                alertMsg(data.msg);
                getDataList();
            });
        }

    });
}
//检查数据状态是否可操作
function checkDataState(row){
    var lj=row['sflj'];
    if(!isNaN(lj)&&lj<0){
        return false;
    }else
        return true;
}
//获取库房名称
function getKfName(kfId){
    var wz="";
    $.ajax({
        url:context+"/kfgl.do?responsePosition",
        type:"post",
        data:{wzhid:kfId},
        dataType:"json",
        cache:true,
        async:false,
        success:function(data){
            wz= data;
        }
    });
    return wz;
}

//显示位置号列表
function detailePosition(wzhid){
    var tablename=$("#_tableName").val();
    ajaxJson(context+"/front/kfgl.do?findKfwzId",{wzhid:wzhid},function(data){
        var sjid=data.dataMap.ghid;
        var chid=data.dataMap.chid;
        var jgid=data.dataMap.jgid;
        var kfid=data.dataMap.kfid;
        var url=context+"/front/businesscore/kfgl_ghxx.do?intoWZH&&sjid="+sjid+"&&chid="+chid+"&&jgid="+jgid+"&&kfid="+kfid;
        $("#editFrame").attr("src",url);
        $("#editDia").dialog("setTitle","机柜管理");
        openEasyuiDialog("editDia");
    });

}
function checkOperationRecord(id){
    var url=context+"/front/businesscore/kfgl_ykhistory.do?showYKHistory&&daid="+id;
    $("#editFrame").attr("src",url);
    $("#editDia").dialog("setTitle","查看操作记录");
    openEasyuiDialog("editDia");
}

//显示位置
function buttonPosition(value,row,index){
    var button="";
    var positionname="";
    var url=context+"/front/kfgl.do?responsePosition&&wzhid="+row.position;
    $.ajax({
        type:"get",
        url:url,
        dataType:"json",
        async: false,
        success:function(data){
            if(data!=null && data!=""){
                positionname=data;
            }
        }
    });
    button="<a href='#' onclick=\"detailePosition('"+row.position+"')\" >"+positionname+"</a>";
    return button;
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