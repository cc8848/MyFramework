package com.sys.controller;

import com.google.gson.reflect.TypeToken;
import com.sys.constant.Globals;
import com.sys.entity.*;
import com.sys.service.ISystemService;
import com.sys.service.SysRoleService;
import com.sys.util.SuccessMsg;
import com.sys.util.BeanUtil;
import com.sys.util.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private ISystemService systemService;
    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "sys_role")
    public String sys_role(){
    return "sys/role/sys_role_list";
    }
    /**
     * 获取树列表
     * @param pid
     * @return
     */
    @RequestMapping(params = "gettree")
    @ResponseBody
    public List<TreeBean> getTree(Long[] pid){
        if(pid==null)
            pid=new Long[0];
        return sysRoleService.getTreeList(new ArrayList<Long>(Arrays.asList(pid)));
    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(SysRoleEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        if(bean.getId()==null||bean.getId()==0){
            bean.setId(System.currentTimeMillis());
            json.setSuccess(sysRoleService.save(bean));
        }else{
            SysRoleEntity sysRoleEntity=sysRoleService.getEntity(SysRoleEntity.class,bean.getId());
            if(sysRoleEntity!=null) sysRoleEntity=new SysRoleEntity();
            BeanUtil.copyNotNull2Bean(bean,sysRoleEntity);
            json.setSuccess(sysRoleService.update(sysRoleEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(SysRoleEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public SysRoleEntity get(Long id){
        Assert.notNull(id);
        return  sysRoleService.getEntity(SysRoleEntity.class,id);
    }

    /**
    * 获取详情
    * @param id
    * @param request
    * @return
    */
    @RequestMapping(params = "detail")
    public String detail(Long id,HttpServletRequest request){
        Assert.notNull(id);
        request.setAttribute("bean",sysRoleService.getEntity(SysRoleEntity.class,id));
        return "sys/role/sys_role_detail";
    }
    /**
    * 删除
    * @param id
    * @return
    */
    @RequestMapping(params = "del")
    @ResponseBody
    public SuccessMsg del(Long id){
        SuccessMsg j=new SuccessMsg();
        SysRoleEntity d=new SysRoleEntity();
        d.setId(id);
        j.setSuccess(sysRoleService.delete(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(SysRoleEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
        return j;
    }

    /**
    * 批量删除
    * @param ids
    * @return
    */
    @RequestMapping(params = "muldel")
    @ResponseBody
    public SuccessMsg mulDel(Long[] ids){
        SuccessMsg j=new SuccessMsg();
        if (ids != null) {
            j.setSuccess(true);
            j.setMsg("成功删除"+ids.length+"条记录");
            for(int i=0;i<ids.length;i++){
                if(!sysRoleService.delete(new SysRoleEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(SysRoleEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

    /**
     * 获取带用户权限的角色树
     * @param pid
     * @param userId
     * @return
     */
    @RequestMapping(params = "role_user_tree")
    @ResponseBody
    public List<TreeBean> getRoleTreeWithPer(Long[] pid,String userId){
        return sysRoleService.getRoleUserTree(new ArrayList<Long>(Arrays.asList(pid)),userId);
    }

    /**
     * 保存角色用户关系
     * @param jsonData
     * @return
     */
    @RequestMapping(params = "saveroleuser")
    @ResponseBody
    public SuccessMsg saveRoleUser(String jsonData,String userId,Long roleId){
        SuccessMsg j=new SuccessMsg(true,"授权成功！");
        String logType=Globals.LOG_UPDATE;
        if(roleId==null) roleId=0l;
        List<RoleUserEntity> list= JsonUtil.getGson().fromJson(jsonData,new TypeToken<List<RoleUserEntity>>(){}.getType());
        sysRoleService.saveRoleUsers(list,userId,roleId);
        systemService.addLog(SysRoleEntity.class.getSimpleName()+j.getMsg(),logType);
        return j;
    }

    /**
     * 删除用户角色信息
     * @param userIds
     * @param roleId
     * @return
     */
    @RequestMapping(params = "delroleuser")
    @ResponseBody
    public SuccessMsg delRoleUser(String[] userIds,Long roleId){
        SuccessMsg j=new SuccessMsg(true,"取消授权成功！");
        j.setSuccess(sysRoleService.delRoleUser(userIds,roleId));
        if(!j.isSuccess()) j.setMsg("取消授权失败");
        return j;
    }

    /**
     * 获取所有菜单及角色拥有的菜单权限
     * @param roleId
     * @return
     */
    @RequestMapping(params = "roleresourcetree")
    @ResponseBody
    public List<RoleResourceTreeBean> getRoleResourceTree(Long roleId){
        if(roleId==null)
            roleId=0l;
        return sysRoleService.getRoleResourceTree(roleId,true);
    }

    /**
     * 保存角色权限信息
     * @param jsonData
     * @return
     */
    @RequestMapping(params = "saveresource")
    @ResponseBody
    public SuccessMsg saveRoleResource(String jsonData,Long roleId){
        SuccessMsg j=new SuccessMsg(true,"保存成功");
        List<RoleMenuEntity> list=JsonUtil.getGson().fromJson(jsonData,new TypeToken<List<RoleMenuEntity>>(){}.getType());
        if(roleId==null) roleId=0l;
        if(!sysRoleService.saveRoleResource(list,roleId)){
            j.setSuccess(false);
            j.setMsg("保存失败！");
        }
        systemService.addLog(SysRoleController.class.getSimpleName()+j.getMsg(),Globals.LOG_INSERT);
        return j;
    }

    /**
     * 更新角色按钮权限
     * @param button_method
     * @param roleId
     * @param menuId
     * @return
     */
    @RequestMapping(params = "updatebutton")
    @ResponseBody
    public SuccessMsg updateRoleButtonResource(String button_method,Long roleId,Long menuId){
        SuccessMsg j=new SuccessMsg(true,"保存成功");
        if(roleId==null) roleId=0l;
        if(menuId==null) menuId=0l;
        j.setSuccess(sysRoleService.updateButtonResource(button_method,menuId,roleId));
        if(!j.isSuccess())
            j.setMsg("保存失败！");
        systemService.addLog(SysRoleController.class.getSimpleName()+j.getMsg(),Globals.LOG_UPDATE);
        return j;

    }
}
