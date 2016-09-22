package com.code.service.impl;

import com.code.entity.BaseEntity;
import com.code.entity.DhConfigBean;
import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.code.service.ITableInfService;
import com.code.util.ViewUtil;
import com.sys.annotation.Ehcache;
import com.sys.service.impl.CommonService;
import com.sys.util.BeanUtil;
import com.sys.util.DataGrid;
import com.sys.util.DicUtil;
import com.sys.util.StringUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by lenovo on 14-8-1.
 */
@Service("tableInfService")
@Transactional
public class TableInfService extends CommonService implements ITableInfService {
    @Ehcache(addOrdel = false)
    @Override
    public boolean saveTableInf(TableHeadBean bean) {
        TableHeadBean tableHeadBean=null;
        //数据库中不存在
        String id= (String) mergeData(bean,bean.getId());
        tableHeadBean=getEntity(TableHeadBean.class,id);
        id=null;
        //保存子类
        List<TableFieldBean> tableFieldBeanList=bean.getFields();
        if(tableFieldBeanList!=null){
            for (TableFieldBean field:tableFieldBeanList){
                if(StringUtils.isBlank(field.getId())) field.setId(null);
                field.setHead(tableHeadBean);
                mergeData(field,field.getId());
            }
        }
        tableFieldBeanList=null;
        return true;
    }

    @Override
    public Map<String, Object> getFieldInf(String tableName, boolean includeSubTable,boolean includeFileType) {
        List<TableFieldBean> mainFieldList=getFieldListByTableName(tableName,includeSubTable,includeFileType);
        Map<String,Object> dataMap=null;
        if(mainFieldList!=null&&mainFieldList.size()>0){
           dataMap=field2Map(mainFieldList);
        }
        mainFieldList=null;
        if(includeSubTable){
            dataMap.putAll(getSubFieldInf(tableName,includeSubTable));
        }
        return dataMap;
    }
    private Map<String,Object> getSubFieldInf(String mainTableName,boolean includeSubTable){
        Map<String,Object> dataMap=new LinkedHashMap<>();
        List<TableHeadBean> subHeadList=getDataListByHql("select bean.head from TableFieldBean bean where bean.mainTable=?",new Object[]{mainTableName});
        if(subHeadList!=null||subHeadList.size()>0){
            for(TableHeadBean headBean:subHeadList){
                List<TableFieldBean> subFieldList=getDataListByHql("from TableFieldBean where head.id=? order by fieldOrder",new Object[]{headBean.getId()});
                if(subFieldList!=null&&subFieldList.size()>0){
                    dataMap.put(StringUtil.toFieldName(headBean.getTableName()+"s"),field2Map(subFieldList));
                }
                dataMap.putAll(getSubFieldInf(headBean.getTableName(),includeSubTable));
            }

        }
        return dataMap;
    }
    /**
     * 字段信息转成map
     * @param fieldBeans
     * @return
     */
    private Map<String,Object> field2Map(List<TableFieldBean> fieldBeans){
        Map<String,Object> dataMap=new LinkedHashMap<>();
        for(TableFieldBean fieldBean:fieldBeans){
            if (fieldBean.getFieldName() !=null){
                dataMap.put(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean);
            }

        }
        return dataMap;
    }

    @Override
    public Map<String, Object> getTitleMap(String tableName, boolean includeSubTable,boolean includeFileType) {
        List<TableFieldBean> fieldBeans=getFieldListByTableName(tableName,includeSubTable,includeFileType);
        Map<String,Object> dataMap=new LinkedHashMap<>();
        if(fieldBeans!=null&&fieldBeans.size()>0){
            for(TableFieldBean fieldBean:fieldBeans){
               // if(Objects.equals(1, fieldBean.getIsShowList())){
                    if(!includeFileType){
                        if(fieldBean.getShowType()!=null&&fieldBean.getShowType().startsWith("file"))
                            continue;
                    }
                    dataMap.put(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean.getFieldContent());
              //  }
            }
        }
        if(includeSubTable){
            dataMap.putAll(getSubTitleMap(tableName,includeSubTable));
        }
        return dataMap;
    }

    private Map<String,Object> field2TitleMap(List<TableFieldBean> fieldBeans){
        Map<String,Object> dataMap=new LinkedHashMap<>();
        if(fieldBeans!=null&&fieldBeans.size()>0){
            for(TableFieldBean fieldBean:fieldBeans){
                if(Objects.equals(1, fieldBean.getIsShowList())&&(fieldBean.getShowType()==null||!fieldBean.getShowType().startsWith("file"))){
                    dataMap.put(StringUtil.toFieldName(fieldBean.getFieldName()),fieldBean.getFieldContent());
                }
            }
        }
        return dataMap;
    }

