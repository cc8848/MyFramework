package com.code.controller;

import com.code.entity.TableHeadBean;
import com.code.service.ITableInfService;
import com.sys.constant.Globals;
import com.sys.dynamic.DynaCompTest;
import com.sys.service.ISystemService;
import com.sys.util.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 在线代码控制器
 * 通过表名即可操作
 * Created by lenovo on 2015/1/21.
 */
@Controller
@RequestMapping("/onlineCode")
public class OnlineCodeController {
    @Resource
    private ITableInfService tableInfService;
    @Resource
    private ISystemService systemService;
    /**
     * 数据列表页面
     * @param tableName
     * @return
     */
    @RequestMapping("/{tableName}")
    public String showView(@PathVariable String tableName,HttpServletRequest request){
        Assert.hasLength(tableName,"请求表名为空！");
        request.setAttribute("tableName",tableName);
        request.setAttribute("_modulesLink", ContextHolderUtil.getRequestUrl());
        request.setAttribute("_conditions", ResourceUtil.getParamsMap(request));
        TableHeadBean tableHead=tableInfService.getTableHead(tableName,true);
        request.setAttribute("tableHead",tableHead);
        systemService.addLog("查看"+tableName+"列表页面", Globals.LOG_VIEW);
        return "sys/code/online/online_code_list";
    }
    /**
     * 获取数据列表
     * @param
     * @return
     */
    @RequestMapping(value ="/{tableName}" ,params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(@PathVariable String tableName,DataGrid d,HttpServletRequest request){
        Assert.hasLength(tableName,"请求表名为空！");
        if(StringUtils.isBlank(d.getOrders()))
            d.setOrders("_createDate:desc,_updateDate:desc");
        Class clazz=getEntityClass(tableName);
        List<Criterion> list= BeanUtil.generateCriterions(clazz, request, true);
        tableInfService.fillDataGrid(clazz,list,d);
        return d;
    }

    /**
     * 新增、更新页面
     * @param tableName
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value ="/{tableName}" ,params = "update")
    public String  updateView(@PathVariable String tableName,String id, HttpServletRequest request){
        Assert.hasLength(tableName,"请求表名为空！");
        request.setAttribute("tableName",tableName);
        if(StringUtils.isNotBlank(id))
            request.setAttribute("id",id);
        List<TableHeadBean> headBeans=tableInfService.getSubTableHead(tableName,true);
        request.setAttribute("subHeadList",headBeans);
        TableHeadBean tableHead=tableInfService.getTableHead(tableName,true);
        request.setAttribute("tableHead",tableHead);
        return "sys/code/online/online_code_update";
    }

    /**
     * 详情页面
     * @param tableName
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value ="/{tableName}" ,params = "detail")
    public String  detailView(@PathVariable String tableName,String id, HttpServletRequest request){
        Assert.hasLength(tableName,"请求表名为空！");
        Assert.hasLength(id,"请求id为空！");
        request.setAttribute("tableName",tableName);
        request.setAttribute("id",id);
        List<TableHeadBean> headBeans=tableInfService.getSubTableHead(tableName,true);
        request.setAttribute("subHeadList",headBeans);
        TableHeadBean tableHead=tableInfService.getTableHead(tableName,true);
        request.setAttribute("tableHead",tableHead);
        return "sys/code/online/online_code_detail";
    }
    /**
     * 保存数据
     * @param tableName
     * @param jsonData
     * @return
     */
    @RequestMapping(value ="/{tableName}",params = "save")
    @ResponseBody
    public SuccessMsg save(@PathVariable String tableName, String jsonData){
        Assert.hasLength(tableName,"请求表名为空！");
        Assert.hasLength(jsonData,"保存数据为空！");
        SuccessMsg successMsg=new SuccessMsg(true,"数据保存成功！");
        Class clazz=getEntityClass(tableName);
        Object entity=JsonUtil.fromJson(jsonData,clazz);
        Object id=null;
        String logType= Globals.LOG_UPDATE;
        try {
            id= PropertyUtils.getProperty(entity,"id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(id==null||id.toString().trim().length()==0){
            logType=Globals.LOG_INSERT;
        }
        List<TableHeadBean> subTableHeadList=tableInfService.getSubTableHead(tableName,false);
        if(id!=null&&id.toString().trim().length()>0){
            Object o2=tableInfService.getEntity(clazz, (java.io.Serializable) id);
            BeanUtil.copyNotNull2Bean(entity,o2);
            tableInfService.update(o2);
        }else{
            id=tableInfService.saveReturnId(entity);
        }

        if(CollectionUtils.isNotEmpty(subTableHeadList)){
            List subList=null;
            for(TableHeadBean subTableHead:subTableHeadList){
                subList=null;
                try {
                    subList=(List)PropertyUtils.getProperty(entity,StringUtil.toFieldName(subTableHead.getTableName())+"s");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                id=tableInfService.mergeOneToMany(entity,StringUtil.toFieldName(tableName)+"Entity",subList);
                subList=null;
                try {
                    PropertyUtils.setProperty(entity,"id",id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(id==null||id.toString().trim().length()==0){
            successMsg.setSuccess(false);
            successMsg.setMsg("数据保存失败！");
        }else{
            Map<String,Object> dataMap=new HashMap<>();
            dataMap.put("id",id);
            successMsg.setDataMap(dataMap);
        }

        systemService.addLog(tableName + successMsg.getMsg(), logType);
        return successMsg;
    }
    /**
     * 获取实体bean 返回json
     * @param id
     * @return
     */
    @RequestMapping(value ="/{tableName}",params = "get")
    @ResponseBody
    public Object get(@PathVariable String tableName,String id){
        Assert.hasLength(tableName,"请求表名为空！");
        Assert.hasLength(id, "请求id为空！");
        return  tableInfService.getEntity(getEntityClass(tableName),id);
    }

    /**
     * 删除信息
     * @param tableName
     * @param ids
     * @return
     */
    @RequestMapping(value ="/{tableName}",params = "del")
    @ResponseBody
    public SuccessMsg del(@PathVariable String tableName ,String[] ids){
        Assert.hasLength(tableName,"请求表名为空！");
        Assert.notEmpty(ids,"请求id为空！");
        SuccessMsg successMsg=new SuccessMsg(true,"删除成功！");
        TableHeadBean headBean= tableInfService.getTableHead(tableName,false);
        int count=0;
        if(Objects.equals(1,headBean.getIsIndex())){
          count= tableInfService.delUseFlag(tableInfService.getClassName(tableName),ids);
        }else{
            count=tableInfService.readDel(tableName,ids);
        }
        if(count<=0){
            successMsg.setMsg("删除失败！");
            successMsg.setSuccess(false);
        }else{
            successMsg.setMsg("成功删除"+count+"条数据!");
        }
        return successMsg;
    }
    /**
     * 获取子表数据
     * @param subTableName
     * @param pid
     * @return
     */
    @RequestMapping(value ="/{tableName}",params = "subList")
    @ResponseBody
    public List subList(@PathVariable String tableName,String subTableName,String pid){
        Assert.hasLength(tableName,"请求表名为空！");
        Assert.hasLength(subTableName,"请求表名为空！");
        Assert.hasLength(pid,"请求pid为空！");
        DetachedCriteria criteria=DetachedCriteria.forClass(getEntityClass(subTableName));
        criteria.add(Restrictions.eq(StringUtil.toFieldName(tableName)+"Entity.id",pid));
        List list=  tableInfService.findDataList(criteria);
        return list;
    }
    /**
     * 获取表对应的实体
     * @param tableName
     * @return
     */
    private Class getEntityClass(String tableName){
        String className=tableInfService.getTableClassMap().get(tableName);
        Assert.hasLength(className,"Entity获取失败！");
        Class clazz= StringUtil.getClassByClassName(className);
        Assert.notNull(clazz,"Entity获取失败！");
        return clazz;
    }
}
