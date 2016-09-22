package com.code.service;

import com.code.core.CodeType;
import com.code.entity.BaseEntity;
import com.code.entity.DhConfigBean;
import com.code.entity.TableHeadBean;
import com.sys.service.ICommonService;
import com.sys.util.DataGrid;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 14-8-1.
 */
public interface ITableInfService extends ICommonService {
    /**
     * 保存表信息
     * @param tableHeadBean
     * @return
     */
    public boolean saveTableInf(TableHeadBean tableHeadBean);

    /**
     * 查询实体中属性对应的字段信息key为属性名，value为对应的TableFieldBean或此类型的map
     * @param tableName 表名
     * @param includeSubTable 是否包含子表信息
     * @param includeFileType 是否包含文件类型的描述
     * @return
     */
    public Map<String,Object> getFieldInf(String tableName, boolean includeSubTable, boolean includeFileType);

    /**
     * 通过表名获取字段的中文描述
     * @param tableName
     * @param includeSubTable
     * @param includeFileType 是否包含文件类型的描述
     * @return
     */
    public Map<String,Object> getTitleMap(String tableName, boolean includeSubTable, boolean includeFileType);

    /**
     * 获取主表信息
     * 若无主表则返回null
     * @param tableName
     * @param includeFields 是否包含字段信息
     * @return
     */
    public TableHeadBean getMainTableHead(String tableName, boolean includeFields);

    /**
     * 获取子表信息
     * 若无子表则返回null
     * @param includeFields 是否包含字段信息
     * @param tableName
     * @return
     */
    public List<TableHeadBean> getSubTableHead(String tableName, boolean includeFields);

    /**
     * 获取表信息
     * @param tableName
     * @param includeFields 是否包含字段信息
     * @return
     */
    public TableHeadBean getTableHead(String tableName, boolean includeFields);

    /**
     * 通过id集合查询
     * @param tableName
     * @param ids
     * @param includeSubTable 包括子表
     * @return
     */
    public List<Object> getDataListByIDs(String tableName, Serializable[] ids, boolean includeSubTable);

    /**
     * 通过表名获取类名
     * @param tableName
     * @return
     */
    public String getClassName(String tableName);

    /**
     * 通过表名获取内容
     * @param tableName
     * @return
     */
    public String  getTableContent(String tableName);

    /**
     * 通过表名获取表描述
     * @param tableName
     * @return
     */
    public String  getTableContentTo(String tableName);

    /**
     * 分页查询数据
     * @param criterions
     * @param tableName
     * @param d
     * @param includeSubTable
     * @return
     */
    public DataGrid fillDataGridWithSubTable(List<Criterion> criterions, String tableName, Class entityClass, DataGrid d, boolean includeSubTable);

    /**
     * 获取档号配置
     * @param tableName
     * @return
     */
    public DhConfigBean getDhConfig(String tableName);

    /**
     * 获取档号最末组成部分的最高值
     * @param tableName
     * @param dhColName 档号字段
     * @param dhPrefix 档号除最末组成部分的部分
     * @return
     */
    public int getdhMaxNum(String tableName, String dhColName, String dhPrefix);

    /**
     * 物理删除
     * @param tableName
     * @param ids
     * @return
     */
    public int readDel(String tableName,Serializable[] ids);
}
