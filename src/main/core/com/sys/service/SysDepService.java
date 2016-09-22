package com.sys.service;

import com.sys.entity.SysDepEntity;
import com.sys.entity.TreeBean;

import java.util.List;

public interface SysDepService extends ICommonService {
    /**
     * 获取treebean集合
     * @param pid 父id
     * @return
     */
    public  List<TreeBean> getTreeList(long pid);

    /**
     * 删除节点及子节点
     * @param id
     * @return
     */
    public boolean deleteWithChildren(Long id);



    /**
     * 获取本部门id及子部门id
     * @param pid
     * @return
     */
    public List<Long> getChildrenId(long pid);
}
