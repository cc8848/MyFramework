package com.sys.dao;

import com.sys.util.DataGrid;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * dao基础接口
 * @author zzl
 * Date:${DATE}
 */
public interface IBaseCommonDao {
    /**
     * 保存
     * @param entity
     */
    public boolean  save(Object entity);

    /**
     * 更新
     * @param entity
     */
    public boolean update(Object entity);

    /**
     * 批量保存
     * @param list
     * @return
     */
    public int bulkSave(List list);

    /**
     * 根据id删除
     * @param clazz
     * @return
     */
    public boolean delete(Object clazz);

    /**
     * 通过hql更新
     * @param hql
     * @param params
     * @return
     */
    public int updateByHql(String hql, Object[] params);

    /**
     * 获取po
     * @param id
     * @param <T>
     */
    public <T> T getEntity(Class entity, Serializable id);

    /**
     * 根据条件返回查询数量
     * @param criteria
     * @return
     */
    public int getTotalCount(Criteria criteria);

    /**
     * 获取数据列表
     * @param criteria
     * @param orders
     * @param <T>
     * @return
     */

    public <T> List<T> getDataList(Criteria criteria, List<Order> orders);

    /**
     * 通过页面信息获取数据列表
     * @param criteria
     * @param datagrid
     * @param <T>
     * @return
     */
    public <T> List<T> getPageDateList(Criteria criteria, DataGrid datagrid);

    /**
     * 获取页面信息
     * @param criteria
     * @param datagrid
     * @return
     */
    public DataGrid fillDataGrid(Criteria criteria, DataGrid datagrid);

    /**
     * 通过hql获取数量
     * @param hql
     * @param params
     * @return
     */
    public int getCountByHql(String hql, Object[] params);

    /**
     * 通过hql获取数据列表
     * @param hql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> getDataListByHql(String hql, Object[] params);

    /**
     * 获取页面数据列表
     * @param hql
     * @param params
     * @param datagrid
     * @param <T>
     * @return
     */
    public <T> List<T> getPageDataListByHql(String hql, Object[] params, DataGrid datagrid);

    /**
     * 填充datagrid
     * @param hql
     * @param params
     * @param datagrid
     * @return
     */
    public DataGrid fillDataGridByHql(String hql, Object[] params, DataGrid datagrid);

    /**
     * 通过sql查询
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public <T> List<T> findBySql(String sql, Object[] params);

    /**
     * 通过sql更新
     * @param sql
     * @param params
     * @return
     */
    public int updateBySql(String sql, Object[] params);
    /**
     * 添加查询条件
     * @param clazz
     * @param list
     * @return
     */
    public   Criteria fillCriterion(Class clazz, List<Criterion> list);

    /**
     * 新增或更新
     * @param entity
     * @return
     */
    public boolean saveorupdate(Object entity);

    /**
     * 保存后返回id
     * @param entity
     * @return
     */
    public Serializable saveReturnId(Object entity);

    /**
     * 获取数据列表
     * @param criteria
     * @param <T>
     * @return
     */
    public <T> List<T> findDataList(DetachedCriteria criteria);
    /**
     * 获取数据列表
     * @param criteria
     * @param enableFilter
     * @param <T>
     * @return
     */
    public <T> List<T> findDataList(DetachedCriteria criteria,boolean enableFilter);

    /**
     * 查询总数
     * @param criteria
     * @return
     */
    public int findCount(DetachedCriteria criteria);

    /**
     * 填充
     * @param criteria
     * @param d
     * @return
     */
    public DataGrid fillDataGrid(DetachedCriteria criteria, DataGrid d);

    /**
     * 通过hql更新
     * @param hql
     * @param map
     * @return
     */
    public int updateByHql(String hql, Map<String, Object> map);
    /**
     * 通过hql查询列表
     * @param hql
     * @param paramMap
     * @return
     */
    public <T> List<T> getDataByHql(String hql, Map<String, Object> paramMap);
    /**
     * 通过sql查询总数
     * @param sql
     * @param paramMap
     * @return
     */
    public int getCountBySql(String sql, Map<String, Object> paramMap);

    /**
     * 通过sql查询数据列表
     * @param sql
     * @param paramMap
     * @param begin
     * @param count
     * @return
     */
    public List<Map<String,Object>> findDataBySql(String sql, Map<String, Object> paramMap, int begin, int count);

    /**
     * 获取表名与实体对应关系
     * @return
     */
    public Map<String,String> getTableClassMap();

    /**
     * 添加hibernate ENTITY
     * @param classList
     */
    public void addMappedClasses(List<Class>  classList);


    <T> boolean bulkUpdate(List<T> list);
}
