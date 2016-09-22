<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

  <table id="dataTable"  class="easyui-datagrid" title="数据列表"  align="center" data-options="toolbar:'#tools'">
     <thead>
      <tr>
         <th field="id" data-options="checkbox:true"></th>
         <th field='moduleName' data-options="width:100" >组件名称</th>
         <th field='moduleCode' data-options="width:100" >组件代号</th>
         <th field='defaultShow' data-options="width:100,formatter:function(value,row,index){ if(value=='1') return '是'; else return '否';}"  >默认显示</th>
         <th field="cz"   data-options="formatter: function(value,row,index){ return buttonCz(value,row,index);}" >操作</th>
        </tr>
      </thead>
  </table>
