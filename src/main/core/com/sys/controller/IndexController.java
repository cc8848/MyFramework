package com.sys.controller;

import com.sys.entity.Client;
import com.sys.entity.SysMenuEntity;
import com.sys.entity.SysRoleEntity;
import com.sys.manager.ClientManager;
import com.sys.service.SysMenuService;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author zzl
 * Date:2014-09-01
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    @Resource
    private SysMenuService sysMenuService;
    /**
     * admin专有菜单-电子签章id
     */
    private static final Long ADMIN_MENU = 1433484919359L;
    @RequestMapping(params = "menulist")
    public String getTopMenu(HttpServletRequest request){
        List<SysMenuEntity> menuEntityList=new ArrayList<>(0);
        Client c= ClientManager.getInstance().getCurrentClient();
        if(c!=null){
            List<SysRoleEntity> roleEntityList=c.getRoleEntities();
            if(roleEntityList!=null){
                List<Long> roleIds=new ArrayList<>();
                for(SysRoleEntity roleEntity:roleEntityList){
                    roleIds.add(roleEntity.getId());
                }
                menuEntityList = sysMenuService.getTopMenuWithRole(roleIds);
            }
        }else{
            return "login";
        }
//        HttpSession s=request.getSession();
//        SessionUser sessionUser=(SessionUser)s.getAttribute("login_session_user");
//        if("admin".equals(sessionUser.getUserName())){
//            menuEntityList.add(sysMenuService.<SysMenuEntity>getEntity(SysMenuEntity.class, ADMIN_MENU));
//        }
        request.setAttribute("menuList",menuEntityList);
        return "../index";
    }
    /**
     * 获取下一级菜单
     * @param menuId
     * @return
     */
    @RequestMapping(params = "childmenu")
    public String getChildMenu(Long menuId,HttpServletRequest request){
        Assert.notNull(menuId);
        List<Criterion> criterions=new ArrayList<>();
        criterions.add(Restrictions.eq("pid",menuId));
        List<SysMenuEntity> menuEntityList=sysMenuService.getDataList(SysMenuEntity.class, criterions, new ArrayList<Order>());
        Client client=ClientManager.getInstance().getCurrentClient();
        if(client!=null&&CollectionUtils.isNotEmpty(menuEntityList)){
            Set<Long> menuSetId=client.getMenuIdSet();
            if(menuSetId!=null){
                for(int i=0;i<menuEntityList.size();i++){
                    if(!menuSetId.contains(menuEntityList.get(i).getId())){
                        menuEntityList.remove(i);
                        i--;
                    }
                }
                Collections.sort(menuEntityList,new BeanComparator("xh"));
                request.setAttribute("menuList",menuEntityList);
            }
         }
        return "sys/menu/index_child_menu";
    }

    /**
     * 通过菜单名称查找
     * @param menuName
     * @param request
     * @return
     */
    @RequestMapping(params = "searchmenu")
    public String findByMenuName(String menuName,HttpServletRequest request){
        if(menuName!=null)
            try {
                menuName= new String(menuName.getBytes("iso-8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        List<Criterion> criterions=new ArrayList<>();
        criterions.add(Restrictions.like("text", "%" + menuName + "%"));
        List<SysMenuEntity> menuEntityList=sysMenuService.getDataList(SysMenuEntity.class, criterions, null);
        request.setAttribute("menuList",menuEntityList);
        return "sys/menu/index_child_menu";
    }
    @RequestMapping(params = "selectChildByPId")
    @ResponseBody
    public Map<String,Object> selectChildByPId(Long pId,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Assert.notNull(pId);
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq("pid", pId));
        List<SysMenuEntity> menuEntityList = sysMenuService.getDataList(SysMenuEntity.class, criterions, new ArrayList<Order>());
        Client client = ClientManager.getInstance().getCurrentClient();
        if (client != null && CollectionUtils.isNotEmpty(menuEntityList)) {
            Set<Long> menuSetId = client.getMenuIdSet();
            if (menuSetId != null) {
                for (int i = 0; i < menuEntityList.size(); i++) {
                    if (!menuSetId.contains(menuEntityList.get(i).getId())) {
                        menuEntityList.remove(i);
                        i--;
                    }
                }
                Collections.sort(menuEntityList, new BeanComparator("xh"));

            }
        }
        if (menuEntityList.size() > 0) {
            map.put("flag", 0);
            map.put("menuEntityList", menuEntityList);
            return map;
        } else {
            List<SysMenuEntity> childMenuList = new ArrayList<>();
            SysMenuEntity e = sysMenuService.getEntity(SysMenuEntity.class, pId);
            childMenuList.add(e);
            map.put("flag", 1);
            map.put("menuEntityList", childMenuList);
            return map;
        }
    }
}
