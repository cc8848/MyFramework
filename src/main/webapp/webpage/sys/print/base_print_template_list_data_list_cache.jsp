<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<div class="edit_list" id="page1">
  <table id="dataTable"  class="easyui-datagrid" title="数据列表"  align="center" data-options="toolbar:'#tools'">
     <thead>
      <tr>
         <th field="id" data-options="checkbox:true"></th>
         <th field='mName' data-options="width:100" >模板名称</th>
         <th field='mTablename' data-options="width:100" >表名</th>
         <th field='mField' data-options="width:100" >字段key</th>
         <th field='mValue' data-options="width:100" >字段值</th>
         <th field="cz"   data-options="formatter: function(value,row,index){ return buttonCz(value,row,index);}" >操作</th>
        </tr>
      </thead>
  </table>
</div>