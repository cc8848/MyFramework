package com.sys.controller;

import com.sys.constant.Globals;
import com.sys.entity.ComboBox;
import com.sys.entity.SessionUser;
import com.sys.entity.SysMenuEntity;
import com.sys.entity.SysUserEntity;
import com.sys.service.SysDepService;
import com.sys.service.SysUserService;
import com.sys.util.SuccessMsg;
import org.apache.commons.lang.StringUtils;
import com.sys.service.ISystemService;
import com.sys.util.BeanUtil;
import com.sys.util.DataGrid;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private ISystemService systemService;
    @Resource
    private SysDepService sysDepService;


    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "sys_user")
    public String sys_user(){
    return "sys/user/sys_user_list";
    }

    /**
     * 电子签章专用页面
     * @return
     */
    @RequestMapping(params = "sys_user_seal")
    public String sys_user_seal(HttpServletRequest request){
        HttpSession s=request.getSession();
        SessionUser sessionUser=(SessionUser)s.getAttribute("login_session_user");
        if(!"admin".equals(sessionUser.getUserName())){
            return "login";
        }
        return "sys/user_seal/sys_user_list";
    }
    /**
    * 获取数据列表
    * @param
    * @return
    */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(SysUserEntity.class, request, false);
        String depId=request.getParameter("depId");
        if(StringUtils.isNotBlank(depId)){
            List<Long> depIds=sysDepService.getChildrenId(new Long(depId));
            list.add(Restrictions.in("dep.id",depIds));
        }
        sysUserService.fillDataGrid(SysUserEntity.class,list,d);
        return d;
    }

    /**
     * 生成用户秘钥
     * @return
     */
    @RequestMapping(params = "generateUserKey")
    @ResponseBody
    public String generateUserKey(){
       return  UUID.randomUUID().toString();
    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(SysUserEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        Long depid=bean.getDep().getId();
        //确保save方法不能修改电子相关签章信息
        bean.setSeal_id(null);
        bean.setSeal_id(null);
        bean.setSeal_id(null);
        bean.setSeal_id(null);
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            json.setSuccess(sysUserService.save(bean));
        }else{
            SysUserEntity sysUserEntity=sysUserService.getEntity(SysUserEntity.class,bean.getId());
            if(sysUserEntity!=null) sysUserEntity=new SysUserEntity();
            BeanUtil.copyNotNull2Bean(bean,sysUserEntity);
            json.setSuccess(sysUserService.update(sysUserEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(SysUserEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
     * 电子签章 保存更新方法，admin专用
     * @param bean
     * @param request
     * @return
     */
    @RequestMapping(params = "save_seal")
    @ResponseBody
    public SuccessMsg save_seal(SysUserEntity bean,HttpServletRequest request){
        SuccessMsg json=new SuccessMsg();
        HttpSession s=request.getSession();
        SessionUser sessionUser=(SessionUser)s.getAttribute("login_session_user");
        if(!"admin".equals(sessionUser.getUserName())){
            json.setSuccess(false);
            json.setMsg("当权用户无权操作！");
            return json;
        }
        String logType=Globals.LOG_INSERT;
        Long depid=bean.getDep().getId();
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            json.setSuccess(sysUserService.save(bean));
        }else{
            SysUserEntity sysUserEntity=sysUserService.getEntity(SysUserEntity.class,bean.getId());
            if(sysUserEntity!=null) sysUserEntity=new SysUserEntity();
            BeanUtil.copyNotNull2Bean(bean,sysUserEntity);
            json.setSuccess(sysUserService.update(sysUserEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(SysUserEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public SysUserEntity get(String id){
        Assert.notNull(id);
        return  sysUserService.getEntity(SysUserEntity.class,id);
    }

    /**
    * 获取详情
    * @param id
    * @param request
    * @return
    */
    @RequestMapping(params = "detail")
    public String detail(String id,HttpServletRequest request){
        Assert.notNull(id);
        request.setAttribute("bean",sysUserService.getEntity(SysUserEntity.class,id));
        return "sys/user/sys_user_detail";
    }
    /**
    * 删除
    * @param id
    * @return
    */
    @RequestMapping(params = "del")
    @ResponseBody
    public SuccessMsg del(String id){
        SuccessMsg j=new SuccessMsg();
        SysUserEntity d=new SysUserEntity();
        d.setId(id);
        j.setSuccess(sysUserService.delete(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(SysUserEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
        return j;
    }

    /**
    * 批量删除
    * @param ids
    * @return
    */
    @RequestMapping(params = "muldel")
    @ResponseBody
    public SuccessMsg mulDel(String[] ids){
        SuccessMsg j=new SuccessMsg();
        if (ids != null) {
            j.setSuccess(true);
            j.setMsg("成功删除"+ids.length+"条记录");
            for(int i=0;i<ids.length;i++){
                if(!sysUserService.delete(new SysUserEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(SysUserEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

    /**
     * 获取角色授权的用户
     * @param roleId
     * @return
     */
    @RequestMapping(params = "role_user")
    @ResponseBody
    public List<SysUserEntity> getRoleUser(Long roleId){
        if(roleId==null)
            roleId=0l;
        return sysUserService.getRoleUsers(roleId);
    }

    /**
     * 获取所有用户
     * @return
     */
    @RequestMapping(params = "allusers")
    @ResponseBody
    public List<SysUserEntity> getAllUsers(){
        return sysUserService.getDataList(SysUserEntity.class,new ArrayList<Criterion>(),null);
    }

    /**
     * 获取所有用户--刷手
     * @return
     */
    @RequestMapping(params = "allusers2")
    @ResponseBody
    public List<ComboBox> getAllUsers2(){
        List<Criterion> list = new ArrayList<>();
        list.add(Restrictions.eq("userType",2));
        List<SysUserEntity> users= sysUserService.getDataList(SysUserEntity.class,list,null);
        List<ComboBox> boxs = new ArrayList<>();
        for(SysUserEntity user :users){
            ComboBox box = new ComboBox();
            box.setcKey(user.getId());
            box.setcValue(user.getRealname());
            boxs.add(box);
        }
        return boxs;
    }


}