    private Map<String,Object> getSubTitleMap(String mainTableName, boolean includeSubTable){
        Map<String,Object> dataMap=new LinkedHashMap<>();
        List<TableHeadBean> subHeadList=getDataListByHql("select bean.head from TableFieldBean bean where bean.mainTable=?",new Object[]{mainTableName});
        if(subHeadList!=null||subHeadList.size()>0){
            for(TableHeadBean headBean:subHeadList){
                List<TableFieldBean> subFieldList=getDataListByHql("from TableFieldBean where head.id=? order by fieldOrder",new Object[]{headBean.getId()});
                if(subFieldList!=null&&subFieldList.size()>0){
                    dataMap.put(StringUtil.toFieldName(headBean.getTableName()+"s"),field2TitleMap(subFieldList));
                }
                dataMap.putAll(getSubTitleMap(headBean.getTableName(),includeSubTable));
            }
        }
        return dataMap;
    }
    private List<TableFieldBean> getFieldListByTableName(String tableName, boolean includeSubTable,boolean includeFileType){
        StringBuffer buffer=new StringBuffer("from TableFieldBean where head.tableName=? ");
        //    DetachedCriteria criteria=DetachedCriteria.forClass(TableFieldBean.class);
        // criteria.add(Restrictions.eq("head.tableName",tableName));
        if(!includeFileType){
            buffer.append(" and showType !='file' and showType !='file_view' ");
          /*  criteria.add(Restrictions.ne("showType","file"));
            criteria.add(Restrictions.ne("showType","file_view"));*/
        }
        buffer.append(" order by fieldOrder");
        // criteria.addOrder(Order.asc("fieldOrder"));
        List<TableFieldBean> mainFieldList=getDataListByHql(buffer.toString(),new Object[]{tableName});
        return mainFieldList;
    }

    @Override
    public TableHeadBean getMainTableHead(String tableName,boolean includeFields) {
        StringBuffer hqlBuffer=new StringBuffer("select headBean from TableFieldBean fieldBean ,TableHeadBean headBean ");
        hqlBuffer.append(" where fieldBean.head.tableName=? and fieldBean.mainTable=headBean.tableName ");
        TableHeadBean headBean=null;
        List<TableHeadBean> headList=getDataListByHql(hqlBuffer.toString(),new Object[]{tableName});
        if(CollectionUtils.isEmpty(headList))
            return null;
        headBean=headList.get(0);
        if(headBean!=null&&includeFields){
            List<TableFieldBean> fieldBeans=getDataListByHql("from TableFieldBean where head.id=?", new Object[]{headBean.getId()});
            headBean.setFields(fieldBeans);
        }
        return headBean;
    }
@SuppressWarnings("unchecked")
    @Override
    public String  getTableContent(String tableName) {
        String dicflh="";
        String hql=" from TableHeadBean  where table_name =? ";
        List<TableHeadBean> headList=getDataListByHql(hql,new Object[]{tableName});
        if(headList.size()>0)
        {
            dicflh= headList.get(0).getDicflh();
        }

        return  dicflh;
    }

    /**
     *
     * 获取表描述
     * @param tableName
     * @return
     */
    @Override
    public String  getTableContentTo(String tableName) {
        String content="";
        String hql=" from TableHeadBean  where table_name =? ";
        List<TableHeadBean> headList=getDataListByHql(hql,new Object[]{tableName});
        if(headList.size()>0)
        {
            content= headList.get(0).getTableContent();
        }

        return  content;
    }


//    @Ehcache
    @Override
    public List<TableHeadBean> getSubTableHead(String tableName,boolean includeFields) {
        String hql="select bean.head from TableFieldBean bean where bean.mainTable=?";
        List<TableHeadBean> headList=getDataListByHql(hql,new Object[]{tableName});
        if(CollectionUtils.isEmpty(headList))
            return null;
        if(includeFields){
            for(TableHeadBean headBean:headList){
                List<TableFieldBean> fieldBeans=getDataListByHql("from TableFieldBean where head.id=?",new Object[]{headBean.getId()});
                headBean.setFields(fieldBeans);
            }
        }
        return headList;
    }
//    @Ehcache
    @Override
    public TableHeadBean getTableHead(String tableName, boolean includeFields) {
        TableHeadBean headBean=null;
        List<TableHeadBean> headBeans=getDataListByHql("from TableHeadBean where tableName=?",new Object[]{tableName});
        if(CollectionUtils.isNotEmpty(headBeans)){
            headBean=headBeans.get(0);
        }
        if(headBean!=null&&includeFields){
            List<TableFieldBean> fieldBeans=getDataListByHql("from TableFieldBean where head.id=? order by fieldOrder",new Object[]{headBean.getId()});
            headBean.setFields(fieldBeans);
        }
        return headBean;
    }

