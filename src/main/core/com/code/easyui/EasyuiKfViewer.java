package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;

/**
 * Created by lenovo on 2014/12/20.
 */
public class EasyuiKfViewer implements Viewer {
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        return "";
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        return "<th field='"+ StringUtil.toFieldName(fieldBean.getFieldName())+"'   data-options=\"formatter: function(value,row,index){ return buttonPosition(value,row,index);}\" >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        return "";
    }

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return "";
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        return "";
    }
}
