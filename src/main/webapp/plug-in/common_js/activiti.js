/**
 * 获取流程表单变量
 * @param formId
 */
function getFormData(formId,processId){
    ajaxJson(context+"/activiti/history.do?form",{processId:processId},function(data){
        $("#"+formId).form("load",data);
    });
}
/**
 * 获取流程任务变量
 * @param taskFormId
 * @param processId
 * @param taskId
 */
function getTaskData(taskFormId,processId,taskId){
    ajaxJson(context+"/activiti/history.do?taskData",{processId:processId,taskId:taskId},function(data){
        $("#"+formId).form("load",data);
    });
}
/**
 * 完成任务
 * @param formId
 * @param taskId
 */
function completeTaskWithFormData (formId,taskId){
    ajaxJson(context+"/activiti/task.do?completeTaskWithFormData",{globalJsonData:formData2Json(formId),taskId:taskId},function(data){
        window.top.alertMsg(data.msg);
        if(data.success){
            window.parent.closeTaskDia();
        }
    });
}
function completeTaskWithThisFormData(formChildObj,taskId,globalArray,globalDataArray){
    var formData;
    if(formChildObj){
        var form=$(formChildObj).parents("form")[0];
        formData=formObjData2Array(form);
    }
    var globalData={};
    if(globalArray){
        for(var i=0;i<globalArray.length;i++){
            globalData[globalArray[i]]=formData[globalArray[i]];
        }
    }
    if(globalDataArray){
        $.extend(globalData,globalDataArray);
    }
    var jsonData="";
    if(formData)
        jsonData=array2Json(formData);
    var globalJsonData="";
    if(globalData)
        globalJsonData=array2Json(globalData);
    ajaxJson(context+"/activiti/task.do?completeTaskWithFormData",{jsonData:jsonData,globalJsonData:globalJsonData,taskId:taskId},function(data){
        slideMsgInfo(data.msg);
        if(data.success){
            window.parent.closeTaskDia();
        }
    });
}
function completeTaskWithThisFormDataAndButton(obj,taskId,buttonName,buttonValue,globalArray){
    var form=$(obj).parents("form")[0];
    var formData=formObjData2Array(form);
    formData[buttonName]=buttonValue;
    var globalData={};
    globalData[buttonName]=buttonValue;
    if(globalArray){
        for(var i=0;i<globalArray.length;i++){
            globalData[globalArray[i]]=formData[globalArray[i]];
        }
    }
    ajaxJson(context+"/activiti/task.do?completeTaskWithFormData",{jsonData:array2Json(formData),globalJsonData:array2Json(globalData),taskId:taskId},function(data){
        window.parent.alertMsg(data.msg);
        if(data.success){
            window.parent.closeTaskDia();
        }
    });
}
//通过按钮条件完成任务
function completeTaskWithFormDataAndButton(formId,taskId,buttonName,buttonValue){
    var data=formData2Array(formId);
    data[buttonName]=buttonValue;
    ajaxJson(context+"/activiti/task.do?completeTaskWithFormData",{globalJsonData:array2Json(data),taskId:taskId},function(data){
        window.top.alertMsg(data.msg);
        if(data.success){
            window.parent.closeTaskDia();
        }
    });
}
//开始流程
function startTask(jsonData,processId,f){
    ajaxJson(context+"/activiti/task.do?startTask",{jsonData:jsonData,processId:processId},function(data){
        f(data);
    });
}