    @Override
    public List<Object> getDataListByIDs(String  tableName, Serializable[] ids, boolean includeSubTable) {
        Class clazz= DicUtil.getEntityClass(tableName);
        DetachedCriteria criteria=DetachedCriteria.forClass(clazz);
         criteria.add(Restrictions.in("id",ids));
         List<Object> dataList=findDataList(criteria);
         if(CollectionUtils.isNotEmpty(dataList)&&includeSubTable){
             fillSubTableData(dataList,ids,tableName);

         }
        return dataList;
    }
    private List<Object> fillSubTableData(List<Object> dataList,Serializable[] ids,String tableName){
        List<TableHeadBean> headBeans=getSubTableHead(tableName,false);
        if(CollectionUtils.isNotEmpty(headBeans)){
            String entityName=StringUtil.toEntityName(tableName)+"Entity";
            String fieldName=StringUtil.toFieldName(tableName)+"Entity";
            String hql="";
            Map<String,Object> idMap=new HashMap<>();
            idMap.put("ids",ids);
            for(TableHeadBean headBean:headBeans){
                String subEntityName=StringUtil.toEntityName(headBean.getTableName())+"Entity";
                hql="select entity."+fieldName+".id as pId ,entity from "+subEntityName+" entity where ";
                hql+="entity."+fieldName+".id in (:ids) ";
                List<Object[]> subDataList=getDataByHql(hql,idMap);
                if(CollectionUtils.isNotEmpty(subDataList)){
                    Map<Serializable,List<Object>> map=makeParentMap(subDataList);
                    for(Object o:dataList){
                        try {
                            Serializable id=BeanUtils.getProperty(o,"id");
                            BeanUtils.setProperty(o,StringUtil.toFieldName(headBean.getTableName())+"s",map.get(id));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        return dataList;
    }
    @Override
    public String getClassName(String tableName) {
        return getTableClassMap().get(tableName);
    }





    private Map<Serializable,List<Object>> makeParentMap(List<Object[]> list){
        Map<Serializable,List<Object>> map=new HashMap<>();
        for(Object[] o:list){
            List<Object> objects=map.get(o[0]);
            if(objects==null) objects=new ArrayList<>();
            objects.add(o[1]);
            map.put((Serializable) o[0],objects);
        }
        return map;
    }

    @Override
    public DataGrid fillDataGridWithSubTable(List<Criterion> criterions, String tableName,Class entityClass, DataGrid d, boolean includeSubTable) {
        //查询数据
        fillDataGrid(entityClass,criterions,d);
        //获取数据列表
         List<Object> dataList=d.getDataList();
         Set<Serializable> idSet=new HashSet<>();
        //遍历数据列表获取id
         for(Object o:dataList){
             if(BaseEntity.class.isAssignableFrom(o.getClass())){
                 BaseEntity entity= (BaseEntity) o;
                 idSet.add(entity.getId());
             }
         }
         if(CollectionUtils.isNotEmpty(dataList)&&includeSubTable){
             fillSubTableData(dataList,idSet.toArray(new Serializable[0]),tableName);
         }
        return d;
    }
    @Override
    public DhConfigBean getDhConfig(String tableName) {
        DhConfigBean configBean=new DhConfigBean();
        String hql=" select field.fieldName,field.head.dhFieldRule,field  from TableFieldBean field where field.fieldName=field.head.dhField and field.head.tableName=?";
        List<Object[]> list=getDataListByHql(hql,new Object[]{tableName});
        if(CollectionUtils.isNotEmpty(list)){
            Object[] objects= list.get(0);
            configBean.setDhField((String) objects[0]);
            configBean.setDhFieldRule((String) objects[1]);
            configBean.setTableFieldBean((TableFieldBean) objects[2]);
            configBean.setTableName(tableName);
        }
        return configBean;
    }
    @Override
    public int getdhMaxNum(String tableName, String dhColName, String dhPrefix) {
        int number=0;
        DataGrid d=new DataGrid();
        d.setPageNum(1);
        d.setPagesize(1);
        Map<String,Object> map=new HashMap<>();
        map.put("dhPrefix",dhPrefix+"%");
        List<Map<String,Object>> list= findDataBySql("select "+dhColName+" from "+tableName+" where "+dhColName+" like :dhPrefix order by "+dhColName+" desc",map,0,1);
        if(CollectionUtils.isNotEmpty(list)){
            Map<String,Object> dh=list.get(0);

                String str=dh.get(dhColName).toString().substring(dhPrefix.length()+1);
                if(StringUtils.isNotBlank(str)){
                    Integer integer=new Integer(str);
                    if(integer!=null){
                        number=integer;
                    }
                }

        }
        return number;
    }




    @Override
    public int readDel(String tableName, Serializable[] ids) {
        Map<String,Object> map=new HashMap<>();
        map.put("ids",ids);
        String mainClassName=getClassName(tableName);
        if(StringUtils.isBlank(mainClassName)) return 0;
        String mainHql=" delete from "+mainClassName+" where id in (:ids)";
        List<TableHeadBean> subHeadList=getSubTableHead(tableName,false);
        if(CollectionUtils.isNotEmpty(subHeadList)){
            for(TableHeadBean headBean:subHeadList){
                String hql=" delete from "+getClassName(headBean.getTableName())+" where "+StringUtil.toFieldName(tableName)+"Entity.id in (:ids) ";
                updateByHql(hql,map);
            }
        }
        return updateByHql(mainHql,map);
    }





    @Ehcache(addOrdel = false)
    @Override
    public Serializable mergeOneToMany(Object mainEntity, String mapBy, List<?> subEntityList) {
        return super.mergeOneToMany(mainEntity, mapBy, subEntityList);
    }
}
