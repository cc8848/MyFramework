package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;

/**
 * Created by lenovo on 2014/12/11.
 */
public class EasyuiCjViewer implements Viewer{
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        String lxName=fieldBean.getFieldDefault();
        String[] names={"",""};
        if(lxName!=null&&lxName.split(",").length==2){
            names=lxName.split(",");
        }
        String fieldName=StringUtil.toFieldName(fieldBean.getFieldName());
        return "<a href=\"javascript:void(0)\" class=\"download_button\" onclick=\"toCj('"+fieldName+"','"+StringUtil.toFieldName(names[0])+"','"+StringUtil.toFieldName(names[1])+"')\">[采集]</a>"+
                "<input type='hidden' name='"+fieldName+"' id='"+fieldName+"' /> ";
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
      //  showPicButton('ywbh',row.ywbh+'tqtime'+row.blsj);}"  >查看</th>
    //    String options=",formatter:function(value,row,index){return showPicButton('"+StringUtil.toFieldName(fieldBean.getFieldName())+"',value);}";
        String options=",formatter:function(value,row,index){return showPicButton(value,row.ywid,row.ywlx);}";
        return "<th field='"+ StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100"+options+"\"  >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        return "";
    }

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        //return "<a href=\"javascript:void(0)\" onclick=\"toCj()\">[查看照片]</a>";
        return "";
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        return "";
    }
}
