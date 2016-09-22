package com.basetemplate.controller;

import com.basetemplate.entity.TemplateFieldBean;
import com.basetemplate.entity.TemplateHeadBean;
import com.basetemplate.service.TemplateService;
import com.sys.constant.Globals;
import com.sys.service.ISystemService;
import com.sys.util.SuccessMsg;
import com.sys.util.BeanUtil;
import com.sys.util.DataGrid;
import com.sys.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zzl
 * Date:2014-08-01
 */
@Controller
@RequestMapping("/sys/template")
public class TemplateController {
    @Resource
    private TemplateService templateService;
    @Resource
    private ISystemService systemService;

    /**
     * 转入模板信息页面
     * @return
     */
    @RequestMapping(params = "template")
    public String template(){
        return "sys/base_template/template_list";
    }

    /**
     * 获取数据列表
     * @param
     * @return
     */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(DataGrid d,HttpServletRequest request){
        List<Criterion> list= BeanUtil.generateCriterions(TemplateHeadBean.class, request, false);
        templateService.fillDataGrid(TemplateHeadBean.class,list,d);
        return d;
    }
    /**
     * 获取子集数据列表
     * @param
     * @return
     */
    @RequestMapping(params = "subdatagrid")
    @ResponseBody
    public List<TemplateFieldBean> subDatagrid(DataGrid d,HttpServletRequest request){
        d.setOrders("fieldOrder");
        List<Criterion> list= BeanUtil.generateCriterions(TemplateFieldBean.class, request, false);
        List<Order> orders=new ArrayList<>();
        orders.add(Order.asc("fieldOrder"));
        return templateService.getDataList(TemplateFieldBean.class, list, orders);
    }
    /**
     * 保存或更新
     * @param bean
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(TemplateHeadBean bean){
        SuccessMsg json=new SuccessMsg();
        String logType= Globals.LOG_UPDATE;
        if(StringUtils.isBlank(bean.getId())){
             bean.setId(null);
            logType=Globals.LOG_INSERT;
        }
        json.setSuccess(templateService.mergeOneToMany(bean,"head",bean.getFields())!=null);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(TemplateHeadBean.class.getSimpleName()+ json.getMsg(), logType);
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
        TemplateHeadBean bean= (TemplateHeadBean) JsonUtil.fromJson(jsonData,TemplateHeadBean.class);
        SuccessMsg json=new SuccessMsg();
        String logType= Globals.LOG_UPDATE;
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            logType=Globals.LOG_INSERT;
        }
        json.setSuccess(templateService.mergeOneToMany(bean,"head",bean.getFields())!=null);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(TemplateHeadBean.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }
    /**
     * 获取实体bean 返回json
     * @param id
     * @return
     */
    @RequestMapping(params = "get")
    @ResponseBody
    public TemplateHeadBean get(String id){
        Assert.notNull(id);
        return  templateService.getEntity(TemplateHeadBean.class,id);
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
        request.setAttribute("bean",templateService.getEntity(TemplateHeadBean.class,id));
        return "sys/base_template/template_detail";
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
        TemplateHeadBean d=new TemplateHeadBean();
        d.setId(id);
        templateService.updateByHql("delete from TemplateFieldBean bean  where bean.head.id=? ",new Object[]{id});
        j.setSuccess(templateService.cascadeDel(TemplateHeadBean.class,id));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(TemplateHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                templateService.updateByHql("delete from TemplateFieldBean bean  where bean.head.id=? ",new Object[]{ids[i]});
                if(!templateService.cascadeDel(TemplateHeadBean.class,ids[i])){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(TemplateHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

    /**
     * 删除表字段信息
     * @param ids
     * @return
     */
    @RequestMapping(params = "delFields")
    @ResponseBody
    public SuccessMsg delFields(String[] ids){
        SuccessMsg j=new SuccessMsg();
        if (ids != null) {
            j.setSuccess(true);
            j.setMsg("成功删除"+ids.length+"条记录");
            for(int i=0;i<ids.length;i++){
                if(!templateService.delete(new TemplateFieldBean(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(TemplateHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;
    }
    /**
     * 获取模板名称列表
     * @param
     * @return
     */
    @RequestMapping(params = "gettemplatename")
    @ResponseBody
    public List<TemplateHeadBean> getTemplateName(){
        List<TemplateHeadBean> headBeanList=templateService.getDataList(TemplateHeadBean.class,new ArrayList<Criterion>(),null);
        return headBeanList;
    }
    /**
     * 获取模板字段列表
     * @param
     * @return
     */
    @RequestMapping(params = "gettemplatefields")
    @ResponseBody
    public List<TemplateFieldBean> getTemplateFields(String id){
        List<Criterion> criterions=new ArrayList<>();
        criterions.add(Restrictions.eq("head.id",id));
        List<TemplateFieldBean> fieldBeans=templateService.getDataList(TemplateFieldBean.class,criterions,null);
        return fieldBeans;
    }

}
