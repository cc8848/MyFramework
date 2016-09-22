package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.FieldDefaultBean;
import com.code.entity.TableFieldBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sys.util.JsonUtil;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zzl
 *         Date:2014-08-05
 */
public class EasyuiCheckboxViewer implements Viewer {
    @Override
    public String getInputString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),getDefaultValue(fieldBean),true);
    }

    @Override
    public String getShowString(TableFieldBean fieldBean) {
        String options=",";
        String editor="editor:{type:'checkbox'";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>1){
             options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
             options+=" else return '"+list.get(1).getText()+"';}";
             editor+=",options:{on:'"+list.get(0).getValue()+"',off:'"+list.get(1).getValue()+"'";
             editor+="}";
        }
        editor+="}";
        if(",".equals(options))
            options="";
       // if(fieldBean.isInsert())
           // options+=","+editor;

        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' sortable=\"true\" data-options=\"width:100"+options+"\"  >"+fieldBean.getFieldContent()+"</th>";
    }

    @Override
    public String getQueryString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),getDefaultValue(fieldBean),true);
    }
    private String getInput(String name,String val,boolean enable){
        String disabled="disabled='disabled'";
        if(enable)
            disabled="";
        return "<input type='checkbox' "+disabled+"  name='"+ StringUtil.toFieldName(name)+"' value='"+val+"' />";
    }

    @Override
    public String getDetailString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),getDefaultValue(fieldBean),false);
    }

    /**
     * 获取可用值
     * @param fieldBean
     * @return
     */
    private List<FieldDefaultBean> getDefaultOpitons(TableFieldBean fieldBean){
        if(StringUtils.isNotBlank(fieldBean.getFieldDefault()))
            return  new Gson().fromJson(fieldBean.getFieldDefault(),new TypeToken<ArrayList<FieldDefaultBean>>(){}.getType());
        else
            return null;

    }

    /**
     * 默认值
     * @param fieldBean
     * @return
     */
    private String getDefaultValue(TableFieldBean fieldBean){
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>0){
            return list.get(0).getValue();
        }else
            return "1";
    }

    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        String options=",";
        String editor="editor:{type:'checkbox'";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>1){
            options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
            options+=" else return '"+list.get(1).getText()+"';}";
            editor+=",options:{on:'"+list.get(0).getValue()+"',off:'"+list.get(1).getValue()+"'";
            editor+="}";
        }
        editor+="}";
        if(",".equals(options))
            options="";
        if(fieldBean.isInsert())
            options+=","+editor;

        return "<th field='"+StringUtil.toFieldName(fieldBean.getFieldName())+"' data-options=\"width:100"+options+"\"  >"+fieldBean.getFieldContent()+"</th>";

    }

    public static void main(String[] args){
        TableFieldBean t1=new TableFieldBean();
        t1.setFieldName("dic_key");
        t1.setFieldContent("字典key");
        t1.setFieldLength(50f);
        t1.setIsNull(0);
        t1.setFieldType("date");
        t1.setIsKey(0);
        t1.setShowLength(5.3f);
        t1.setFieldValidType("length[3,5]");
        t1.setIsInsert(1);
        t1.setIsQuery(1);
        t1.setIsShow(1);
        t1.setIsShowList(1);
        t1.setFieldDefault("1_是,0_否");
        //  t1.setShowLength(50.2f);
        t1.setShowType("text");
        Viewer viewer=new EasyuiCheckboxViewer();
        System.out.println(viewer.getShowString(t1));
        System.out.println(viewer.getDetailString(t1));
        System.out.println(viewer.getInputString(t1));
        System.out.println(viewer.getQueryString(t1));
    }
}
