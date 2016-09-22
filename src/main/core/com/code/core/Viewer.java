package com.code.core;

import com.code.entity.TableFieldBean;

/**
 * 视图类
 * @author zzl
 * Date:2014-08-05
 */
public interface Viewer {
    /**
     * 获取输入框
     * @param fieldBean
     * @return
     */
    public  String getInputString(TableFieldBean fieldBean);



    /**
     * 获取列表显示
     * @param fieldBean
     * @return
     */
    public  String getShowString(TableFieldBean fieldBean);

    /**
     * 获取查询框
     * @param fieldBean
     * @return
     */
    public String getQueryString(TableFieldBean fieldBean);

    /**
     * 获取详细显示
     * @param fieldBean
     * @return
     */
    public String getDetailString(TableFieldBean fieldBean);

    /**
     * 获取可编辑的列表显示
     * @param fieldBean
     * @return
     */
    public String getEditShowString(TableFieldBean fieldBean);
}
