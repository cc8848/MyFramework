package com.sys.service;


import com.sys.entity.MenuButtonEntity;
import com.sys.entity.SysMenuEntity;
import com.sys.entity.TreeBean;

import java.util.List;

public interface SysMenuService  extends ICommonService {
    /**
     * 获取treebean集合
     * @param pid 父id
     * @return
     */
    public List<SysMenuEntity> getTreeList(Long pid);
   /**
     * 删除节点及子节点
     * @param id
     * @return
     */
    public boolean deleteWithChildren(Long id);



    /**
     * 获取本级菜单id及后代菜单id
     * @param pid
     * @return
     */
    public List<Long> getChildrenId(long pid);

    /**
     * 通过按钮id查找按钮权限信息
     * @param menuId
     * @return
     */
    public List<MenuButtonEntity> getMenuButtonList(long menuId);

    /**
     * 删除菜单
     * 同时会删除按钮权限信息
     * @param menuId
     * @return
     */
    public boolean delMenu(long menuId);

    /**
     * 根据角色信息查询顶层菜单
     * @param roleIds
     * @return
     */
    public List<SysMenuEntity> getTopMenuWithRole(List<Long> roleIds);

    /**
     * 获取角色下的所有菜单信息，包括按钮权限信息
     * @param roleIds
     * @return
     */
    public List<SysMenuEntity> getRoleMenuAndButtons(List<Long> roleIds);
}
