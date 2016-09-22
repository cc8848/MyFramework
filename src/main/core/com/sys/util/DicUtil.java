package com.sys.util;

import com.code.easyui.ComboboxOptionBean;
import com.code.easyui.ComboboxOptionUtil;
import com.code.entity.TableFieldBean;
import com.code.entity.TableHeadBean;
import com.sys.annotation.Ehcache;
import com.sys.service.DicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 字典工具类
 * @author zzl
 * Date:2014-09-18
 */
@Component
public class DicUtil {
    @Resource
    private DicService dicService;
    private static DicUtil dicUtil;
    @PostConstruct
    public void init(){
        dicUtil=this;
        dicUtil.dicService=this.dicService;
    }
    /**
     * 获取字典值
     * @param fieldBean
     * @param code
     * @param accuracy 如果true 未找到匹配返回null或“” 若为false 未找到匹配返回code
     * @return
     */
    public static Object getDicValue(TableFieldBean fieldBean,Object code,boolean accuracy){
        if(StringUtils.isBlank(fieldBean.getDicKey())){
            return getDefaultDicVal(fieldBean.getFieldDefault(),code,"checkbox".equals(fieldBean.getShowType()), accuracy);
        }else{
            return dicUtil.dicService.getDicVal(fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),code+"");
        }
    }

    /**
     * 返回字典值
     * @param dicKey
     * @param dicVal
     * @param dicTable
     * @param code
     * @return
     */
    public static Object getDictVal(String dicKey,String dicVal,String dicTable,Object code){
            return dicUtil.dicService.getDicVal(dicKey,dicVal,dicTable,code+"");
    }
    /**
     * 获取字典编码
     * @param fieldBean
     * @param value
     * @return
     */
    public static Object getDicCode(TableFieldBean fieldBean,String value){
        if(StringUtils.isBlank(fieldBean.getDicKey())){
            return getDefaultDicCode(fieldBean.getFieldDefault(),value+"");
        }else{
            return dicUtil.dicService.getDicCode(fieldBean.getDicKey(),fieldBean.getDicValue(),fieldBean.getDicTable(),value+"");
        }
    }
    /**
     * 获取固定的字典值
     * @param fieldDefault
     * @param code
     * @param accuracy 如果true 未找到匹配返回null或“” 若为false 未找到匹配返回code
     * @return
     */

   private static Object getDefaultDicVal(String fieldDefault,Object code,boolean isCheckbox,boolean accuracy) {
       if(code==null)
           return null;
       List<ComboboxOptionBean> comboboxOptionBeans = ComboboxOptionUtil.fromString(fieldDefault);
            if (comboboxOptionBeans != null&&comboboxOptionBeans.size()>0) {
                for (ComboboxOptionBean bean : comboboxOptionBeans) {
                    if (code.toString().equals(bean.getValue()))
                         return bean.getText();
                }
                if(isCheckbox){
                    return comboboxOptionBeans.get(comboboxOptionBeans.size()-1).getText();
                }
            }
       if(accuracy)
           return null;
       return code;

   }

    /**
     * 获取固定的字典code
     * @param fieldDefault
     * @param val
     * @return
     */
   private static Object getDefaultDicCode(String fieldDefault,Object val){
       if(val==null)
           return null;
       List<ComboboxOptionBean> comboboxOptionBeans = ComboboxOptionUtil.fromString(fieldDefault);
       if (comboboxOptionBeans != null) {
           for (ComboboxOptionBean bean : comboboxOptionBeans) {
               if (val.toString().equals(bean.getText()))
                   return bean.getValue();
           }
       }
       return val;
   }

    /**
     * 根据表名获取实体名称
     * @param tableName
     * @return
     */
   public static String getEntityClassName(String tableName){
       String className=dicUtil.dicService.getTableClassMap().get(tableName);
       return className;
   }

    /**
     * 根据表名获取实体类
     * @param tableName
     * @return
     */
   public static Class getEntityClass(String tableName){
       String className=getEntityClassName(tableName);
       return StringUtil.getClassByClassName(className);
   }
    /**
     * 获取视图文件夹相对于webpage的路径
     * @param tableName
     * @return
     */
   public static String getViewFolder(String tableName){
       List<TableHeadBean> list=dicUtil.dicService.getDataListByHql("from TableHeadBean where tableName=?",new Object[]{tableName});
       if(list!=null&list.size()>0){
           TableHeadBean headBean=list.get(0);
           String folder= headBean.getViewFolder();
           if(folder!=null&&!folder.endsWith("/")){
               folder+="/";
           }
           return folder;
       }
       return null;
   }
}
