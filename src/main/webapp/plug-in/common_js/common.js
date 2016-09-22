//右下角消息提示
function slideMsg(title,msg){
    $.messager.show({
        title:title,
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
}

/**
 * 右下角提示信息
 * @param msg
 */
function slideMsgInfo(msg){
    $.messager.show({
        title:"提示",
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
}
/**
 * ajaxpost请求 返回json
 * @param url
 * @param data
 * @param f
 */
function ajaxJson(url,data,f){
    $.ajax({
            url:url,
            type:"post",
            data:data,
            dataType:"json",
            cache:false,
            success:function(data){
            f(data);
        }
    });
}

/**
 * ajaxpost请求 返回json
 * @param url
 * @param data
 * @param f
 */
function ajaxJsonWithCache(url,data,f){
    $.ajax({
        url:url,
        type:"post",
        data:data,
        dataType:"json",
        cache:true,
        success:function(data){
            f(data);
        }
    });
}
/**
 * 非ajax获取实体对象
 * @param url
 * @param id
 * @returns {*}
 */
function getDetail(url,id){
    var o=null;
    $.ajax({
        url:url,
        type:"get",
        dataType:"json",
        data:{id:id},
        async:false,
        cache:false,
        success:function(data){
            o=data;
        }
    });
    return o;
}
/**
 * 打开包含form的对话框，并已data填充
 * @param formId
 * @param diaId
 * @param data
 */
function showFormDataDia(formId,diaId,data){
    $("#"+formId).form("reset");
    if(data){
        $("#"+formId).form("load",data);
    }
    $("#"+diaId).dialog("open").dialog("hcenter").dialog("vcenter");
}
/**
 * 返回成功时，关闭对话框并提示
 * @param ajax
 * @param diaId
 */
function closeDiaWithMsgWhenSuccess(diaId,ajax){
    if(ajax.success&&$("#"+diaId).length>0){
        $("#"+diaId).dialog("close");
    }

    slideMsgInfo(ajax.msg);
}

/**
 * 表单验证
 * @param formId
 * @returns {*|jQuery}
 */
function  validForm(formId){
    return $("#"+formId).form("validate");
}
/**
 * alert消息
 * @param msg
 */
function alertMsg(msg){
    //window.top.$.messager.alert('提示',msg);
    window.top.$.messager.show({
        title:'提示',
        msg:msg,
        timeout:3000,
        style:{bottom:'',right:''}
    })
}

function showFrameDialog(dialogId,frameId,frameUrl){
    document.getElementById(frameId).src=frameUrl;
    $("#"+dialogId). dialog("open").dialog("vcenter");//.dialog("hcenter");
}

/**
 * 在index页面打开此页面上的对话框
 * @param title
 * @param frameUrl
 */
function showBussinessWindow(title,frameUrl){
    window.top.openBussinessChildMenu(title,frameUrl);
}

/**
 * 日期格式化 如2014-01-01
 * @param date
 */
function dateFormat(val){
    var date=new Date(val);
    return date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
}

//取浏览器宽度百分比
function fixWidth(percent){
    return document.body.clientWidth * percent ;
}
//群浏览器高度百分比
function fixHeight(percent){
    return document.body.clientHeight * percent ;
}
function myformatter(d){
    var date=new Date(d);
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}
function myparser(s){
    if (!s) return new Date();
    var ss = (s.split('-'));
    var y = parseInt(ss[0],10);
    var m = parseInt(ss[1],10);
    var d = parseInt(ss[2],10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
        return new Date(y,m-1,d);
    } else {
        return new Date();
    }
}
/**
 * 获取字典值
 * @param dicKey
 * @param dicValue
 * @param dicTable
 * @param val
 * @returns {*}
 */
function getDicValue(dicKey,dicValue,dicTable,val){
    var o=null;
    $.ajax({
        url:context+"/sys/dic.do?getDicValue",
        type:"post",
        dataType:"json",
        data:{dicKey:dicKey,dicValue:dicValue,dicTable:dicTable,dicKeyValue:val},
        async:false,
        cache:false,
        success:function(data){
            o=data;
        }
    });
    return o;
}
/**
 * 调整frame和dialog大小
 * @param frameId
 * @param diaId
 */
function iFrameHeight(frameId,diaId) {
    var ifm = document.getElementById(frameId);
    var subWeb = document.frames ? document.frames[frameId].document : ifm.contentDocument;
    if (ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight;
    }
    $("#"+diaId).dialog("resize",{height:(parseInt(ifm.height)+200)}).dialog("hcenter").dialog("vcenter");
}
function iFrameHeightSize(frameId,diaId,size) {
    var ifm = document.getElementById(frameId);
    var subWeb = document.frames ? document.frames[frameId].document : ifm.contentDocument;
    if (ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight;
    }
    $("#"+diaId).dialog("resize",{height:(parseInt(ifm.height)+size)}).dialog("hcenter").dialog("vcenter");
}
/**
 * 设置easyuiwindow的size
 * @param frameId
 * @param windowId
 */
function windowFrameSize(frameId,windowId){
    var ifm = document.getElementById(frameId);
    var subWeb = document.frames ? document.frames[frameId].document : ifm.contentDocument;
    if (ifm != null && subWeb != null) {
        ifm.height = subWeb.body.scrollHeight;
        ifm.width=subWeb.body.scrollWidth;
    }
    $("#"+windowId).window("resize",{height:(parseInt(ifm.height)),width:(parseInt(ifm.width))}).window("hcenter").window("vcenter");
}
/**
 * 打开easyui dialog 并居中显示
 * @param dialogid
 */
function openEasyuiDialog(dialogid,widthPercent,heightPercent){
    $("#"+dialogid).dialog("open").dialog("hcenter");
    if(widthPercent&&heightPercent)
        $("#"+dialogid).dialog("resize",getPanelSize(widthPercent,heightPercent));//.dialog("vcenter");
}
/**
 * 打开easyuiwindow
 */
function openEasyuiWindow(windowId){
    $("#"+windowId).window("open").window("hcenter");//.window("vcenter");
}
/**
 * 停止编辑easyui datagridRow
 * @param id
 * @returns {boolean}
 */
function endEditDatagridRow(id){
    var  editIndex = $('#'+id).datagrid('getRows').length-1;
    for(var i=0;i<=editIndex;i++){
        if($('#'+id).datagrid('validateRow', i))
            $('#'+id).datagrid('endEdit', i);
        else
            return false;
    }
    return true;
}
/**
 * 将选中的datagrid行置为编辑状态
 * @param id
 * @returns {boolean}
 */
function editDatagridRow(id){
    var rows=$('#'+id).datagrid("getChecked");
    for(var i=0;i<rows.length;i++){
        var index= $('#'+id).datagrid('getRowIndex', rows[i]);
        $('#'+id).datagrid('beginEdit', index);
    }
}
/**
 * 删除datagridrow
 * @param url
 * @param paramName
 * @param datagridId
 * @param f
 * @returns {boolean}
 */
function deleteDatagridRow(url,datagridId,f){
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
                ajaxJson(url, $.param({ids:ids},true),f);
            }
        })
    }
}
//表单内容转json
function formData2Json(formId){
    return JSON.stringify(formData2Array(formId));
}
//表单内容转数组
function formData2Array(formId){
    var data={};
    var formData=$("#"+formId).serializeArray();
    for(var i=0;i<formData.length;i++){
        data[formData[i]["name"]]=formData[i]["value"];
    }
    return data;
}
function formObjData2Array(formObj){
    var data={};
    var formData=$(formObj).serializeArray();
    for(var i=0;i<formData.length;i++){
        data[formData[i]["name"]]=formData[i]["value"];
    }
    return data;
}
//数组转json
function array2Json(array){
    return JSON.stringify(array)
}
//date字符串（yyyy-mm-dd）转iso8601
function date2iso8601(dateStr){
    return dateStr+"T00:00:00";
}
//date字符串（yyyy-mm-dd）转iso8601，转为当日的最后时刻
function date2iso8601After(dateStr){
    return dateStr+"T23:59:59";
}
//date字符串（yyyy-mm-dd hh:mm:ss）转iso8601
function date2iso8601ForLongType(dateStr){
    //2014-12-26 13:53:00
    var newStr = dateStr.replace(' ','T');
    return newStr;
}
function onDrag(e){
    alert(e);
    var d = e.data;
    if (d.left < 0){d.left = 0}
    if (d.top < 0){d.top = 0}
    if (d.left + $(d.target).outerWidth() > $(d.parent).width()){
        d.left = $(d.parent).width() - $(d.target).outerWidth();
    }
    if (d.top + $(d.target).outerHeight() > $(d.parent).height()){
        d.top = $(d.parent).height() - $(d.target).outerHeight();
    }
}
//自动调整列宽
function datagridColumnAutoSize(datagridId){
    var co= $("#"+datagridId).datagrid("getColumnFields");
    for(var i=0;i<co.length;i++){
        $("#"+datagridId).datagrid("autoSizeColumn",co[i]);
    }
}
function getEasyuiDatagridEditor(datagridId,index,field){
  return  $("#"+datagridId).datagrid("getEditor",{index:index,field:field});
}
/**
 * 更新列值
 * @param tableName
 * @param colName
 * @param colValue
 */
