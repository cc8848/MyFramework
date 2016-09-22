package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.code.service.impl.TableInfService;
import com.code.util.ViewUtil;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author zzl
 * Date:2014-08-09
 */
public class EasyuiViewUtil implements ViewUtil {




    private static Map<String,Viewer> viewerMap=new WeakHashMap<>(0);
    private static String DEFAULT_TYPE="text";
    static{
        viewerMap.put("number",new EasyuiNumBerBoxViewer());
        viewerMap.put("date",new EasyuiDateBoxViewer());
        viewerMap.put("combobox",new EasyuiComboboxViewer());
        viewerMap.put("combobox(不可编辑)",new EasyuiComboboxViewer(false));
        viewerMap.put("checkbox",new EasyuiCheckboxViewer());
        viewerMap.put("file",new EasyuiFileViewer());
        viewerMap.put("file_view",new EasyuiFileViewViewer());
        viewerMap.put("text",new EasyuiTextViewer());
        viewerMap.put("数据采集",new EasyuiCjViewer());
        viewerMap.put("下拉树",new EasyuiComboTreeViewer());
        viewerMap.put("库房位置",new EasyuiKfViewer());
        viewerMap.put("combobox（选中第一个）",new EasyuiComboboxCheckedViewer());
    }
    /**
     * 获取更新输入框
     * @param fieldBean
     * @return
     */
    public  String getFieldInput(TableFieldBean fieldBean){
        return getViewer(fieldBean.getShowType()).getInputString(fieldBean);
    }

    public  String getTableContentFieldInput(TableFieldBean fieldBean,String tableName){
        EasyuiComboboxCheckedViewer checkedViewer=  new EasyuiComboboxCheckedViewer();
       return  checkedViewer.getAllInputString(fieldBean,tableName);
    }


    /**
     * 获取新的更新输入框
     * @param fieldBean
     * @return
     */
    public  String getNewFieldInput(TableFieldBean fieldBean,String tableName) {
        return getViewer("combobox（选中第一个）").getInputString(fieldBean);
    }






    /**
     * 获取查询输入框
     * @param fieldBean
     * @return
     */
    public  String getFieldQuery(TableFieldBean fieldBean){
        return getViewer(fieldBean.getShowType()).getQueryString(fieldBean);

    }

    /**
     * 获取列表页面显示head
     * @param fieldBean
     * @return
     */
    public  String getFieldShow(TableFieldBean fieldBean){
       return getViewer(fieldBean.getShowType()).getShowString(fieldBean);
    }

    @Override
    public String getDetailShow(TableFieldBean fieldBean) {
        return getViewer(fieldBean.getShowType()).getDetailString(fieldBean);
    }

    @Override
    public String getEditFieldShow(TableFieldBean fieldBean) {
        return getViewer(fieldBean.getShowType()).getEditShowString(fieldBean);
    }
    private Viewer getViewer(String type){
        Viewer viewer=viewerMap.get(type);
        if(viewer==null)
            viewer=viewerMap.get(DEFAULT_TYPE);
        return viewer;
    }
    @Override
    public void addView(String type, Viewer viewer) {
        viewerMap.put(type,viewer);
    }

    @Override
    public List<String> getViewerKeys() {
        return new ArrayList<String>(viewerMap.keySet());
    }


}
