package com.sys.service.impl;

import com.sys.entity.MenuButtonEntity;
import com.sys.entity.SysMenuEntity;
import com.sys.entity.SysRoleEntity;
import com.sys.entity.TreeBean;
import com.sys.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysMenuService")
@Transactional
public class SysMenuServiceImpl extends CommonService implements SysMenuService {
    @Override
    public List<SysMenuEntity> getTreeList(Long pid) {
        if(pid==null) pid=0l;
        List<SysMenuEntity>  list=getDataListByHql("from SysMenuEntity  order by xh,id desc",new Object[]{});
        Map<Long,List<SysMenuEntity>> treeMap=new HashMap<>();
        List<SysMenuEntity> list1=null;
        SysMenuEntity treeBean=null;
        for(SysMenuEntity entity:list){
            if(entity.getPid()==null)
                entity.setPid(0l);
            if(entity.getId()==pid)
                treeBean=entity;
            list1=treeMap.get(entity.getPid());
            if(list1==null)
                list1=new ArrayList<>();
            list1.add(entity);
            treeMap.put(entity.getPid(),list1);
        }
        list=null;
        list1=null;
        List<SysMenuEntity> list2=null;
        for(Long ePid:treeMap.keySet()){
            list1=treeMap.get(ePid);
            if(list1!=null){
                for(SysMenuEntity tree:list1){
                    list2=treeMap.get(tree.getId());
                    if(list2!=null)
                        tree.setChildren(list2);
                }
            }
        }
        list2=null;
        list1=null;
        List<SysMenuEntity> childList=treeMap.get(pid);
        if(treeBean!=null){
            treeBean.setChildren(childList);
            List<SysMenuEntity> treeBeans=new ArrayList<>();
            treeBeans.add(treeBean);
            return treeBeans;
        }else{
            return childList;
        }
    }

    @Override
    public boolean deleteWithChildren(Long id) {
        return false;
    }

    @Override
    public List<Long> getChildrenId(long pid) {
        List<SysMenuEntity> treeList=getTreeList(pid);
        Set<Long> set=new HashSet<>();
        set.add(pid);
        if(treeList!=null){
            for(SysMenuEntity bean:treeList){
                set.addAll(getChildId(bean));
            }
        }
        return new ArrayList<>(set);
    }
    private Set<Long> getChildId(SysMenuEntity bean){
        Set<Long> set=new HashSet<>();
        set.add(bean.getId());
        List<SysMenuEntity> list=bean.getChildren();
        if(list!=null&&list.size()>0){
            for(SysMenuEntity bean1:list){
                set.addAll(getChildId(bean1));
            }
        }
        return set;
    }

    @Override
    public List<MenuButtonEntity> getMenuButtonList(long menuId) {
        return getDataListByHql("from MenuButtonEntity where menuId=?",new Object[]{menuId});
    }

    @Override
    public boolean delMenu(long menuId) {
        boolean success=true;
        success=success&&updateByHql("delete from MenuButtonEntity where menuId=?",new Object[]{menuId})>0;
        success=success&&delete(new SysMenuEntity(menuId));
        return success;
    }

    @Override
    public List<SysMenuEntity> getTopMenuWithRole(List<Long>roleIds) {
        Map<String,Object> paraMap=new HashMap<>();
        paraMap.put("roleIds",roleIds);
        return getDataByHql("select distinct(menu) from SysMenuEntity menu,RoleMenuEntity resource where (menu.pid is null or menu.pid =0) and menu.id=resource.menuId and resource.roleId in (:roleIds) order by menu.xh", paraMap);
    }

    @Override
    public List<SysMenuEntity> getRoleMenuAndButtons(List<Long> roleIds) {
        Map<String,Object> paramMap=new HashMap<>();
        paramMap.put("roleIds",roleIds);
        List<Object[]> objects=getDataByHql(" select menu,resource.buttonCode from SysMenuEntity menu,RoleMenuEntity resource where  menu.id=resource.menuId and resource.roleId in(:roleIds)",paramMap);
        List<SysMenuEntity> menuEntityList=new ArrayList<>();
        Map<Long,Set<String>> menu_buttonsMap=new HashMap<>();
        SysMenuEntity tempMenuEntity=null;
        String button_code=null;
        String[] button_codes=null;
        Set<String> tempSet=null;
        for(Object[] objects1:objects){
            button_code=null;
            button_codes=null;
            tempSet=null;
            tempMenuEntity=null;
            tempMenuEntity=(SysMenuEntity)objects1[0];
            menuEntityList.add(tempMenuEntity);
            button_code=(String)objects1[1];
            tempSet=new HashSet<>();
            if(StringUtils.isNotBlank(button_code)){
                button_codes=button_code.split(";");
                for(String str:button_codes){
                    if(str!=null)
                        tempSet.add(str.split(":")[0]);
                }

            }
            Set<String> tSet=menu_buttonsMap.get(tempMenuEntity.getId());
            if(tSet!=null)
                tempSet.addAll(tSet);
            menu_buttonsMap.put(tempMenuEntity.getId(),tempSet);
        }
        List<MenuButtonEntity> buttonEntityList=new ArrayList<>(0);
        if(menu_buttonsMap.size()>0){
            List<Criterion> criterions=new ArrayList<>();
            criterions.add(Restrictions.in("menuId",menu_buttonsMap.keySet()));
            buttonEntityList=getDataList(MenuButtonEntity.class, criterions,null);
        }
         Map<Long,List<MenuButtonEntity>> buttonMap=new HashMap<>();
        for(MenuButtonEntity button:buttonEntityList){
            List<MenuButtonEntity> tempButtonList=buttonMap.get(button.getMenuId());
            if(tempButtonList==null)
                tempButtonList=new ArrayList<>();
            if(menu_buttonsMap.get(button.getMenuId())!=null&&menu_buttonsMap.get(button.getMenuId()).contains(button.getButtonCode())){
                button.setHasPermission(true);
            }
            tempButtonList.add(button);
            buttonMap.put(button.getMenuId(),tempButtonList);
        }
        for(SysMenuEntity menu:menuEntityList){
            if(buttonMap.get(menu.getId())!=null){
                menu.setButtonEntities(buttonMap.get(menu.getId()));
            }
        }
        return menuEntityList;
    }

}
