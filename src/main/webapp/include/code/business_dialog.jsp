<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div id="editDia" title="更新" class="easyui-dialog" style="width:850px;height:500px;" data-options=" closed:true,
            onResize:function(){
               $(this).dialog('center');
            },onClose:function(){getDataList();$('#editFrame').attr('src','');}">
    <iframe scrolling="auto" id="editFrame"  frameborder="0"  src="" style="width:99%;height:99%;"></iframe>
</div>



