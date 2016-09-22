package com.sys.controller;

import com.google.gson.reflect.TypeToken;
import com.sys.constant.Globals;
import com.sys.entity.MenuButtonEntity;
import com.sys.entity.SysMenuEntity;
import com.sys.service.SysMenuService;
import com.sys.util.*;
import org.apache.commons.lang.StringUtils;
import com.sys.service.ISystemService;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private ISystemService systemService;
    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "sys_menu")
    public String sys_menu(){
    return "sys/menu/sys_menu_list";
    }
    /**
    * 获取数据列表
    * @param
    * @return
    */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(SysMenuEntity.class, request, false);
        sysMenuService.fillDataGrid(SysMenuEntity.class,list,d);
        return d;
    }
    /**
     * 获取树列表
     * @param pid
     * @return
     */
    @RequestMapping(params = "gettree")
    @ResponseBody
    public List<SysMenuEntity> getTree(Long pid){
        if(pid==null) pid=0l;
        return sysMenuService.getTreeList(pid);
    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(SysMenuEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        if(bean.getId()==null||bean.getId()==0){
            bean.setId(System.currentTimeMillis());
            json.setSuccess(sysMenuService.save(bean));
        }else{
            SysMenuEntity sysMenuEntity=sysMenuService.getEntity(SysMenuEntity.class,bean.getId());
            if(sysMenuEntity!=null) sysMenuEntity=new SysMenuEntity();
            BeanUtil.copyNotNull2Bean(bean,sysMenuEntity);
            json.setSuccess(sysMenuService.update(sysMenuEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(SysMenuEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public SysMenuEntity get(Long id){
        Assert.notNull(id);
        return  sysMenuService.getEntity(SysMenuEntity.class,id);
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
        SysMenuEntity d=new SysMenuEntity();
        d.setId(id);
        j.setSuccess(sysMenuService.delete(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(SysMenuEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                if(!sysMenuService.delete(new SysMenuEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(SysMenuEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }
    /**
     * 批量删除按钮
     * @param ids
     * @return
     */
    @RequestMapping(params = "delbuttons")
    @ResponseBody
    public SuccessMsg delButtons(String[] ids){
        SuccessMsg j=new SuccessMsg();
        if (ids != null) {
            j.setSuccess(true);
            j.setMsg("成功删除"+ids.length+"条记录");
            for(int i=0;i<ids.length;i++){
                if(!sysMenuService.delete(new MenuButtonEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(SysMenuEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;
    }

    /**
     * 获取按钮列表
     * @param menuId
     * @return
     */
    @RequestMapping(params = "getbuttons")
    @ResponseBody
    public List<MenuButtonEntity> getButtons(Long menuId){
       Assert.notNull(menuId);
        List<MenuButtonEntity>  list=  sysMenuService.getMenuButtonList(menuId);
        return list;
    }

    /**
     * 保存按钮权限
     * @param jsonData
     * @return
     */
    @RequestMapping(params = "savebuttons")
    @ResponseBody
    public SuccessMsg saveButtons(String jsonData){
        SuccessMsg j=new SuccessMsg(true,"成功保存按钮权限!");
         List<MenuButtonEntity> buttonEntities= JsonUtil.getGson().fromJson(jsonData,new TypeToken<List<MenuButtonEntity>>(){}.getType());
        String logType=Globals.LOG_UPDATE;
        if(buttonEntities!=null){
             for(MenuButtonEntity entity:buttonEntities){
                 if(StringUtils.isBlank(entity.getId())){
                     entity.setId(null);
                     logType=Globals.LOG_INSERT;
                 }
                 j.setSuccess(j.isSuccess()&&sysMenuService.saveorupdate(entity));
                 systemService.addLog(SysMenuEntity.class.getSimpleName()+j.getMsg(),logType);
             }
         }
        if(!j.isSuccess()){
            j.setMsg("保存失败！");
        }
        return j;
    }


}
