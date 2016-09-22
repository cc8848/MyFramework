package com.sys.dao.impl;

import com.sys.dao.IBaseCommonDao;
import com.sys.util.DataFilterUtil;
import com.sys.util.DataGrid;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 *  Date:2014-07-28
 */
@Repository("baseCommonDao")
public class BaseCommonDao implements IBaseCommonDao {
    private static Logger logger=Logger.getLogger(BaseCommonDao.class);
    @Resource
    private SessionFactory sessionFactory;
    public Session getSession(){
        Session session= sessionFactory.getCurrentSession();
        DataFilterUtil.enableFilters(session);
        return session;
    }

    /**
     * 获取无过滤器的session
     * @return
     */
    public Session getSession(boolean enableFilter){
        Session session= sessionFactory.getCurrentSession();
        if(enableFilter)
            DataFilterUtil.enableFilters(session);
        return session;
    }
    @Override
    public boolean save(Object entity) {
        try{
            getSession().persist(entity);
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Object entity) {
        try{
            getSession().merge(entity);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int bulkSave(List list) {
        int i=0;
        try{
            Assert.notEmpty(list);
            for(;i<list.size();i++){
                getSession().persist(list.get(i));
                if(1%20==0){
                    getSession().flush();;
                    getSession().clear();
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
        return i+1;
    }

    @Override
    public boolean delete(Object clazz) {
        try{
            getSession().delete(clazz);
            getSession().flush();
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public int updateByHql(String hql, Object[] params) {
        int count=0;
        try{
            Query query=getSession().createQuery(hql);
            if(params!=null&&params.length>0)
                for(int i=0;i<params.length;i++){
                    if(params[i]!=null){
                       query.setParameter(i,params[i]);
                    }

                }
            count=query.executeUpdate();
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
        return count;
    }

    @Override
    public <T> T getEntity(Class entity, Serializable id) {
        T t=null;
        try{
            t=(T)getSession().get(entity,id);
            if(t!=null){
                getSession().flush();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
        return t;
    }

    @Override
    public int getTotalCount(Criteria criteria) {
         criteria.setProjection(Projections.rowCount());
         long total= (long) criteria.uniqueResult();
        return (int)total;
    }

    @Override
    public <T> List<T> getDataList(Criteria criteria,List<Order> orders) {
        fillOrders(criteria,orders);
        return criteria.list();
    }

    @Override
    public <T> List<T> getPageDateList(Criteria criteria, DataGrid datagrid) {
        if(StringUtils.isNotEmpty(datagrid.getOrders())){
            String[] orders=datagrid.getOrders().split(",");
            if(orders!=null&&orders.length>0){
               for(int i=0;i<orders.length;i++){
                   String[] order=orders[i].split(":");
                   if(order!=null){
                       if(order.length==2&&"desc".equals(order[1])){
                           criteria.addOrder(Order.desc(order[0]));
                       }else{
                           criteria.addOrder(Order.asc(order[0]));
                       }
                   }

               }
            }
        }
        if(datagrid.getPagesize()>0){
            criteria.setFirstResult(datagrid.getCurrentPoint());
            criteria.setMaxResults(datagrid.getPagesize());
        }
        criteria.setProjection(null);
        return criteria.list();
    }

    @Override
    public DataGrid fillDataGrid(Criteria criteria, DataGrid datagrid) {
        datagrid.setTotalCount(getTotalCount(criteria));
        datagrid.setDataList(getPageDateList(criteria, datagrid));
        return datagrid;
    }

    @Override
    public int getCountByHql(String hql, Object[] params) {
          int fromIndex=hql.indexOf("from");
          int orderIndex=hql.indexOf("order");
          StringBuffer hqlbuffer=new StringBuffer("select count(id) ");
          if(orderIndex!=-1)
              hqlbuffer.append( hql.substring(fromIndex,orderIndex-1));
          else
              hqlbuffer.append(hql.substring(fromIndex));
          Query query=fillQueryParams(hqlbuffer.toString(),params);
        long total= (long) query.uniqueResult();
        return (int)total;
    }

    @Override
    public <T> List<T> getDataListByHql(String hql, Object[] params) {
        return fillQueryParams(hql,params).list();
    }

    @Override
    public <T> List<T> getPageDataListByHql(String hql, Object[] params, DataGrid datagrid) {
        Query query=fillQueryParams(hql,params);
        query.setFirstResult(datagrid.getCurrentPoint());
        query.setMaxResults(datagrid.getPagesize());
        return query.list();
    }

    @Override
    public DataGrid fillDataGridByHql(String hql, Object[] params, DataGrid datagrid) {
        datagrid.setTotalCount(getCountByHql(hql,params));
        datagrid.setDataList(getPageDataListByHql(hql,params,datagrid));
        return datagrid;
    }

    @Override
    public <T> List<T> findBySql(String sql, Object[] params) {
        SQLQuery query=getSession().createSQLQuery(sql);
        if(params!=null&&params.length>0){
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query.list();
    }

    @Override
    public int updateBySql(String sql, Object[] params) {
        SQLQuery query=getSession().createSQLQuery(sql);
        if(params!=null&&params.length>0){
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query.executeUpdate();
    }
    @Override
    public   Criteria fillCriterion(Class clazz,List<Criterion> list){
        Criteria criteria=getSession().createCriteria(clazz);
        /*List<String> fieldList= BeanUtil.getAlignField(clazz);
        if(fieldList!=null&&fieldList.size()>0){
            for(String str:fieldList){
                criteria=criteria.createCriteria(str,str);
            }
        }*/
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                criteria.add(list.get(i));
            }

        }
        return criteria;
    }

    /**
     * 设置排序
     * @param list
     * @return
     */
    public Criteria fillOrders(Criteria criteria, List<Order> list){
        if(list!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                criteria.addOrder(list.get(i));
            }
        }
        return criteria;
    }
    public  Query fillQueryParams(String hql,Object[] params){
        Query query=getSession().createQuery(hql);
        if(params!=null&&params.length>0){
            for(int i=0;i<params.length;i++){
                query.setParameter(i,params[i]);
            }
        }
        return query;
    }

    @Override
    public boolean saveorupdate(Object entity) {
        try{
            getSession().saveOrUpdate(entity);
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Serializable saveReturnId(Object entity) {
        return getSession().save(entity);
    }

    @Override
    public <T> List<T> findDataList(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getSession()).list();
    }

    @Override
    public int findCount(DetachedCriteria criteria) {
       return getTotalCount(criteria.getExecutableCriteria(getSession()));
    }

    @Override
    public DataGrid fillDataGrid(DetachedCriteria criteria, DataGrid d) {
        return fillDataGrid(criteria.getExecutableCriteria(getSession()),d);
    }

    @Override
    public int updateByHql(String hql, Map<String,Object> map) {
        int count=0;
        try{
            Query query=getSession().createQuery(hql);
               for(String str:map.keySet()){
                   Object o=map.get(str);
                   if(o instanceof java.util.Collection){
                       query.setParameterList(str, (java.util.Collection) o);
                   }else if(o.getClass().isArray()){
                       query.setParameterList(str, (Object[]) o);
                   } else{
                       query.setParameter(str,o);
                   }
               }
            count=query.executeUpdate();
        }catch (Exception e){
            logger.error(e.getMessage());
            return -1;
        }
        return count;
    }

    @Override
    public <T> List<T> getDataByHql(String hql, Map<String, Object> paramMap) {
        List<T> list=null;
        try{
            Query query=getSession().createQuery(hql);
            for(String str:paramMap.keySet()){
                Object o=paramMap.get(str);
                if(o instanceof java.util.Collection){
                    query.setParameterList(str, (java.util.Collection) o);
                }else if(o.getClass().isArray()){
                    query.setParameterList(str, (Object[]) o);
                } else{
                    query.setParameter(str,o);
                }
            }
            list=query.list();
        }catch (Exception e){
             e.printStackTrace();
            logger.error(e.getMessage());
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public int getCountBySql(String sql, Map<String, Object> paramMap) {
        SQLQuery query=getSession().createSQLQuery(sql);
        if(paramMap!=null&&paramMap.size()>0){
           for(String str:paramMap.keySet()){
               Object o=paramMap.get(str);
               if(o instanceof java.util.Collection){
                   query.setParameterList(str, (java.util.Collection) o);
               }else if(o.getClass().isArray()){
                   query.setParameterList(str, (Object[]) o);
               } else{
                   query.setParameter(str,o);
               }
           }
        }
        return (int) query.uniqueResult();
    }

    @Override
    public List<Map<String, Object>> findDataBySql(String sql, Map<String, Object> paramMap, int begin, int count) {
        SQLQuery query=getSession().createSQLQuery(sql);
        if(paramMap!=null&&paramMap.size()>0){
            for(String str:paramMap.keySet()){
                Object o=paramMap.get(str);
                if(o instanceof java.util.Collection){
                    query.setParameterList(str, (java.util.Collection) o);
                }else if(o.getClass().isArray()){
                    query.setParameterList(str, (Object[]) o);
                } else{
                    query.setParameter(str,o);
                }
            }
        }
        if(count!=0){
            query.setFirstResult(begin);
            query.setMaxResults(count);
        }
        query.setResultTransformer(
                Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public Map<String, String> getTableClassMap() {
        List<ClassMetadata> list=new ArrayList<>(sessionFactory.getAllClassMetadata().values());
        Map<String,String> tableClassMap=new HashMap<>();
        if(list!=null&&list.size()>0){
            for(ClassMetadata metadata:list){
                AbstractEntityPersister persister= (AbstractEntityPersister) metadata;
                tableClassMap.put(persister.getTableName().toLowerCase(),persister.getEntityMetamodel().getName());
            }
        }
        return tableClassMap;
    }

    @Override
    public <T> List<T> findDataList(DetachedCriteria criteria, boolean enableFilter) {
        return criteria.getExecutableCriteria(getSession(enableFilter)).list();
    }

    @Override
    public void addMappedClasses(List<Class> classList) {


    }

    @Override
    public <T> boolean bulkUpdate(List<T> list) {
        int i = 0;
        try{
            Assert.notEmpty(list);
            for(;i<list.size();i++){
                getSession().merge(list.get(i));
                if(i%20==0){
                    getSession().flush();;
                    getSession().clear();
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
