package com.sys.controller;

import com.code.entity.BaseTreeEntity;
import com.code.service.ITableInfService;
import com.sys.constant.Globals;
import com.sys.entity.ComboBox;
import com.sys.entity.ComboTree;
import com.sys.entity.DicBean;
import com.sys.service.DicService;
import com.sys.service.ISystemService;
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
import java.io.Serializable;
import java.util.List;

/**
 * 字典管理
 * @author zzl
 * Date:2014-07-28
 */
@Controller
@RequestMapping("/sys/dic")
public class DicController {
    @Resource
    private DicService dicService;
    @Resource
    private ISystemService systemService;
    @Resource
    private ITableInfService tableInfService;
    /**
     * 转入页面
     * @return
     */
    @RequestMapping(params = "dic")
    public String dic(){
        return "sys/dic/dic_list";
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
        List<Criterion> list= BeanUtil.generateCriterions(DicBean.class, request, false);
        dicService.fillDataGrid(DicBean.class,list,d);
        return d;
    }
    /**
     * 保存或更新
     * @param bean
     * @return
     */
    @RequestMapping(params = "save")
    @ResponseBody
    public SuccessMsg save(DicBean bean){
        SuccessMsg json=new SuccessMsg();
        String logType=Globals.LOG_INSERT;
        if(bean.getId()!=null)
            logType=Globals.LOG_UPDATE;
        json.setSuccess(dicService.mergeData(bean,bean.getId())!=null);
        if(json.isSuccess()) json.setMsg("保存成功!");
        else  json.setMsg("保存失败！");
        systemService.addLog(DicBean.class.getSimpleName()+ json.getMsg(), logType);
        return json;
    }

    /**
     * 获取实体bean 返回json
     * @param id
     * @return
     */
    @RequestMapping(params = "get")
    @ResponseBody
    public DicBean get(String id){
        Assert.notNull(id);
        return  dicService.getEntity(DicBean.class,id);
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
        request.setAttribute("bean",dicService.getEntity(DicBean.class,id));
        return "sys/dic/dic_detail";
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
        DicBean d=new DicBean();
        d.setId(id);
        j.setSuccess(dicService.delete(d));
        if(j.isSuccess())
            j.setMsg("删除成功!");
        else
            j.setMsg("删除失败！");
        systemService.addLog(DicBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
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
                if(!dicService.delete(new DicBean(ids[i]))){
                        j.setSuccess(false);
                        j.setMsg("删除失败!");
                }
                systemService.addLog(DicBean.class.getSimpleName()+j.getMsg(),Globals.LOG_DEL);
            }
        }
        return j;

    }

    /**
     * 获取字典选项
     * @param dicKey
     * @param dicValue
     * @param dicTable
     * @return
     */
    @RequestMapping(params = "dicList")
    @ResponseBody
    public List<ComboBox> getDicCombobox(String dicKey,String dicValue,String dicTable){
       List list= dicService.getDicComboBoxes(dicKey,dicValue,dicTable);
       return list;
    }
    /**
     * 获取字典树选项
     * @param dicKey
     * @param dicValue
     * @param dicTable
     * @return
     */
    @RequestMapping(params = "dicTreeList")
    @ResponseBody
    public List<ComboTree> getDicComboTree(String dicKey,String dicValue,String dicTable){
        return dicService.getDicComboTree(dicKey, dicValue, dicTable);
    }

    /**
     * 获取字典值
     * @param dicKey
     * @param dicValue
     * @param dicTable
     * @param dicKeyValue
     * @return
     */
    @RequestMapping(params = "getDicValue")
    @ResponseBody
    public String getDicValue(String dicKey,String dicValue,String dicTable,String dicKeyValue){
       return dicService.getDicVal(dicKey,dicValue,dicTable,dicKeyValue);
    }

    /**
     * 通过id集合查询数据
     * @param tableName
     * @param ids
     * @param includeSubTable
     * @return
     */
    @RequestMapping(params = "getDataListByIDs")
    @ResponseBody
    public List<Object> getDataListByIDs(String tableName,String[] ids,boolean includeSubTable){
        return tableInfService.getDataListByIDs(tableName,ids,includeSubTable);
    }
}
