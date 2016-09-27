package com.sys.controller;


import com.sys.constant.Globals;
import com.sys.entity.SysDepEntity;
import com.sys.entity.TreeBean;
import com.sys.service.ISystemService;
import com.sys.service.SysDepService;
import com.sys.util.SuccessMsg;
import com.sys.util.BeanUtil;
import com.sys.util.DataGrid;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/sys/dep")
public class SysDepController {
    @Resource
    private SysDepService sysDepService;
    @Resource
    private ISystemService systemService;
    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "sys_dep")
    public String sys_dep(){
    return "sys/dep/sys_dep_list";
    }
    /**
    * 获取数据列表
    * @param
    * @return
    */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(SysDepEntity.class, request, false);
        sysDepService.fillDataGrid(SysDepEntity.class,list,d);
        return d;
    }

    /**
     * 获取树列表
     * @param pid
     * @return
     */
    @RequestMapping(params = "gettree")
    @ResponseBody
    public List<TreeBean> getTree(Long pid){
        if(pid==null) pid=0l;
        return sysDepService.getTreeList(pid);
    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(SysDepEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        if(bean.getId()==null||bean.getId()==0){
            bean.setId(System.currentTimeMillis());
            json.setSuccess(sysDepService.save(bean));
        }else{
            SysDepEntity sysDepEntity=sysDepService.getEntity(SysDepEntity.class,bean.getId());
            if(sysDepEntity!=null) sysDepEntity=new SysDepEntity();
            BeanUtil.copyNotNull2Bean(bean,sysDepEntity);
            json.setSuccess(sysDepService.update(sysDepEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(SysDepEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public SysDepEntity get(Long id){
        Assert.notNull(id);
        return  sysDepService.getEntity(SysDepEntity.class,id);
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
        request.setAttribute("bean",sysDepService.getEntity(SysDepEntity.class,id));
        return "sys/dep/sys_dep_detail";
    }
    /**
    * 删除
    * @param id
    * @return
    */
    @RequestMapping(params = "del")
    @ResponseBody
    public SuccessMsg del(Long id){
        Assert.notNull(id);
        SuccessMsg j=new SuccessMsg();
        SysDepEntity d=new SysDepEntity();
        d.setId(id);
        j.setSuccess(sysDepService.delete(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(SysDepEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                if(!sysDepService.delete(new SysDepEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(SysDepEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

}
