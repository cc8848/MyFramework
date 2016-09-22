package com.code.util;

import com.code.core.Viewer;
import com.code.easyui.*;
import com.code.entity.TableFieldBean;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author zzl
 *         Date:2014-08-04
 */
public interface ViewUtil {
    /**
     * 获取更新输入框
     * @param fieldBean
     * @return
     */
    public  String getFieldInput(TableFieldBean fieldBean);


    /**
     * 获取新的更新输入框
     * @param fieldBean
     * @return
     */
    public  String getTableContentFieldInput(TableFieldBean fieldBean,String tableName);

    /**
     * 获取查询输入框
     * @param fieldBean
     * @return
     */
    public  String getFieldQuery(TableFieldBean fieldBean);

    /**
     * 获取列表页面显示
     * @param fieldBean
     * @return
     */
    public  String getFieldShow(TableFieldBean fieldBean);

    /**
     * 获取详情显示
     */
    public String getDetailShow(TableFieldBean fieldBean);

    /**
     * 获取可编辑列表显示
     * @param fieldBean
     * @return
     */
    public String getEditFieldShow(TableFieldBean fieldBean);

    /**
     * 添加视图
     * @param type
     * @param viewer
     */
    public void addView(String type, Viewer viewer);

    /**
     * 获取视图工具所能处理的类型
     * @return
     */
    public List<String> getViewerKeys();



}
