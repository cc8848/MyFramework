package com.code.service;

import com.code.entity.TableFieldBean;
import com.sys.service.ICommonService;

import java.util.List;

/**
 * @author zzl
 *         Date:2014-08-07
 */
public interface ITableFieldService extends ICommonService {
    /**
     * 通过表名查找有存储文件的字段
     * @param tableName
     * @return
     */
    public List<TableFieldBean> findFileField(String tableName);
}
