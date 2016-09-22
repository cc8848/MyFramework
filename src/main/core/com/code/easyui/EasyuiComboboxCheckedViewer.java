package com.code.easyui;

import com.code.core.Viewer;
import com.code.entity.FieldDefaultBean;
import com.code.entity.TableFieldBean;
import com.code.service.ITableInfService;
import com.code.service.impl.TableInfService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sys.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import com.code.service.impl.TableInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2015/3/26.
 */
@Component
public class EasyuiComboboxCheckedViewer implements Viewer{

    @Resource
    private ITableInfService tableInfService;

    private boolean editable=true;

    public EasyuiComboboxCheckedViewer() {
    }

        public EasyuiComboboxCheckedViewer(boolean editable) {

        this.editable = editable;
    }

        private static String dicStr="${pageContext.request.contextPath}/sys/dic.do?";
        @Override
        public String getInputString(TableFieldBean fieldBean) {
        return getInput(
                fieldBean.getFieldName(),
                !fieldBean.isNull(),
                fieldBean.getTextLength(),
                fieldBean.getDicKey(),
                fieldBean.getDicValue(),
                fieldBean.getDicTable(),
                ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),
                true,false);
        }

    public  String getAllInputString(TableFieldBean fieldBean,String tableName) {
        return getAllInput(
                fieldBean.getFieldName(),
                !fieldBean.isNull(),
                fieldBean.getTextLength(),
                fieldBean.getDicKey(),
                fieldBean.getDicValue(),
                fieldBean.getDicTable(),
                ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),
                true,false,tableName);
    }

        @Override
        public String getShowString(TableFieldBean fieldBean) {
        String options="";
        String editor="editor:{type:'combobox'";
        if(!fieldBean.isNull())
            editor+=",required:true";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>0){
            options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
            for(int i=1;i<list.size();i++){
                options+=" else if(value=='"+list.get(i).getValue()+"') return '"+list.get(i).getText()+"';";
            }
            options+=" else return ''; }";
            editor+=",options:{valueField:'value',textField:'text',data:"+ComboboxOptionUtil.getDataByList(list)+"}";

        }else{
            options="formatter: function(value,row,index){ return getDicValue('"+fieldBean.getDicKey().replace("'","\\'")+"','"+fieldBean.getDicValue()+"','"+fieldBean.getDicTable()+"',value);}";
            editor+=",options:{valueField:'cKey',textField:'cValue',method: 'get',";
            editor+="url:'"+dicStr+"dicList&dicKey="+fieldBean.getDicKey().replace("'","\\'");
            if(StringUtils.isNotBlank(fieldBean.getDicValue()))
                editor+="&dicValue="+fieldBean.getDicValue();
            if(StringUtils.isNotBlank(fieldBean.getDicTable()))
                editor+="&dicTable="+fieldBean.getDicTable();
            editor+="'}";
        }
        editor+="}";
        // options+=","+editor;
        return "<th field='"+ StringUtil.toFieldName(fieldBean.getFieldName())+"' sortable=\"true\" data-options=\"width:100,"+options+"\" >"+fieldBean.getFieldContent()+"</th>";
    }

        @Override
        public String getQueryString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),false,fieldBean.getTextLength(),fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),true,true);
    }

        @Override
        public String getDetailString(TableFieldBean fieldBean) {
        return getInput(fieldBean.getFieldName(),false,fieldBean.getTextLength(),fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),ComboboxOptionUtil.getData(fieldBean.getFieldDefault()),false,false);
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
    private String getInput(String name,boolean isRequired,int size,String dicKey,String dicValue,String dicTable,String defaultVal,boolean enable,boolean isSearch){
        String disabled="disabled='disabled'";
        dicValue =tableInfService.getTableContent(name);
        if(enable)
            disabled="";
        StringBuffer buffer=new StringBuffer("<input class='easyui-combobox' "+disabled+" name='");
        buffer.append(StringUtil.toFieldName(name))
                .append("' ");
        if(size>0)
            buffer.append("style='width:")
                    .append(size)
                    .append("' ");
        String options="";
        if(!editable)
            options+="editable:false,";
        if(StringUtils.isNotBlank(defaultVal)){
            options+="valueField:'value',textField:'text',data:"+defaultVal+",";
        }else{
            options+="url:'"+dicStr+"dicList&dicKey="+dicKey.replace("'","\\'");
            if(StringUtils.isNotBlank(dicValue))
                options+="&dicValue="+dicValue;
            if(StringUtils.isNotBlank(dicTable))
                options+="&dicTable="+dicTable;
            options+="' , method: 'get', valueField:'cKey',textField:'cValue',";
        }
        if(isRequired&&!isSearch)
            options+="required:true,";
        if(options.endsWith(","))
            options=options.substring(0,options.length()-1);
        if(StringUtils.isNotBlank(options))
            buffer.append(" data-options=\"")
                    .append(options)
                    .append("\"");
        buffer.append(" value=\""+dicValue+"\" />");

 return buffer.toString();

 }

    private String getAllInput(String name,boolean isRequired,int size,String dicKey,String dicValue,String dicTable,String defaultVal,boolean enable,boolean isSearch,String tableName){
        String disabled="disabled='disabled'";
        dicValue =getDicStr(tableName);
        if(enable)
            disabled="";
        String style="";
        if(isSearch)
            style=" style=\"width:190px\" ";
        StringBuffer buffer=new StringBuffer("<input class='easyui-combobox' "+style+disabled+" name='");
        buffer.append(StringUtil.toFieldName(name))
                .append("' ");
        if(size>0)
            buffer.append("style='width:")
                    .append(size)
                    .append("' ");
        String options="";
        if(!editable)
            options+="editable:false,";
        if(StringUtils.isNotBlank(defaultVal)){
            options+="valueField:'value',textField:'text',data:"+defaultVal+",";
        }else{
            options+="url:'"+dicStr+"dicList&dicKey="+dicKey.replace("'","\\'");
            if(StringUtils.isNotBlank(dicValue))
                options+="&dicValue="+dicValue;
            if(StringUtils.isNotBlank(dicTable))
                options+="&dicTable="+dicTable;
            options+="' , method: 'get', valueField:'cKey',textField:'cValue',";
        }
        if(isRequired&&!isSearch)
            options+="required:true,";
        if(options.endsWith(","))
            options=options.substring(0,options.length()-1);
        if(StringUtils.isNotBlank(options))
            buffer.append(" data-options=\"")
                    .append(options)
                    .append("\"");
        buffer.append(" value=\""+dicValue+"\" />");

        return buffer.toString();

    }




