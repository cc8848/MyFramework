package com.code.util;

import com.code.entity.DhConfigBean;
import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.code.service.ITableInfService;
import com.sys.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2014/12/12.
 */
@Component
public class DhUtil {
    @Resource
    private ITableInfService tableInfService;
    private static DhUtil dhUtil;
    @PostConstruct
    private void init(){
        dhUtil=this;
        dhUtil.tableInfService=this.tableInfService;
    }
    public static DhConfigBean getDhConfigBean(String tableName){
        return dhUtil.tableInfService.getDhConfig(tableName);
    }
    public static boolean fillDh(Object o,DhConfigBean dhConfigBean,boolean includeSubTable){

        String val=getDhVal(o,dhConfigBean);
        try {
            if(StringUtils.isBlank(val)) return false;
            PropertyUtils.setProperty(o,StringUtil.toFieldName(dhConfigBean.getDhField()),val);


            Field[] Fields = o.getClass().getDeclaredFields();
            Boolean isAjh = false;
            Boolean isJh = false;
            for(Field field:Fields){
                if("ajh".equals(field.getName())){
                    isAjh = true;
                }
                if("jh".equals(field.getName())){
                    isJh = true;
                }
            }

            if(isAjh) {
                if (dhConfigBean.getParentDh() != null && dhConfigBean.getParentDh().trim().length() > 0) {
                    PropertyUtils.setProperty(o, StringUtil.toFieldName("ajh"), getAjh(dhConfigBean.getParentDh()));
                } else {
                    PropertyUtils.setProperty(o, StringUtil.toFieldName("ajh"), getAjh(val));
                }
            }
            //如果子类有件号则进行处理
            if(isJh){
                if (dhConfigBean.getParentDh() != null && dhConfigBean.getParentDh().trim().length() > 0) {

                    PropertyUtils.setProperty(o, StringUtil.toFieldName("jh"), getJh(val));
                } else {

                    PropertyUtils.setProperty(o, StringUtil.toFieldName("jh"), getJh(val));
                }
            }

            dhUtil.tableInfService.saveorupdate(o);
            if(includeSubTable){
                List<TableHeadBean> headBeans=dhUtil.tableInfService.getSubTableHead(dhConfigBean.getTableName(),false);
                if(CollectionUtils.isNotEmpty(headBeans)){
                    Serializable id= (Serializable) PropertyUtils.getProperty(o,"id");
                    for(TableHeadBean headBean:headBeans){
                        updateSubTableDh(headBean.getTableName(),dhConfigBean.getTableName(),id,val);

                        // updateSubTableJh(headBean.getTableName(),dhConfigBean.getTableName(),id,val);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private static String getAjh(String dh){
        try {
            return dh.split("-")[ dh.split("-").length-1];
        }catch (Exception e){
            return  null;
        }

    }

    /***
     * 获取件号
     * */
    private static String getJh(String dh){
        try {
            String[] str = dh.split("-");
            return dh.split("-")[ dh.split("-").length-1];
        }catch (Exception e){
            return  null;
        }

    }
    public static String getDhVal(Object obj,DhConfigBean dhConfigBean){
        if(StringUtils.isBlank(dhConfigBean.getDhFieldRule()))
            return null;
        Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
        Matcher matcher = pattern.matcher(dhConfigBean.getDhFieldRule());
        String val=dhConfigBean.getParentDh()+ dhConfigBean.getDhFieldRule();
        while (matcher.find()) {
            if(StringUtils.isNotBlank(matcher.group(1))){
                if(matcher.group(1).startsWith("%")){
                    try{
                        int num= dhUtil.tableInfService.getdhMaxNum(dhConfigBean.getTableName(),dhConfigBean.getDhField(),val.substring(0,val.indexOf(matcher.group(0))));
                       // System.out.println("num="+num+"matcher.group(1)="+matcher.group(1)+"matcher.group(0)="+matcher.group(0));
                        val=val.replace(matcher.group(0),String.format(matcher.group(1),num+1));
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }else{
                    try{
                        val=val.replace(matcher.group(0), PropertyUtils.getProperty(obj, StringUtil.toFieldName(matcher.group(1)))+"");
                    }catch (Exception e){
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return val;
    }
    //更新字表
    public static void updateSubTableDh(String tableName,String mainTableName,Serializable mainId,String dhPrefix){
        DhConfigBean dhConfigBean=getDhConfigBean(tableName);
        dhConfigBean.setParentDh(dhPrefix);
        String hql=" from "+StringUtil.toEntityName(tableName)+"Entity where "+StringUtil.toFieldName(mainTableName)+"Entity.id=? order by _createDate ";
        List<Object> dataList=dhUtil.tableInfService.getDataListByHql(hql,new Object[]{mainId});
        for(Object o:dataList){
            fillDh(o,dhConfigBean,false);
        }
    }

    /**
     * 获取档号的组成字段
     * @param tableName
     * @return
     */
    public static TableHeadBean getDhFields(String tableName){
        TableHeadBean headBean=dhUtil.tableInfService.getTableHead(tableName,true);
        if(headBean!=null){
            List<TableFieldBean> fieldBeans=headBean.getFields();
            List<TableFieldBean> list=new LinkedList<>();
            if(CollectionUtils.isNotEmpty(fieldBeans)){
                DhConfigBean dhConfigBean=dhUtil.tableInfService.getDhConfig(tableName);
                Pattern pattern = Pattern.compile("\\{([^{}]+)\\}");
                Matcher matcher = pattern.matcher(dhConfigBean.getDhFieldRule());
                Map<String,TableFieldBean> map=new HashMap<>();
                for(TableFieldBean fieldBean:fieldBeans){
                    map.put(fieldBean.getFieldName(),fieldBean);
                }
                while (matcher.find()) {
                    if(StringUtils.isNotBlank(matcher.group(1))){
                        if(!matcher.group(1).startsWith("%")){
                            TableFieldBean fieldBean=map.get(matcher.group(1));
                            if(fieldBean!=null)
                                list.add(fieldBean);
                        }
                    }
                }
            }
            headBean.setFields(list);
        }
        return headBean;
    }

}
