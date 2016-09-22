package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @author zzl
 *         Date:2014-08-05
 */
public class EasyuiNumBerBoxViewer implements Viewer {
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),fieldBean.getTextLength(),!fieldBean.isNull(),fieldBean.getFieldValidType(),fieldBean.getTextPrecision(),true,false);
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        String editor="editor:{type:'numberbox'";
        if(fieldBean.getTextPrecision()>0)
            editor+=",precision:'"+fieldBean.getTextPrecision()+"'";
        if(!fieldBean.isNull())
            editor+=",required:true";
        editor+="}";
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' sortable=\"true\" data-options=\"width:100\" >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),fieldBean.getTextLength(),false,null,0,true,true);
    }
    private String getInput(String name,int size,boolean isRequired,String validType,int precision,boolean enable,boolean isSearch){
        String disabled="disabled='disabled'";
        if(enable)
            disabled="";
        String style="";
        if(isSearch)
            style=" style=\"width:190px\" ";
        StringBuffer buffer=new StringBuffer("<input class='easyui-numberbox' "+style+disabled+" name='");
        buffer.append(StringUtil.toFieldName(name))
                .append("' ");
        if(size>0)
            buffer.append("style='width:")
                    .append(size)
                    .append("px' ");
        if(precision>0){
            buffer.append(" precision='")
                  .append(precision)
                  .append("' ");
        }
        String options="";
        if(isRequired&&!isSearch)
            options+="required:true,";
        if(StringUtils.isNotBlank(validType))
            options+="validType:"+validType;
        if(options.endsWith(","))
            options=options.substring(0,options.length()-1);
        if(StringUtils.isNotBlank(options))
            buffer.append(" data-options=\"")
                    .append(options)
                    .append("\"");
        buffer.append(" />");
        return buffer.toString();
    }
    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),fieldBean.getTextLength(),false,null,0,false,false);
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        String editor="editor:{type:'numberbox'";
        if(fieldBean.getTextPrecision()>0)
            editor+=",precision:'"+fieldBean.getTextPrecision()+"'";
        if(!fieldBean.isNull())
            editor+=",required:true";
        editor+="}";
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100,"+editor+"\" >"+fieldBean.getFieldContent()+"</th>";
    }
}
