package com.code.service;

import com.code.entity.ModuleExtEntity;
import com.sys.service.ICommonService;

import java.io.Serializable;
import java.util.List;

public interface ModuleExtService  extends ICommonService {
    /**
     * 查询表对应的扩展组件
     * @param tableName
     * @param includeDetail 是否包含组件详情
     * @return
     */
    public List<ModuleExtEntity> getModuleExtList(String tableName, boolean includeDetail);

    /**
     * 更新表对应的组件
     * @param tableName
     * @param ids
     * @return
     */
    public boolean updateModules(String tableName, String[] ids);

    /**
     * 删除组件及组件详情
     * @param id
     * @return
     */
    public boolean delModule(String id);

    /**
     * 获取所有组件并将已有组件选中
     * @param tableName
     * @return
     */
    public List<ModuleExtEntity> getAllAndCheckedList(String tableName);

    /**
     *
     * @param tableName
     * @param type
     * @return
     */
    public List<ModuleExtEntity> getModulesDetailLink(String tableName, String type);

    /**
     * 保存组件信息
     * @param extEntity
     * @return
     */
    public  Serializable updateModules(ModuleExtEntity extEntity);
}
