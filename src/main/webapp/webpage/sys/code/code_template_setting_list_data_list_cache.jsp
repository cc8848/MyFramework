<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>

  <table id="dataTable" style="height: 300px;" class="easyui-datagrid" title="数据列表"  align="center" data-options="toolbar:'#tools'">
     <thead>
      <tr>
         <th field="id" data-options="checkbox:true"></th>
         <th field='javaPath' data-options="width:400" >生成类路径</th>
         <th field='viewPath' data-options="width:400" >生成视图路径</th>
         <th field="cz"   data-options="formatter: function(value,row,index){ return buttonCz(value,row,index);}" >操作</th>
        </tr>
      </thead>
  </table>
