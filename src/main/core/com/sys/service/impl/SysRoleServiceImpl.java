package com.sys.service.impl;

import com.sys.entity.*;
import com.sys.entity.TreeBean;
import com.sys.service.SysRoleService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl extends CommonService implements SysRoleService {
    @Override
    public List<TreeBean> getTreeList(List<Long> pid) {
        return getRoleUserTree(pid,null);
    }

    @Override
    public List<Long> getChildrenId(List<Long> pid,boolean preThrough) {
        List<TreeBean> treeList=getRoleUserTree(pid, null);
        Set<Long> idSet=new HashSet<>();
        for(Long id:pid){
            idSet.addAll(getChildId(id, preThrough, treeList));
        }
        return new ArrayList<>(idSet);
    }
    public List<Long> getChildId(long pid,boolean preThrough,List<TreeBean> treeBeans){
        Set<Long> set=new HashSet<>();
        set.add(pid);
        if(treeBeans!=null){
            for(TreeBean bean:treeBeans){
                set.addAll(getChildId(bean,preThrough));
            }
        }
        return new ArrayList<>(set);
    }
    private Set<Long> getChildId(TreeBean bean,boolean preThrough){
        Set<Long> set=new HashSet<>();
        if(preThrough)
            set.add(bean.getId());
        else
            if(bean instanceof RoleTreeBean){
                RoleTreeBean treeBean=(RoleTreeBean)bean;
                if(treeBean.isPreThrough())
                    set.add(bean.getId());
            }

        List<TreeBean> list=bean.getChildren();
        if(list!=null&&list.size()>0){
            for(TreeBean bean1:list){
                set.addAll(getChildId(bean1,preThrough));
            }
        }
        return set;
    }

    @Override
    public List<TreeBean> getRoleUserTree(List<Long> pids, String userId) {
        List<SysRoleEntity>  list=getDataListByHql("from SysRoleEntity  order by xh,id desc",new Object[]{});
        List<Long> roleIds=new ArrayList<>(0);
        if(userId!=null) {
            roleIds = getDataListByHql("select roleId from RoleUserEntity where userId=?", new Object[]{userId});
        }
        Set<Long> roleSet=new HashSet<>(roleIds);
        Map<Long,List<TreeBean>> treeMap=new HashMap<>();
        List<TreeBean> list1=null;
        Set<Long> pidSet=new HashSet<>(pids);
        List<TreeBean> selfTreeBean=new ArrayList<>();
        boolean checked=false;
        for(SysRoleEntity entity:list){
            checked=false;
            if(roleSet.contains(entity.getId()))
                checked=true;
            if(entity.getPid()==null) entity.setPid(0l);
            if(pidSet.contains(entity.getId()))
                selfTreeBean.add(new RoleTreeBean(entity.getId(),entity.getRoleName(),entity.isPreThrough(),checked));
            list1=treeMap.get(entity.getPid());
            if(list1==null)  list1=new ArrayList<>();
            list1.add(new RoleTreeBean(entity.getId(),entity.getRoleName(),entity.isPreThrough(),checked));
            treeMap.put(entity.getPid(),list1);
        }
        list=null;
        list1=null;
        List<TreeBean> list2=null;
        for(Long ePid:treeMap.keySet()){
            list1=treeMap.get(ePid);
            if(list1!=null){
                for(TreeBean tree:list1){
                    list2=treeMap.get(tree.getId());
                    if(list2!=null)
                        tree.setChildren(list2);
                }
            }
        }
        list2=null;
        list1=null;
        if(selfTreeBean.size()>0){
            for(TreeBean treeBean:selfTreeBean){
                if(treeMap.get(treeBean.getId())!=null){
                    treeBean.setChildren(treeMap.get(treeBean.getId()));
                }
            }
            return selfTreeBean;
        }else{
            return treeMap.get(0l);
        }

    }

    @Override
    public boolean saveRoleUsers(List<RoleUserEntity> userEntities, String userId, long roleId) {
        if(userId!=null)
            updateByHql("delete from RoleUserEntity where userId=?",new Object[]{userId});
        if(roleId!=0)
            updateByHql("delete from RoleUserEntity where roleId=?",new Object[]{roleId});
        if(userEntities!=null){
            for(RoleUserEntity entity:userEntities){
                if(StringUtils.isBlank(entity.getId()))
                    entity.setId(null);
                saveorupdate(entity);
            }
        }
        return true;
    }

    @Override
    public boolean delRoleUser(String[] userIds, long roleId) {
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userIds);
        map.put("roleId",roleId);
        return updateByHql("delete from RoleUserEntity where userId in (:userId) and roleId =:roleId",map)>0;
    }

    @Override
    public List<RoleResourceTreeBean> getRoleResourceTree(long roleId, boolean allMenu) {
        List<RoleMenuEntity> roleMenuEntities=getDataListByHql("from RoleMenuEntity where role_id=?",new Object[]{roleId});
        Map<Long,RoleMenuEntity> roleMenuEntityMap=new HashMap<>();
        if(roleMenuEntities!=null&&roleMenuEntities.size()>0) {
            for (RoleMenuEntity e : roleMenuEntities) {
                roleMenuEntityMap.put(e.getMenuId(),e);
            }
        }
        Set<Long> menuSet=roleMenuEntityMap.keySet();
        List<SysMenuEntity> menuEntities=getMenuList(menuSet,allMenu);
        Map<Long,List<RoleResourceTreeBean>> menuEntityMap=new ConcurrentHashMap<>();
        boolean checked=false;
        List<RoleResourceTreeBean> tempList=new ArrayList<>();
        String button_code=null;
        for(SysMenuEntity entity:menuEntities){
            button_code=null;
            tempList=null;
            if(roleMenuEntityMap.get(entity.getId())!=null)
                button_code=roleMenuEntityMap.get(entity.getId()).getButtonCode();
            checked=false;
            if(menuSet.contains(entity.getId()))
                checked=true;
            if(entity.getPid()==null)
                entity.setPid(0l);
            tempList=menuEntityMap.get(entity.getPid());
            if(tempList==null)
                tempList=new ArrayList<>();
            Map<String,Object> attributes=new HashMap<>();
            attributes.put("button_method",button_code);
            tempList.add(new RoleResourceTreeBean(entity.getId(),entity.getText(),entity.getMenuLink(),checked,attributes));
            menuEntityMap.put(entity.getPid(),tempList);
        }
        relationChildren(menuEntityMap,false);
        List list=menuEntityMap.get(0l);

        return list;
    }

    /**
     * 获取菜单
     * @param menuIdSet 指定的菜单id集合
     * @param allMenu 是否获取所有菜单
     * @return
     */
    private List<SysMenuEntity> getMenuList(Set<Long> menuIdSet,boolean allMenu){
        List<SysMenuEntity> menuEntities=new ArrayList<>(0);
        if(allMenu)
            menuEntities=getDataListByHql("from SysMenuEntity order by xh,id desc",new Object[]{});
        else{
            if(menuIdSet.size()>0){
                List<Criterion> criterions=new ArrayList<>();
                criterions.add(Restrictions.in("id",menuIdSet));
                List<Order> orders=new ArrayList<>();
                orders.add(Order.asc("xh"));
                orders.add(Order.desc("id"));
                menuEntities=getDataList(SysMenuEntity.class,criterions,orders);
            }

        }
        return menuEntities;
    }
    /**
     * 组合树和他的子节点
     * @param map
     * @param clearKey 是否清除有父节点的数据
     */
    protected void relationChildren(Map<Long,List<RoleResourceTreeBean>> map,boolean clearKey){
        List<RoleResourceTreeBean> list=null;
        List<RoleResourceTreeBean> list2=null;
        for(Long pid:map.keySet()){
            list=map.get(pid);
            if(list!=null){
                for(RoleResourceTreeBean tree:list){
                    list2=map.get(tree.getId());
                    if(list2!=null){
                        tree.setChildren(list2);
                        if(clearKey)
                            map.remove(tree.getId());
                    }

                }
            }
        }
    }

    @Override
    public boolean saveRoleResource(List<RoleMenuEntity> list, long roleId) {
        updateByHql("delete from RoleMenuEntity where roleId=?",new Object[]{roleId});
        if(list!=null){
            for(RoleMenuEntity entity:list){
                if(StringUtils.isBlank(entity.getId())){
                    entity.setId(null);
                    save(entity);
                }
            }
        }
        return true;
    }

    @Override
    public boolean updateButtonResource(String button_method, long menuId, long roleId) {
        List<RoleMenuEntity> list = getDataListByHql(" from RoleMenuEntity where  roleId=? and menuId=?", new Object[]{roleId, menuId});
        for (RoleMenuEntity entity : list) {
            entity.setButtonCode(button_method);
            update(entity);
        }
       return  true;
    }

    @Override
    public List<SysRoleEntity> getRolesByUserId(String userId) {
        List<SysRoleEntity> roleEntities=getDataListByHql("select distinct(role) from SysRoleEntity role,RoleUserEntity role_user where role.id=role_user.roleId and role_user.userId=?",new Object[]{userId});
        return roleEntities;
    }
}
