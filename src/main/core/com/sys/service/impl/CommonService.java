package com.sys.service.impl;

import com.code.entity.BaseEntity;
import com.sys.annotation.Ehcache;
import com.sys.dao.IBaseCommonDao;
import com.sys.entity.TreeBean;
import com.sys.service.ICommonService;
import com.sys.util.BeanUtil;
import com.sys.util.DataGrid;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 *         Date:2014-07-28
 */
@Service("commonService")
@Transactional
public  class CommonService implements ICommonService {
    private static Logger logger=Logger.getLogger(CommonService.class);
    @Autowired
    private IBaseCommonDao baseCommonDao;
    @Override
    public boolean save(Object entity) {
        return baseCommonDao.save(entity);
    }

    @Override
    public boolean update(Object entity) {
        return baseCommonDao.update(entity);
    }

    @Override
    public <T> boolean bulkUpdate(List<T> list) {
        return baseCommonDao.bulkUpdate(list);
    }

    @Override
    public int bulkSave(List list) {
        return baseCommonDao.bulkSave(list);
    }

    @Override
    public boolean delete(Object clazz) {
        return baseCommonDao.delete(clazz);
    }

    @Override
    public int updateByHql(String hql, Object[] params) {
        return baseCommonDao.updateByHql(hql,params);
    }

    @Override
    public <T> T getEntity(Class entity, Serializable id) {
        return baseCommonDao.getEntity(entity,id);
    }

    @Override
    public int getTotalCount(Class clazz,List<Criterion> list) {
        return baseCommonDao.getTotalCount(baseCommonDao.fillCriterion(clazz,list));
    }

    @Override
    public <T> List<T> getDataList(Class clazz,List<Criterion> list, List<Order> orders) {
        return baseCommonDao.getDataList(baseCommonDao.fillCriterion(clazz,list),orders);
    }

    @Override
    public <T> List<T> getPageDateList(Class clazz,List<Criterion> list, DataGrid datagrid) {
        return baseCommonDao.getPageDateList(baseCommonDao.fillCriterion(clazz,list),datagrid);
    }

    @Override
    public DataGrid fillDataGrid(Class clazz,List<Criterion> list, DataGrid datagrid) {
        return baseCommonDao.fillDataGrid(baseCommonDao.fillCriterion(clazz,list),datagrid);
    }

    @Override
    public int getCountByHql(String hql, Object[] params) {
        return baseCommonDao.getCountByHql(hql,params);
    }

    @Override
    public <T> List<T> getDataListByHql(String hql, Object[] params) {
        return baseCommonDao.getDataListByHql(hql,params);
    }

    @Override
    public <T> List<T> getPageDataListByHql(String hql, Object[] params, DataGrid datagrid) {
        return baseCommonDao.getPageDataListByHql(hql,params,datagrid);
    }

    @Override
    public DataGrid fillDataGridByHql(String hql, Object[] params, DataGrid datagrid) {
        return baseCommonDao.fillDataGridByHql(hql,params,datagrid);
    }

    @Override
    public <T> List<T> findBySql(String sql, Object[] params) {
        return baseCommonDao.findBySql(sql,params);
    }

    @Override
    public int updateBySql(String sql, Object[] params) {
        return baseCommonDao.updateBySql(sql,params);
    }

    @Override
    public boolean saveorupdate(Object entity) {
        return baseCommonDao.saveorupdate(entity);
    }

    @Override
    public Serializable saveReturnId(Object entity) {
        return baseCommonDao.saveReturnId(entity);
    }

    @Override
    public Serializable mergeData(Object entity,Serializable id) {
            if(id==null)
                return  saveReturnId(entity);
            else{
                Object entity2=getEntity(entity.getClass(),id);
                BeanUtil.copyNotNull2Bean(entity,entity2);
                update(entity2);
                entity=null;
            }
        return id;
    }

    @Override
    public Serializable mergeOneToMany(Object mainEntity, String mapBy, List<?> subEntityList) {
        Serializable id=null;
        boolean mainExist=true;//主表是否存在
        try {
            id= BeanUtils.getProperty(mainEntity,"id");
            Object mainEntity2=null;
            //若不存在 则保存，若存在则取出
            if(id==null){
                mainExist=false;
                id=saveReturnId(mainEntity);
                mainEntity2=mainEntity;
            }else{
                mainEntity2=getEntity(mainEntity.getClass(),id);
                BeanUtil.copyNotNull2Bean(mainEntity,mainEntity2);
                update(mainEntity2);
            }
            if(CollectionUtils.isNotEmpty(subEntityList)){
                for(Object subEntity:subEntityList){
                    Serializable subId=BeanUtils.getProperty(subEntity,"id");
                    boolean isUpdate=false;
                    if(subId!=null&&subId.toString().length()>0){
                        Object subEntity2=getEntity(subEntity.getClass(),subId);
                        if(subEntity2!=null){
                            BeanUtil.copyNotNull2Bean(subEntity,subEntity2);
                            BeanUtils.setProperty(subEntity2,mapBy,mainEntity2);
                            update(subEntity2);
                            isUpdate=true;
                        }
                    }
                    if(!isUpdate){
                        BeanUtils.setProperty(subEntity,"id",null);
                        BeanUtils.setProperty(subEntity,mapBy,mainEntity2);
                        save(subEntity);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public boolean cascadeDel(Class clazz, Serializable id) {
        boolean b=true;
         Object o=getEntity(clazz,id);
        try{
            baseCommonDao.delete(o);
        }catch (Exception e){
            b=false;
        }
        return b;
    }

    @Override
    public <T> List<T> findDataList(DetachedCriteria criteria) {
        return baseCommonDao.findDataList(criteria);
    }

    @Override
    public int findCount(DetachedCriteria criteria) {
        return baseCommonDao.findCount(criteria);
    }

    @Override
    public DataGrid fillDataGrid(DetachedCriteria criteria, DataGrid d) {
        return baseCommonDao.fillDataGrid(criteria,d);
    }

    @Override
    public int updateByHql(String hql, Map<String, Object> map) {
        return baseCommonDao.updateByHql(hql,map);
    }

    @Override
    public <T> List<T> getDataByHql(String hql, Map<String, Object> paramMap) {
        return baseCommonDao.getDataByHql(hql,paramMap);
    }

    @Override
    public int getCountBySql(String sql, Map<String, Object> paramMap) {
        return baseCommonDao.getCountBySql(sql,paramMap);
    }

    @Override
    public List<Map<String, Object>> findDataBySql(String sql, Map<String, Object> paramMap, int begin, int count) {
        return baseCommonDao.findDataBySql(sql,paramMap,begin,count);
    }

    @Override
    public boolean delUseFlag(Object o) {
        if(BaseEntity.class.isAssignableFrom(o.getClass())){
            BaseEntity entity= (BaseEntity) o;
            entity=  getEntity(o.getClass(),((BaseEntity) o).getId());
            entity.set_dataState(2);
            return update(entity);
        }else{
            return delete(o);
        }
    }
    @Ehcache
    @Override
    public Map<String, String> getTableClassMap() {
        return baseCommonDao.getTableClassMap();
    }

    @Override
    public int delUseFlag(String entityName, Serializable[] ids) {
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("ids",ids);
        return updateByHql(" update "+entityName+" set _dataState='2' where id in (:ids) ",paramMap);
    }

    @Override
    public <T> List<T> findDataList(DetachedCriteria criteria, boolean enableFilter) {
        return baseCommonDao.findDataList(criteria,enableFilter);
    }
}
