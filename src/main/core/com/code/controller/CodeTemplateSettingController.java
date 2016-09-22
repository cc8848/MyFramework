package com.code.controller;

import com.sys.constant.Globals;
import com.code.entity.CodeTemplateSettingEntity;
import com.code.service.CodeTemplateSettingService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.BeanUtils;
import com.sys.service.ISystemService;
import com.sys.util.*;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/sys/code/setting")
public class CodeTemplateSettingController {
    @Resource
    private CodeTemplateSettingService codeTemplateSettingService;
    @Resource
    private ISystemService systemService;
    /**
    * 转入页面
    * @return
    */
    @RequestMapping(params = "code_template_setting")
    public String code_template_setting(HttpServletRequest request){
        request.setAttribute("_modulesLink",ContextHolderUtil.getRequestUrl());
        request.setAttribute("_conditions", ResourceUtil.getParamMap(request));
        return "sys/code/code_template_setting_list";
    }
    /**
    * 获取数据列表
    * @param
    * @return
    */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(CodeTemplateSettingEntity.class, request, false);
        codeTemplateSettingService.fillDataGrid(CodeTemplateSettingEntity.class,list,d);
        return d;
    }
    /**
    * 保存或更新
    * @param bean
    * @return
    */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(CodeTemplateSettingEntity bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        Map<String,Object> dataMap=new HashMap<>();
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            Serializable id=codeTemplateSettingService.saveReturnId(bean);
                json.setSuccess(id!=null);
            try {
                BeanUtils.setProperty(bean,"id",id);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            CodeTemplateSettingEntity codeTemplateSettingEntity=codeTemplateSettingService.getEntity(CodeTemplateSettingEntity.class,bean.getId());
            if(codeTemplateSettingEntity!=null) codeTemplateSettingEntity=new CodeTemplateSettingEntity();
            BeanUtil.copyNotNull2Bean(bean,codeTemplateSettingEntity);
            json.setSuccess(codeTemplateSettingService.update(codeTemplateSettingEntity));
            logType=Globals.LOG_UPDATE;
        }
        dataMap.put("bean",bean);
        json.setDataMap(dataMap);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(CodeTemplateSettingEntity.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
    * 获取实体bean 返回json
    * @param id
    * @return
    */
    @RequestMapping(params = "get")
    @ResponseBody
    public CodeTemplateSettingEntity get(String id){
        Assert.notNull(id);
        return  codeTemplateSettingService.getEntity(CodeTemplateSettingEntity.class,id);
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
        request.setAttribute("bean",codeTemplateSettingService.getEntity(CodeTemplateSettingEntity.class,id));
        return "sys/code/code_template_setting_detail";
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
        CodeTemplateSettingEntity d=new CodeTemplateSettingEntity();
        d.setId(id);
        j.setSuccess(codeTemplateSettingService.delUseFlag(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(CodeTemplateSettingEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                if(!codeTemplateSettingService.delUseFlag(new CodeTemplateSettingEntity(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(CodeTemplateSettingEntity.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }
}
