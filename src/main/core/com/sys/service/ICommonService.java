package com.sys.service;

import com.sys.util.DataGrid;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 *         Date:2014-07-28
 */
public interface ICommonService {
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

    public <T> boolean bulkUpdate(List<T> list);

    /**
     * 批量保存
     * @param list
     * @return
     */
    public int bulkSave(List<Object> list);

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
     * @param clazz
     * @param list
     * @return
     */
    public int getTotalCount(Class clazz, List<Criterion> list);

    /**
     * 获取数据列表
     * @param clazz
     * @param list
     * @param orders
     * @param <T>
     * @return
     */

    public <T> List<T> getDataList(Class clazz, List<Criterion> list, List<Order> orders);

    /**
     * 通过页面信息获取数据列表
     * @param clazz
     * @param list
     * @param datagrid
     * @param <T>
     * @return
     */
    public <T> List<T> getPageDateList(Class clazz, List<Criterion> list, DataGrid datagrid);

    /**
     * 获取页面信息
     * @param clazz
     * @param list
     * @param datagrid
     * @return
     */
    public DataGrid fillDataGrid(Class clazz, List<Criterion> list, DataGrid datagrid);

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
     * 保存或更新
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
     * 根据id是否为空判断是保存或更新，更新时只更新非空字段
     * @param entity
     * @param id
     * @return id
     */
    public Serializable mergeData(Object entity, Serializable id);

    /**
     * 保存或更新一对多关系
     * @param mainEntity 主表实体
     * @param mapBy 子表中 关联主表的变量名称
     * @param subEntityList 子表集合
     * @return
     */
    public Serializable mergeOneToMany(Object mainEntity, String mapBy, List<?> subEntityList);

    /**
     * 级联删除
     * @param clazz
     * @param id
     * @return
     */
    public boolean cascadeDel(Class clazz, Serializable id);
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
     * 逻辑删除，若实体中无删除表标识则直接删除
     * @param o
     * @return
     */
    public boolean delUseFlag(Object o);

    /**
     * 获取表名与实体名对应关系
     * @return
     */
    public Map<String,String> getTableClassMap();

    /**
     * 批量逻辑删除
     * @param entityName
     * @param ids
     * @return
     */
    public int delUseFlag(String entityName, Serializable[] ids);
}
