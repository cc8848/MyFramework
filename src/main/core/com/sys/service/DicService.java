package com.sys.service;


import com.code.entity.BaseTreeEntity;
import com.sys.entity.ComboBox;
import com.sys.entity.ComboTree;

import java.util.List;

/**
 * @author zzl
 *         Date:2014-07-28
 */
public interface DicService  extends ICommonService {
    /**
     * 通过条件获取字典项combobox列表
     * @param dicKey
     * @param dicValue
     * @param dicTable
     * @return
     */
    public List<ComboBox> getDicComboBoxes(String dicKey, String dicValue, String dicTable);

    /**
     * 获取combotree
     * @param dicKey
     * @param dicValue
     * @param dicTable
     * @return
     */
    public List<ComboTree> getDicComboTree(String dicKey, String dicValue, String dicTable);
    /**
     * 获取字典值
     * @param dicKey 字典key对应字段
     * @param dicValue 字典value对应字段
     * @param dicTable 字典表
     * @param dicKeyValue 字典编码
     * @return
     */
    public String getDicVal(String dicKey, String dicValue, String dicTable, String dicKeyValue);

    /**
     * 获取字典编码
     * @param dicKey 字典key对应字段
     * @param dicValue 字典value对应字段
     * @param dicTable 字典表
     * @param dicKeyValue 字典值
     * @return
     */
    public Object getDicCode(String dicKey, String dicValue, String dicTable, String dicKeyValue);
    /**
     * 通过表名获取类名
     * @param tableName
     * @return
     */
    public String getClassName(String tableName);

}
