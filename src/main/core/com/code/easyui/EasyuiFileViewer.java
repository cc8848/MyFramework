package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * easyui文件上传
 * @author zzl
 * Date:2014-09-11
 */
public class EasyuiFileViewer implements Viewer {
    @Override
    public String getInputString(TableFieldBean fieldBean) {
       return getInput(fieldBean.getFieldName());
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        return "<th field='"+ StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100,formatter:function(value,row,index){return showViewFile(value,row,'"+StringUtil.toFieldName(fieldBean.getFieldName())+"','file');;}\" >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {

        return "";
    }
    private String getInput(String name){
       return " <input type=\"hidden\"   id=\""+name+"\" name='"+name+"'  />\n" +
                "<a href=\"#\" class=\"download_button\" onclick=\"fileList('"+name+"',$('#"+name+"').val(),'file')\">[附件]</a>";

    }

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName());
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        return "<th field='"+ StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100,formatter:function(value,row,index){return list_file_upload(row.id,'"+StringUtil.toFieldName(fieldBean.getFieldName())+"',value,'file');}\" >"+fieldBean.getFieldContent()+"</th>";
    }
}