public  String getDicStr(String tableName){
    return tableInfService.getTableContent(tableName);
}



    @Override
    public String getEditShowString(TableFieldBean fieldBean) {
        String options="";
        String editor="editor:{type:'combobox'";
        String edit="";
        if(!editable)
            edit="editable:false,";
        if(!fieldBean.isNull())
            editor+=",required:true";
        List<ComboboxOptionBean> list=ComboboxOptionUtil.fromString(fieldBean.getFieldDefault());
        if(list!=null&&list.size()>0){
            options+="formatter:function(value,row,index){ if(value=='"+list.get(0).getValue()+"') return '"+list.get(0).getText()+"';";
            for(int i=1;i<list.size();i++){
                options+=" else if(value=='"+list.get(i).getValue()+"') return '"+list.get(i).getText()+"';";
            }
            options+=" else return ''; }";
            editor+=",options:{"+edit+"valueField:'cKey',textField:'cValue',data:"+ComboboxOptionUtil.getDataByList(list)+"}";

        }else{
            options="formatter: function(value,row,index){ return getDicValue('"+fieldBean.getDicKey().replace("'","\\'")+"','"+fieldBean.getDicValue()+"','"+fieldBean.getDicTable()+"',value);}";
            editor+=",options:{"+edit+"valueField:'cKey',textField:'cValue',method: 'get',";
            editor+="url:'"+dicStr+"dicList&dicKey="+fieldBean.getDicKey().replace("'","\\'");
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

    public static void main(String[] args){
        TableFieldBean t1=new TableFieldBean();
        t1.setFieldName("dic_key");
        t1.setFieldContent("字典key");
        t1.setFieldLength(50f);
        t1.setIsNull(1);
        t1.setFieldType("combobox");
        t1.setIsKey(0);

        t1.setFieldValidType("length[3,5]");
        t1.setIsInsert(1);
        t1.setIsQuery(1);
        t1.setIsShow(1);
        t1.setIsShowList(1);
        t1.setDicKey("dicKey");
        t1.setDicValue("dicValue");
        t1.setDicTable("sys_dic");
        t1.setFieldDefault("1_是,4_否");
        //  t1.setShowLength(50.2f);
        t1.setShowType("text");
        EasyuiComboboxCheckedViewer viewer=new EasyuiComboboxCheckedViewer();
        System.out.println(viewer.getShowString(t1));
        System.out.println(viewer.getDetailString(t1));
        System.out.println(viewer.getInputString(t1));
        System.out.println(viewer.getQueryString(t1));
    }
}