function colUpdate(tableName,colName,colValue,idArray) {
            ajaxJson(context + "/identify.do?identify", $.param({tableName: tableName, colName: colName, colValue: colValue, ids: idArray}, true), function (data) {
                alertMsg(data.msg);
                getDataList();
      });
}
//获取easyuipanel尺寸
function getPanelSize(widthPercent,heightPercent){
    return {width:fixWidth(widthPercent),height:fixHeight(heightPercent)};
}
/**
 * 在父窗体内部打开对话框
 * @param dialogId
 */
function openEasyuiInnerDialog(dialogId){
    openEasyuiDialog(dialogId,0.55,0.85);
}
/**
 * 判断ajax请求是否成功！
 * @param obj
 * @returns {boolean}
 */
function assertAjaxSuccess(obj,errorMsg){

    if(!errorMsg)
        errorMsg=null;
    var isSuccess=true;
    if(obj.hasOwnProperty("success")){
            isSuccess=obj.success;
    }
    if(errorMsg==null&&obj.hasOwnProperty("msg")&&obj.msg!=null){
        errorMsg=obj.msg;
    }
    if(!isSuccess&&errorMsg!=null){
        alertError(errorMsg);
    }
    return isSuccess;
}
//错误提示！
function alertError(msg){
    $.messager.alert('请注意',msg,'error');
}