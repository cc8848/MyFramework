/**
 * 初始化上传控件
 * @param inputId input type='file'的 id
 * @param uploadDir 上传到服务器的文件夹路径
 * @param fileName 上传文件重命名
 * @param func 上传完成后回调
 */
function initUpload(inputId,uploadDir,fileName,fileCount,complateFunc){
    $("#"+inputId).uploadify({
        'swf': context+'/plug-in/uploadify/uploadify.swf',
        'uploader': context+'/file.do?upload',
        'formData': {'dirPath':uploadDir,fileName:fileName},
        'auto': false,
        'multi': true,
        'fileObjName':'file',
        'buttonText':'浏览...',
        'progressData': 'all',
        'removeCompleted' : true,
        'height':20,
        'width':100,
        'cancelImage':context+'/plug-in/uploadify/uploadify-cancel.png',
        'fileSizeLimit':1024*1014*1024
    });

}
/**
 * 上传文件
 * @param inputId
 */
function uploadFile(inputId){
    $("#"+inputId).uploadify("upload",'*');
}
/**
 * 上传结束时调用
 * @param fileCount 文件选择框数量
 * @param completeCount 已完成文件选择框数量
 * @param func  所有文件选择框上传完毕后执行函数
 */
function uploadComplete(inputId,fileCount,completeCount,func){
    $("#"+inputId).uploadify("settings",'onAllComplete',function(){
        completeCount++;
        if(fileCount==completeCount)
            func();
    });
}
/**
 * 设置上传文件夹路径
 * @param inputId
 * @param dirPath
 */
function setDirAndFileName(inputId,dirPath,fileName){
    $("#"+inputId).uploadify("settings",'formData',{dirPath:dirPath,fileName:fileName});
}

function initFileCount(inputId,f1,f2,f3){
    $("#"+inputId).uploadify("settings",'onSelect',function(file){
        f1();
    });
    $("#"+inputId).uploadify("settings",'onCancel',function(file){
        f2();
    });
    $("#"+inputId).uploadify("settings",'onUploadComplete',function(file){
        f3();
    });
}
