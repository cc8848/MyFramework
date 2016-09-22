package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * @author zzl
 *         Date:2014-08-05
 */
public class EasyuiDateBoxViewer  implements Viewer{
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        return getInput(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean.getTextLength(),!fieldBean.isNull(),true,false,false);
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        String editor="editor:{type:'datebox',formatter:myformatter,parser:myparser";
        if(!fieldBean.isNull())
            editor+=",required:true";
        editor+="}";
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' sortable=\"true\" data-options=\"width:100\"  >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        if("group".equals(fieldBean.getQueryModel())){
            return getInput(StringUtil.toFieldName(fieldBean.getFieldName())+"_begin",fieldBean.getTextLength(),false,true,true,true)+" 到: "+
                   getInput(StringUtil.toFieldName(fieldBean.getFieldName())+"_end",fieldBean.getTextLength(),false,true,true,true);
        }else{
            return getInput(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean.getTextLength(),false,true,false,true);
        }
    }
    private String getInput(String name,int size,boolean isRequired,boolean enable,boolean groupSearch,boolean isSearch){
        String disabled="disabled='disabled'";
        if(enable)
            disabled="";
        String style="";
        if(isSearch)
            style="style=\"width:190px\"";
        if(groupSearch)
            style="style=\"width:86px\"";
        StringBuffer buffer=new StringBuffer("<input class='easyui-datebox' "+disabled+"  "+style+" name='");
        buffer.append(name)
                .append("' ");
        if(size>0)
            buffer.append("style='width:")
                    .append(size)
                    .append("' ");
        String options="formatter:myformatter,parser:myparser,";
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

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return getInput(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean.getTextLength(),!fieldBean.isNull(),false,false,false);
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        String editor="editor:{type:'datebox',formatter:myformatter,parser:myparser";
        if(!fieldBean.isNull())
            editor+=",required:true";
        editor+="}";
        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100,"+editor+"\"  >"+fieldBean.getFieldContent()+"</th>";
    }
}
