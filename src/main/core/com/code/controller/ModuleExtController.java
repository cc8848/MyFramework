package com.code.controller;

import com.code.entity.ModuleExtDetailEntity;
import com.code.entity.ModuleExtEntity;
import com.code.service.ModuleExtService;
import com.sys.constant.Globals;
import com.sys.entity.ComboBox;
import com.sys.service.ISystemService;
import com.sys.util.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/business/module_ext")
public class ModuleExtController {
    @Resource
    private ModuleExtService moduleExtService;
    @Resource
    private ISystemService systemService;
    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "module_ext")
    public String module_ext(HttpServletRequest request){
        request.setAttribute("_conditions", ResourceUtil.getParamMap(request));
        return "sys/code/module_ext_list";
    }
    /**
    * 获取数据列表
    * @param
    * @return
    */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){;
        List<Criterion> list= BeanUtil.generateCriterions(ModuleExtEntity.class, request, false);
        moduleExtService.fillDataGrid(ModuleExtEntity.class,list,d);
        return d;
    }
    /**
    * 获取扩展组件详情列表
    * @param
    * @return
    */
    @RequestMapping(params = "moduleExtDetails")
    @ResponseBody
    public List<ModuleExtDetailEntity> moduleExtDetails(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(ModuleExtDetailEntity.class, request, false);
        return moduleExtService.getDataList(ModuleExtDetailEntity.class, list, new ArrayList<Order>());
    }

    /**
    * 批量删除
    * @param ids
    * @return
    */
    @RequestMapping(params = "delmoduleExtDetails")
    @ResponseBody
    public SuccessMsg delmoduleExtDetails(String[] ids){
        SuccessMsg j=new SuccessMsg();
        if (ids != null) {
            j.setSuccess(true);
            j.setMsg("成功删除"+ids.length+"条记录");
            for(int i=0;i<ids.length;i++){
                if(!moduleExtService.cascadeDel(ModuleExtDetailEntity.class,ids[i])){
                j.setSuccess(false);
                j.setMsg("删除失败!");
                }
                systemService.addLog(ModuleExtDetailEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(ModuleExtEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            json.setSuccess(moduleExtService.save(bean));
        }else{
            ModuleExtEntity moduleExtEntity=moduleExtService.getEntity(ModuleExtEntity.class,bean.getId());
            if(moduleExtEntity!=null) moduleExtEntity=new ModuleExtEntity();
            BeanUtil.copyNotNull2Bean(bean,moduleExtEntity);
            json.setSuccess(moduleExtService.update(moduleExtEntity));
            logType=Globals.LOG_UPDATE;
        }
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(ModuleExtEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 通过传入json方式保存
    * @param jsonData
    * @return
    */
    @RequestMapping(params = "jsonSave")
    @ResponseBody
    public SuccessMsg jsonSave(String jsonData){
        ModuleExtEntity bean= (ModuleExtEntity) JsonUtil.fromJson(jsonData,ModuleExtEntity.class);
        SuccessMsg json=new SuccessMsg();
        String logType= Globals.LOG_UPDATE;
        Map<String,Object> dataMap=new HashMap<>();
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            logType=Globals.LOG_INSERT;
        }
        boolean isSuccess=true;
        Object id=null;
        id=moduleExtService.updateModules(bean);
        isSuccess=isSuccess&&(id!=null);
        dataMap.put("id",id);
        json.setDataMap(dataMap);
        json.setSuccess(isSuccess);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(ModuleExtEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }
    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public ModuleExtEntity get(String id){
        Assert.notNull(id);
        return  moduleExtService.getEntity(ModuleExtEntity.class,id);
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
        request.setAttribute("bean",moduleExtService.getEntity(ModuleExtEntity.class,id));
        return "sys/code/module_ext_detail";
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
        ModuleExtEntity d=new ModuleExtEntity();
        d.setId(id);
        j.setSuccess(moduleExtService.delModule(id));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(ModuleExtEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
            ModuleExtEntity d=new ModuleExtEntity();
            for(int i=0;i<ids.length;i++){
                d.setId(ids[i]);
                if(!moduleExtService.delModule(ids[i])){
                j.setSuccess(false);
                j.setMsg("删除失败!");
                }
                systemService.addLog(ModuleExtEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

    /**
     * 获取所有组件
     * @return
     */
    @RequestMapping(params = "getAllModuleList")
    @ResponseBody
    public List<ModuleExtEntity> getAllModuleList(){
        DetachedCriteria criteria=DetachedCriteria.forClass(ModuleExtEntity.class);
        List<ModuleExtEntity> moduleExtEntities=moduleExtService.findDataList(criteria);
        return  moduleExtEntities;
    }

    /**
     * 更新表组件规则
     * @param tableName
     * @param moduleIds
     * @return
     */
    @RequestMapping(params = "updateModuleRule")
    @ResponseBody
    public SuccessMsg updateModuleRule(String[] tableName,String[] moduleIds){
        Assert.notEmpty(tableName);
         SuccessMsg successMsg=new SuccessMsg(true,"更新成功！");
         for(String tn: tableName){
             successMsg.setSuccess(successMsg.isSuccess()&&moduleExtService.updateModules(tn,moduleIds));
         }

         if(!successMsg.isSuccess()){
             successMsg.setMsg("更新失败！");
         }
        return successMsg;
    }
    @RequestMapping(params = "getAllAndCheckedList")
    public String getAllAndCheckedList(String[] tableName,HttpServletRequest request){
        Assert.notEmpty(tableName);
        List<ModuleExtEntity> list=moduleExtService.getAllAndCheckedList(tableName[0]);
        request.setAttribute("dataList",list);
        request.setAttribute("tableNames",tableName);
        return "sys/code/module_rule";
    }
    @RequestMapping(params = "getModulesFiles")
    @ResponseBody
    public List<ComboBox> getModulesFiles(HttpServletRequest request){
        String basePath=request.getSession().getServletContext().getRealPath("/modules");
        File dir=new File(basePath);
        List<ComboBox> fileList=new ArrayList<>();
        if(dir.exists()&&dir.isDirectory()){
            String[] files=dir.list();
            if(!ArrayUtils.isEmpty(files)){
                for(String str :files){
                    fileList.add(new ComboBox(str,str));
                }
            }
        }
        return fileList;
    }
}
