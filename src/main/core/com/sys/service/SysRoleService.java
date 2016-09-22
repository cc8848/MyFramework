package com.sys.service;

import com.sys.entity.*;

import java.util.List;

public interface SysRoleService  extends ICommonService {
    /**
     * 获取treebean集合
     * @param pid 父id
     * @return
     */
    public List<TreeBean> getTreeList(List<Long> pid);

    /**
     * 获取后代角色id
     * @param ids 角色id数组
     * @param preThrough 是否允许角色权限深入 如为true 则获取所有后台角色权限
     * @return
     */
    public List<Long> getChildrenId(List<Long> ids, boolean preThrough);

    /**
     * 获取带用户权限的角色树
     * @param pid
     * @param userId
     * @return
     */
    public List<TreeBean> getRoleUserTree(List<Long> pid, String userId);

    /**
     * 保存授权信息 同时将删除之前的角色权限
     * 若userID不为0则删除此用户的角色权限
     * 若roleID不为0则删除拥有此角色权限的授权
     * @param userEntities
     * @param userId
     * @param roleId
     * @return
     */
    public boolean saveRoleUsers(List<RoleUserEntity> userEntities, String userId, long roleId);

    /**
     * 取消用户角色授权
     * @param userIds
     * @param roleId
     * @return
     */
    public boolean delRoleUser(String[] userIds, long roleId);

    /**
     * 获取角色拥有的菜单
     * @param roleId
     * @param allMenu 若为true则获取所有菜单，角色拥有的则选中
     * @return
     */
    public List<RoleResourceTreeBean> getRoleResourceTree(long roleId, boolean allMenu);

    /**
     * 保存角色权限信息
     * @param list
     * @param roleId
     * @return
     */
    public boolean saveRoleResource(List<RoleMenuEntity> list, long roleId);

    /**
     * 更新按钮权限
     * @param button_method
     * @param menuId
     * @param roleId
     * @return
     */
    public boolean updateButtonResource(String button_method, long menuId, long roleId);

    /**
     * 通过用户id查询相应角色
     * @param userId
     * @return
     */
    public List<SysRoleEntity> getRolesByUserId(String userId);

}
