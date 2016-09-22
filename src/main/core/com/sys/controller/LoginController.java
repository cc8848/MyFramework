package com.sys.controller;



import com.sys.constant.Globals;
import com.sys.entity.*;
import com.sys.filter.DataFilter;
import com.sys.manager.ClientManager;
import com.sys.service.*;
import com.sys.util.ResourceUtil;
import com.sys.util.SuccessMsg;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 登录
 * @author zzl
 * Date:2014-08-30
 */
@Controller
@RequestMapping("/login")
public class LoginController {
   @Resource
   private SysUserService sysUserService;
   @Resource
   private ISystemService systemService;
   @Resource
   private SysRoleService sysRoleService;
   @Resource
   private SysMenuService sysMenuService;
   @Resource
    private SysDepService sysDepService;


    private final  String loginUrlTableName="login_path";
   @RequestMapping(params = "login")
   public String login(String userName,String passwd,HttpServletRequest request,String type){
        boolean success=false;
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(passwd)){
            request.setAttribute(Globals.SYSTEM_MSG,new SuccessMsg(false,"用户名或密码不能为空！"));
            return "login";
        }

       Client client=ClientManager.getInstance().getCurrentClient();
       if(client!=null)
           client.setDataFilters(new HashSet<DataFilter>(0));
        List<SysUserEntity> userEntities=sysUserService.findByUserNameAndPasswd(userName,passwd);
        HttpSession session=request.getSession();

        if(userEntities!=null&&userEntities.size()>0){
            for(SysUserEntity entity:userEntities){
                if(Objects.equals(1,entity.getState())){
                    session.setAttribute(Globals.SESSIONUSE,new SessionUser(entity));
                    ClientManager.getInstance().addClient(session.getId(),getClient(entity,request));
                    success=true;
                    break;
                }else{
                    request.setAttribute(Globals.SYSTEM_MSG,new SuccessMsg(false,"用户已删除"));
                }
            }
        }else{
            request.setAttribute(Globals.SYSTEM_MSG,new SuccessMsg(false,"用户验证失败！"));
        }
        if(success){
            systemService.addLog(((SessionUser)session.getAttribute(Globals.SESSIONUSE)).getRealName()+"登录成功!",Globals.LOG_LOGIN);
          //  return "redirect:index.do?menulist";
         if(null==type)
             return  "redirect:index.do?menulist";
            else{
             List<Criterion> relationDataCriterionlist = new ArrayList<Criterion>();
             relationDataCriterionlist.add(Restrictions.eq("type", type.toString()));
             return  null;
            }
        }else{
            return "login";
        }
    }


    /**
     * 第三方登录
     * @param userKey
     * @param loginTime
     * @param request
     */
    @RequestMapping(params = "sLogin")
    public String sLogin(String userKey,Long loginTime,HttpServletRequest request,String type){
        Assert.hasLength(userKey,"秘钥不能为空！");
       // Assert.notNull(loginTime,"请求时间不能为空！");
        long sysTime=System.currentTimeMillis();
       // Assert.isTrue((loginTime>(sysTime-5*1000)&&loginTime<=sysTime),"请求时间不正确!");
        SysUserEntity entity=sysUserService.findByUserKey(userKey);
        Assert.notNull(entity,"无效的秘钥！");
        //request.setAttribute("type",type);
        return  login(entity.getUsername(),entity.getPasswd(),request,type);
    }
    /**
     * 退出登录
     * @param request
     * @return
     */
    @RequestMapping(params = "loginout")
    public String loginout(HttpServletRequest request){
        ClientManager.getInstance().removeClient(request.getSession().getId());
        request.getSession().removeAttribute(Globals.SESSIONUSE);
        return "login";
    }
    /**
     * 生成在线用户信息
     * @param entity
     * @param request
     * @return
     */
    private Client getClient(SysUserEntity entity,HttpServletRequest request){
        Client client=new Client();
        List<SysRoleEntity> roleEntityList=sysRoleService.getRolesByUserId(entity.getId());
        client.setUserEntity(entity);
        client.setRoleEntities(roleEntityList);
        client.setLoginTime(new Date());
        client.setIpAddress(ResourceUtil.getIpAddr(request));
        if(roleEntityList!=null){
            List<Long> roleIds=new ArrayList<>();
            for(SysRoleEntity roleEntity:roleEntityList){
                roleIds.add(roleEntity.getId());
            }
            List<Long> childRoleIds=sysRoleService.getChildrenId(roleIds,false);
            List<SysMenuEntity> menuEntityList=sysMenuService.getRoleMenuAndButtons(childRoleIds);
            Set<Long> menuSetId=new HashSet<>();
            if(menuEntityList!=null){
                Map<String,SysMenuEntity> map=new HashMap<>();
                for(SysMenuEntity entity1:menuEntityList){
                   /* if(entity1.getMenuLink()!=null&&entity1.getMenuLink().indexOf("?")!=-1)
                        map.put(entity1.getMenuLink().substring(0,entity1.getMenuLink().indexOf("?")),entity1);
                    else*/
                        menuSetId.add(entity1.getId());
                        map.put(entity1.getMenuLink(),entity1);
                }
                client.setMenuEntityMap(map);
                client.setMenuIdSet(menuSetId);
            }

        }

        SysDepEntity depEntity=entity.getDep();
        if(depEntity!=null){
           // client.addDataFilter(getDataFilter(depEntity.getId()));
        }
        return client;
    }
    private DataFilter getDataFilter(Long depId){
        DataFilter depFilter=new DataFilter("depFilter");
        List<Long> depIds= sysDepService.getChildrenId(depId);
        depFilter.addParam("depId",depIds);
        return depFilter;
    }
}
