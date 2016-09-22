package com.sys.util;

import com.code.entity.TableFieldBean;
import com.code.service.ITableInfService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;

/**
 * 对map进行字典翻译或反翻译
 * @author zzl
 * @Date:2014-10-18
 */
@Component
public class DicConvertUtil {
    @Resource
    private ITableInfService tableInfService;
    private static DicConvertUtil dicConvertUtil;
    @PostConstruct
    public void init(){
        dicConvertUtil=this;
        dicConvertUtil.tableInfService=this.tableInfService;
    }
    /**
     * 对数据进行字典翻译
     * @param dataMap 原始数据，其中的Object 只能是基本类型，String，Map 和Collection
     * @param dictMap 字典对应项，object为 TableFieldBean 或Map value为TableFieldBean
     * @param addDict 是否是添加字典，true则添加字典，false则去除字典
     * @return
     */
   public static Map<String,Object> dictTrans(Map<String,Object> dataMap,Map<String,Object> dictMap,boolean addDict){
       if(dataMap==null||dictMap==null) return null;
       Object dictObject=null;
       Object dataValue=null;
       for(String str:dataMap.keySet()){
            dictObject=null;
            dictObject=dictMap.get(str);
            dataValue=null;
            dataValue= dataMap.get(str);
            if(dictObject==null||dataValue==null)
                continue;
            if((dataValue.getClass().isArray()|| Collection.class.isAssignableFrom(dataValue.getClass()))
               &&(dictObject instanceof Map)){
                Map tDictMap= (Map) dictObject;
                Collection c=null;
                if(dataValue.getClass().isArray()){
                    c= Arrays.asList(dataValue);
                }else{
                    c=(Collection)dataValue;
                }
                Iterator it=c.iterator();
                Map<String,Object> tempMap=new HashMap<>();
                while (it.hasNext()){
                    Map<String,Object> tMap2= (Map<String,Object> ) it.next();
                    tempMap.put(str,dictTrans(tMap2,tDictMap,addDict));
                }
            }else if(dataValue instanceof Map  &&(dictObject instanceof Map)){
                dataMap.put(str,dictTrans((Map)dataValue,(Map)dictObject,addDict));
            }else if(dictObject instanceof TableFieldBean) {
                TableFieldBean fieldBean= (TableFieldBean) dictObject;
                if(addDict)
                    dataMap.put(str,DicUtil.getDicValue(fieldBean,dataMap.get(str),false));
                else
                    dataMap.put(str,DicUtil.getDicCode(fieldBean,dataMap.get(str)+""));
            }
       }
       return dataMap;
   }

    /**
     * 对数据进行字典翻译
     * @param dataMap 原始数据，其中的Object 只能是基本类型，String，Map 和Collection
     * @param tableName 表名
     * @param includeSubTable 是否包含子表
     * @param addDict 是否是添加字典，true则添加字典，false则去除字典
     * @return
     */
    public static Map<String,Object> dictTransWithTableName(Map<String,Object> dataMap,String tableName,boolean includeSubTable ,boolean addDict){
        Map<String,Object> dictMap=dicConvertUtil.tableInfService.getFieldInf(tableName,includeSubTable,false);
        return dictTrans(dataMap,dictMap,addDict);
    }

    /**
     * 对数据列表进行字典翻译
     * @param sourceDataList
     * @param tableName
     * @param includeSubTable
     * @param addDict
     * @return
     */
    public static List<Map<String,Object>> dictTransListWithTableName(List<Object> sourceDataList,String tableName,boolean includeSubTable, boolean addDict){
        Map<String,Object> dictMap=dicConvertUtil.tableInfService.getFieldInf(tableName,includeSubTable,false);
        List<Map<String,Object>> targetList=new ArrayList<>();
        for(Object o:sourceDataList){
            Map<String,Object> dataMap=BeanUtil.transBean2Map(o);
            targetList.add(dictTrans(dataMap,dictMap,addDict));
        }
        dictMap=null;
        return targetList;
    }
}
