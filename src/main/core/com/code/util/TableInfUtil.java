package com.code.util;

import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.code.service.ITableInfService;
import com.sys.util.DataGrid;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Component
public class TableInfUtil {
    @Resource
    private ITableInfService tableInfService;
    private static TableInfUtil tableInfUtil;
    @PostConstruct
    private void init(){
        tableInfUtil=this;
        tableInfUtil.tableInfService=this.tableInfService;
    }

    /**
     * 获取标题名
     * @param tableName
     * @param includeSubTable
     * @param includeFileType
     * @return
     */
    public static Map<String,Object> getTitleMap(String tableName, boolean includeSubTable,boolean includeFileType){
        return tableInfUtil.tableInfService.getTitleMap(tableName,includeSubTable,includeFileType);
    }

    /**
     * 根据id获取数据
     * @param tableName
     * @param ids
     * @param includeSubTable
     * @return
     */
    public static List<Object> getDataListByIDs(String  tableName, Serializable[] ids, boolean includeSubTable){
        return tableInfUtil.tableInfService.getDataListByIDs(tableName,ids,includeSubTable);
    }

    /**
     * 获取分页数据
     * @param criterions
     * @param tableName
     * @param entityClass
     * @param d
     * @param includeSubTable
     * @return
     */
    public static DataGrid fillDatagrid(List<Criterion> criterions,String tableName,Class entityClass,DataGrid d,boolean includeSubTable){
        return tableInfUtil.tableInfService.fillDataGridWithSubTable(criterions,tableName,entityClass,d,includeSubTable);
    }

    /**
     * 获取表信息
     * @param tableName
     * @param includeFields
     * @return
     */
    public static TableHeadBean getHeadInf(String tableName,boolean includeFields){
        return tableInfUtil.tableInfService.getTableHead(tableName,includeFields);
    }
}
