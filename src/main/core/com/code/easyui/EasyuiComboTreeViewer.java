package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Created by lenovo on 2015/1/19.
 */
public class EasyuiComboTreeViewer  implements Viewer{
    private static String dicStr="${pageContext.request.contextPath}/sys/dic.do?";
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),!fieldBean.isNull(),fieldBean.getTextLength(),fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),true,false);
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        String options="";
        String editor="editor:{type:'combotree'";
        if(!fieldBean.isNull())
            editor+=",required:true";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>0){
            options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
            for(int i=1;i<list.size();i++){
                options+=" else if(value=='"+list.get(i).getValue()+"') return '"+list.get(i).getText()+"';";
            }
            options+=" else return ''; }";
            editor+=",options:{valueField:'cKey',textField:'cValue',data:"+ComboboxOptionUtil.getDataByList(list)+"}";

        }else{
            options="formatter: function(value,row,index){ return getDicValue('"+fieldBean.getDicKey().replace("'","\\'")+"','"+fieldBean.getDicValue()+"','"+fieldBean.getDicTable()+"',value);}";
            editor+=",options:{valueField:'cKey',textField:'cValue'd:'text',method: 'get',";
            editor+="url:'"+dicStr+"dicTreeList&dicKey="+fieldBean.getDicKey().replace("'","\\'");
            if(StringUtils.isNotBlank(fieldBean.getDicValue()))
                editor+="&dicValue="+fieldBean.getDicValue();
            if(StringUtils.isNotBlank(fieldBean.getDicTable()))
                editor+="&dicTable="+fieldBean.getDicTable();
            editor+="'}";
        }
        editor+="}";
        // options+=","+editor;
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' sortable=\"true\" data-options=\"width:100,"+options+"\" >"+fieldBean.getFieldContent()+"</th>";

    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),false,fieldBean.getTextLength(),fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),true,true);
    }

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),false,fieldBean.getTextLength(),fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),false,false);
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        String options="";
        String editor="editor:{type:'combotree'";
        if(!fieldBean.isNull())
            editor+=",required:true";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>0){
            options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
            for(int i=1;i<list.size();i++){
                options+=" else if(value=='"+list.get(i).getValue()+"') return '"+list.get(i).getText()+"';";
            }
            options+=" else return ''; }";
            editor+=",options:{valueField:'cKey',textField:'cValue',data:"+ComboboxOptionUtil.getDataByList(list)+"}";

        }else{
            options="formatter: function(value,row,index){ return getDicValue('"+fieldBean.getDicKey().replace("'","\\'")+"','"+fieldBean.getDicValue()+"','"+fieldBean.getDicTable()+"',value);}";
            editor+=",options:{valueField:'cKey',textField:'cValue',method: 'get',";
            editor+="url:'"+dicStr+"dicTreeList&dicKey="+fieldBean.getDicKey().replace("'","\\'");
            if(StringUtils.isNotBlank(fieldBean.getDicValue()))
                editor+="&dicValue="+fieldBean.getDicValue();
            if(StringUtils.isNotBlank(fieldBean.getDicTable()))
                editor+="&dicTable="+fieldBean.getDicTable();
            editor+="'}";
        }
        editor+="}";
        options+=","+editor;
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100,"+options+"\" >"+fieldBean.getFieldContent()+"</th>";

    }
    private String getInput(String name,boolean isRequired,int size,String dicKey,String dicValue,String dicTable,String defaultVal,boolean enable,boolean isSearch){
        String disabled="disabled='disabled'";
        if(enable)
            disabled="";
        String style="";
        if(isSearch)
            style=" style=\"width:190px\" ";
        StringBuffer buffer=new StringBuffer("<input class='easyui-combotree' "+style+disabled+" name='");
        buffer.append(StringUtil.toFieldName(name))
                .append("' ");
        if(size>0)
            buffer.append("style='width:")
                    .append(size)
                    .append("' ");
        String options="";
        if(StringUtils.isNotBlank(defaultVal)){
            options+="valueField:'value',textField:'text',data:"+defaultVal+",";
        }else{
            options="url:'"+dicStr+"dicTreeList&dicKey="+dicKey.replace("'","\\'");
            if(StringUtils.isNotBlank(dicValue))
                options+="&dicValue="+dicValue;
            if(StringUtils.isNotBlank(dicTable))
                options+="&dicTable="+dicTable;
          //  options+="' , method: 'get', valueField:'cKey',textField:'cValue',";
            options+="',";
        }
        if(isRequired&&!isSearch)
            options+="required:true,";
        if(options.endsWith(","))
            options=options.substring(0,options.length()-1);
        if(StringUtils.isNotBlank(options))
            buffer.append(" data-options=\"")
                    .append(options)
                    .append("\"");
        buffer.append(" />");
        return buffer.toString();
    }
}
