package com.code.controller;

import com.code.core.CodeType;
import com.code.core.impl.FirstGenerateDirector;
import com.code.core.impl.OneToManyCodeWoker;
import com.code.core.impl.SimpleCodeWorker;
import com.code.easyui.EasyuiViewUtil;
import com.code.entity.CodeConfigBean;
import com.code.entity.CodeTemplateSettingEntity;
import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.code.service.CodeTemplateSettingService;
import com.code.service.ITableInfService;
import com.code.util.ClassUtil;
import com.sys.constant.Globals;
import com.sys.entity.ComboBox;
import com.sys.entity.FileTreeBean;
import com.sys.service.ISystemService;
import com.sys.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zzl
 * Date:2014-08-01
 */
@Controller
@RequestMapping("/sys/code/tableinf")
public class TableInfController {
    @Resource
    private ITableInfService tableInfService;
    @Resource
    private CodeTemplateSettingService codeTemplateSettingService;
    @Resource
    private ISystemService systemService;

    /**
     * 转入表信息页面
     * @return
     */
    @RequestMapping(params = "tableinf")
    public String tableinf(HttpServletRequest request){
        return "sys/code/tableinf_list";
    }
    @RequestMapping(params = "templateList")
    @ResponseBody
    public List<FileTreeBean> getTemplateList(){
        String rootPath=this.getClass().getClassLoader().getResource("/").getPath().replace("\\","/");
        return  FileUtil.listFileTree(rootPath+"code_templates","vm",true,true);
    }
    /**
     * 获取数据列表
     * @param pageNum
     * @return
     */
    @RequestMapping(params = "datagrid")
    @ResponseBody
    public DataGrid datagrid(Integer pageNum,HttpServletRequest request){
        DataGrid d=new DataGrid();
        if(pageNum==null||pageNum<1)  pageNum=1;
        d.setPageNum(pageNum);
        List<Criterion> list= BeanUtil.generateCriterions(TableHeadBean.class, request, false);
        tableInfService.fillDataGrid(TableHeadBean.class,list,d);
        return d;
    }
    /**
     * 获取子集数据列表
     * @param pageNum
     * @return
     */
    @RequestMapping(params = "subdatagrid")
    @ResponseBody
    public List<Object> subDatagrid(Integer pageNum,HttpServletRequest request){
        DataGrid d=new DataGrid();
        if(pageNum==null||pageNum<1)  pageNum=1;
        List<Criterion> list= BeanUtil.generateCriterions(TableFieldBean.class, request, false);
        List<Order> orders=new ArrayList<>();
        orders.add(Order.asc("fieldOrder"));
        return tableInfService.getDataList(TableFieldBean.class,list,orders);
    }
    @RequestMapping(params = "viewerKeys")
    @ResponseBody
    public List<ComboBox> getViewerKeys(){
        List<String> keys=new EasyuiViewUtil().getViewerKeys();
        List<ComboBox> comboBoxes=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(keys)){
            for(String str:keys){
                comboBoxes.add(new ComboBox(str,str));
            }
        }
        return comboBoxes;
    }
    /**
     * 保存或更新
     * @param bean
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(TableHeadBean bean){
        SuccessMsg json=new SuccessMsg();
        String logType= Globals.LOG_UPDATE;
        if(StringUtils.isBlank(bean.getId())){
             bean.setId(null);
            logType=Globals.LOG_INSERT;
        }
        json.setSuccess(tableInfService.mergeOneToMany(bean,"head",bean.getFields())!=null);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(TableHeadBean.class.getSimpleName()+ json.getMsg(), logType);
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
        TableHeadBean bean= (TableHeadBean) JsonUtil.fromJson(jsonData,TableHeadBean.class);
       // TableHeadBean bean= (TableHeadBean) JacksonDIctUtil.json2Object(jsonData,TableHeadBean.class);
        SuccessMsg json=new SuccessMsg();
        String logType= Globals.LOG_UPDATE;
        if(StringUtils.isBlank(bean.getId())){
            bean.setId(null);
            logType=Globals.LOG_INSERT;
        }
        json.setSuccess(tableInfService.mergeOneToMany(bean,"head",bean.getFields())!=null);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(TableHeadBean.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }
    /**
     * 获取实体bean 返回json
     * @param id
     * @return
     */
    @RequestMapping(params = "get")
    @ResponseBody
    public TableHeadBean get(String id){
        Assert.notNull(id);
        return  tableInfService.getEntity(TableHeadBean.class,id);
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
        request.setAttribute("bean",tableInfService.getEntity(TableHeadBean.class,id));
        return "sys/code/tableinf_detail";
    }

    /**
     * 获取TableContent
     * @param tableName
     * @return
     */
    @RequestMapping(params = "getTableContent")
    @ResponseBody
    public String getTableContent(String tableName){
        int a=0;
        return tableInfService.getTableContent(tableName);
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
        TableHeadBean d=new TableHeadBean();
        d.setId(id);
        d = this.tableInfService.getEntity(TableHeadBean.class,id);
        List<Criterion> list= new ArrayList<>();
        list.add(Restrictions.eq("head",d));
        List<TableFieldBean> beans = this.tableInfService.getDataList(TableFieldBean.class,list,null);
        for (TableFieldBean bean :beans){
            this.tableInfService.delete(bean);
        }
        j.setSuccess(tableInfService.cascadeDel(TableHeadBean.class,id));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(TableHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                TableHeadBean d= this.tableInfService.getEntity(TableHeadBean.class,ids[i]);
                List<Criterion> list= new ArrayList<>();
                list.add(Restrictions.eq("head",d));
                List<TableFieldBean> beans = this.tableInfService.getDataList(TableFieldBean.class,list,null);
                for (TableFieldBean bean :beans){
                    this.tableInfService.delete(bean);
                }
                if(!tableInfService.cascadeDel(TableHeadBean.class,ids[i])){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(TableHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                if(!tableInfService.delete(new TableFieldBean(ids[i]))){
                    j.setSuccess(false);
                    j.setMsg("删除失败!");
                }
                systemService.addLog(TableHeadBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;
    }

    /**
     * 生成代码
     * @param config
     * @return
     */
    @RequestMapping(params = "genereatecode")
    @ResponseBody
    public SuccessMsg genereateCode(CodeConfigBean config){
        //视图文件夹
        config.setViewFolder(config.getViewFolder().toLowerCase());
        SuccessMsg j=new SuccessMsg(true,"代码生成成功！");
        if(!j.isSuccess())
            return j;
        List<Criterion> list=new ArrayList<>();
        list.add(Restrictions.eq("head.id",config.getHeadId()));
        List<Order> orders=new ArrayList<>();
        orders.add(Order.asc("fieldOrder"));
        //查询出表信息并用config配置
        TableHeadBean head=initTableHead(list,orders,config);
        Set<CodeType> codeTypeSet=new HashSet<>();
        if(config.getCodeType()!=null){
            for(String str:config.getCodeType()){
                codeTypeSet.add(CodeType.getInstance(str));
            }
        }
        if("single".equals(head.getTableViewType())){
            j.setSuccess(generateSingleCode(head, codeTypeSet, null));
        }else if("main".equals(head.getTableViewType())){
            List<Criterion> subCriterionList=new ArrayList<>();
            subCriterionList.add(Restrictions.eq("mainTable",head.getTableName()));
            List<TableFieldBean> fieldBeanList=tableInfService.getDataList(TableFieldBean.class,subCriterionList,orders);
            List<TableHeadBean> headBeanList=new ArrayList<>();
            for(TableFieldBean field:fieldBeanList){
                List<Criterion> subCriterionList2=new ArrayList<>();
                subCriterionList2.add(Restrictions.eq("head.id",field.getHead().getId()));
                headBeanList.add(initTableHead(subCriterionList2,orders,config));
            }
            if(headBeanList.size()==0){
                j.setSuccess(generateSingleCode(head,codeTypeSet,null));
            }else{
                head.setSubList(headBeanList);
                j.setSuccess(generateOneToManyCode(head,codeTypeSet,null));
            }

        }else if("tree".equals(head.getTableViewType())){
            j.setSuccess(generateSingleCode(head, codeTypeSet, ResourceUtil.getResource("code_templates/TreeResource.properties")));
        }
        if(j.isSuccess()){
            j.setMsg("生成代码成功！");
        }else
            j.setMsg("生成失败！");

        return j;
    }

    /**
     * 生成实体类class文件
     * @param ids
     * @return
     */
    @RequestMapping(params = "generateEntityClass")
    @ResponseBody
    public SuccessMsg generateEntityClass(String[] ids){
        SuccessMsg successMsg=new SuccessMsg(true,"实体生成成功,重启服务生效！");
        DetachedCriteria criteria=DetachedCriteria.forClass(TableHeadBean.class);
        if(!ArrayUtils.isEmpty(ids))
            criteria.add(Restrictions.in("id",ids));
        criteria.add(Restrictions.eq("dyEntity",1));
        List<TableHeadBean> list=tableInfService.findDataList(criteria);
        if(CollectionUtils.isNotEmpty(list)){
            for(TableHeadBean headBean:list){
                headBean.setFields(tableInfService.getTableHead(headBean.getTableName(),true).getFields());
            }
        }
        list=tableHeadFilter(list);
        List<String> classNameList= ClassUtil.generateEntityClass(list,ClassUtil.class.getClassLoader().getResource("").getPath(),"business.dynamic");
        ClassUtil.loadClass(classNameList);
        return successMsg;
    }

    /**
     * 将主子表重复的实体去掉
     * @param headBeans
     * @return
     */
    private List<TableHeadBean> tableHeadFilter(List<TableHeadBean> headBeans){
        Map<String,TableHeadBean> dataMap=new ConcurrentHashMap<>();
        for(TableHeadBean headBean:headBeans){
            dataMap.put(headBean.getTableName(),headBean);
        }
        for(String key:dataMap.keySet()){
            List<TableHeadBean> subHeadList=tableInfService.getSubTableHead(key,true);
            if(CollectionUtils.isNotEmpty(subHeadList)){
                dataMap.get(key).setSubList(subHeadList);
                for(TableHeadBean headBean:subHeadList){
                    if(dataMap.containsKey(headBean.getTableName()))
                        dataMap.remove(headBean.getTableName());
                }
            }

        }
        return new ArrayList<>(dataMap.values());
    }
    /**
     * 生成单表
     * @param headBean
     * @param codeTypes
     * @return
     */
     private boolean generateSingleCode(TableHeadBean headBean,Set<CodeType> codeTypes,Map<String,String> soureMap) {
            SimpleCodeWorker codeWorker = new SimpleCodeWorker();
            if(MapUtils.isNotEmpty(soureMap))
                codeWorker.setSourceMap(soureMap);
            codeWorker.setTableHeadBean(headBean);
            FirstGenerateDirector director = new FirstGenerateDirector(codeWorker);
            director.generateCode(codeTypes);
         return true;
     }

    /**
     * 查询出表信息并用config配置
      * @param list
     * @param orders
     * @return
     */
     private TableHeadBean initTableHead(List<Criterion> list,List<Order> orders,CodeConfigBean config) {
         List<TableFieldBean> fieldBeans=tableInfService.getDataList(TableFieldBean.class,list,orders);
         TableHeadBean head=null;
         if(fieldBeans!=null&&fieldBeans.size()>0) {
             head  = fieldBeans.get(0).getHead();
             head.setBasePackageName(config.getBasePackageName());
             head.setViewFolder(config.getViewFolder());
             tableInfService.update(head);
             Set<CodeType> codeTypeSet = new HashSet<>();
             if (config.getCodeType() != null) {
                 for (String str : config.getCodeType()) {
                     codeTypeSet.add(CodeType.getInstance(str));
                 }
             }
             head.setFields(fieldBeans);
         }
         return head;
     }

    /**
     * 一对多生成器
     * @param headBean
     * @param codeTypes
     * @return
     */
    private boolean generateOneToManyCode(TableHeadBean headBean,Set<CodeType> codeTypes,Map<String,String> soureMap){
        OneToManyCodeWoker codeWoker=new OneToManyCodeWoker();
        codeWoker.setTableHeadBean(headBean);
        FirstGenerateDirector director = new FirstGenerateDirector(codeWoker);
        director.generateCode(codeTypes);
        return true;
    }

    /**
     * 资源设置
     * @param sourceMap
     * @return
     */
    private SuccessMsg fillSourceMap(Map<String,String> sourceMap,String code_template_paths){
       CodeTemplateSettingEntity entity= codeTemplateSettingService.getSetting(null);
       SuccessMsg successMsg=new SuccessMsg(false,"参数设置不正确！");
       if(entity!=null&&StringUtils.isNotBlank(code_template_paths)){
           successMsg.setSuccess(true);
           successMsg.setMsg("成功！");
           sourceMap.put("basepath_java_code",entity.getJavaPath());
           sourceMap.put("basepath_view",entity.getViewPath());
           String[] paths=code_template_paths.split(",");
           String rootPath=this.getClass().getClassLoader().getResource("/").getPath().replace("\\","/").substring(1);
           for(String str:paths){
               File file=new File(str);
               if(file.isFile()&&file.getName().endsWith("vm"))
                    sourceMap.put(FileUtil.getSImplePrefixPath(str),file.getAbsolutePath().replace("\\","/").replace(rootPath,""));
           }
       }
       return successMsg;
    }


}
