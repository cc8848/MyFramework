package com.sys.service.impl;

import com.code.entity.BaseTreeEntity;
import com.code.entity.TableHeadBean;
import com.code.util.TreeUtil;
import com.sys.annotation.Ehcache;
import com.sys.dao.IBaseCommonDao;
import com.sys.dao.impl.BaseCommonDao;
import com.sys.entity.ComboBox;
import com.sys.entity.ComboTree;
import com.sys.service.DicService;
import com.sys.util.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zzl
 * Date:2014-07-28
 */
@Service("dicService")
@Transactional
public class DIcServiceImpl extends CommonService implements DicService {
    @Ehcache
    @Override
    public List<ComboBox> getDicComboBoxes(String dicKey, String dicValue, String dicTable) {
        if(StringUtils.isBlank(dicKey))
            return new ArrayList<>();
        String[] strs=dicKey.split(":");
        String conditions="";
        if(strs.length==2)
            conditions="and "+strs[1];
        List<ComboBox> comboBoxList=new ArrayList<>();
        if(StringUtils.isBlank(dicValue)) dicValue="dic_value";
        if(StringUtils.isBlank(dicTable)) dicTable="sys_dic";
        List<Object[]> Os= findBySql("select distinct(" + strs[0] + ") as dicKey," + dicValue + " as dicValue from " + dicTable+" where 1=1 "+conditions, new Object[0]);
        if(Os!=null){
            for (Object[] o:Os){
                comboBoxList.add(new ComboBox(o[0],o[1]));
            }
        }
        return comboBoxList;
    }
    @Override
    public String getDicVal(String dicKey, String dicValue, String dicTable,String dicKeyValue) {
        if(StringUtils.isBlank(dicKey))
            return dicValue;
        String[] strs=dicKey.split(":");
        String conditions="";
        if(strs.length==2){
            String[] con=strs[1].split(",");
            if(!ArrayUtils.isEmpty(con)){
                for(String str:con){
                    if(!str.startsWith(strs[0]+"="))
                        conditions="  and "+str;
                }
            }
        }

        if(StringUtils.isBlank(dicValue)) dicValue="dic_value";
        if(StringUtils.isBlank(dicTable)) dicTable="sys_dic";
        List<Object> list= findBySql("select distinct("+dicValue+") as dicValue from "+dicTable+" where "+strs[0]+"=? "+conditions,new Object[]{dicKeyValue});
        if(list!=null&&list.size()>0)
            return list.get(0)+"";
        else
            return dicKeyValue;
    }
    @Ehcache
    @Override
    public Object getDicCode(String dicKey, String dicValue, String dicTable, String dicKeyValue) {
        if(StringUtils.isBlank(dicKey))
            return dicValue;
        String[] strs=dicKey.split(":");
        String conditions="";
        if(strs.length==2)
            conditions="and "+strs[1];
        if(StringUtils.isBlank(dicValue)) dicValue="dic_value";
        if(StringUtils.isBlank(dicTable)) dicTable="sys_dic";
        List<Object> list= findBySql("select distinct("+strs[0]+") as dicKey from "+dicTable+" where "+dicKey+"=? "+conditions,new Object[]{dicKeyValue});
        if(list!=null&&list.size()>0)
            return list.get(0)+"";
        else
            return "";
    }

    @Ehcache(addOrdel = false)
    @Override
    public boolean save(Object entity) {
        return super.save(entity);
    }
    @Ehcache(addOrdel = false)
    @Override
    public boolean update(Object entity) {
        return super.update(entity);
    }
    @Ehcache(addOrdel = false)
    @Override
    public Serializable mergeData(Object entity, Serializable id) {
        return super.mergeData(entity, id);
    }
    @Ehcache(addOrdel = false)
    @Override
    public boolean delete(Object clazz) {
        return super.delete(clazz);
    }
    @Ehcache
    @Override
    public String getClassName(String tableName) {
        List<TableHeadBean> list=getDataListByHql("from TableHeadBean where tableName=?",new Object[]{tableName});
        if(list!=null&list.size()>0){
            TableHeadBean headBean=list.get(0);
            if(StringUtils.isNotBlank(headBean.getBasePackageName()))
                return headBean.getBasePackageName()+".entity."+ StringUtil.toEntityName(tableName)+"Entity";
        }
        return null;
    }
    @Ehcache
    @Override
    public List<ComboTree> getDicComboTree(String dicKey, String dicValue, String dicTable) {
        if(StringUtils.isBlank(dicKey)||StringUtils.isBlank(dicValue)||StringUtils.isBlank(dicTable))
            return new ArrayList<>();
        String[] strs=dicKey.split(":");
        String conditions="";
        if(strs.length==2)
            conditions="and "+strs[1];
        List<ComboTree> comboBoxList=new ArrayList<>();
        String className=getTableClassMap().get(dicTable);
        if(StringUtils.isBlank(className))
            return new ArrayList<>();
        String hql=" from "+className;
        List<? extends BaseTreeEntity> objects=getDataListByHql(hql, new Object[0]);
        objects=TreeUtil.combineTree(objects,null);
        for(int i=0;i<objects.size();i++){
            if(strs.length!=2||(strs.length==2&&filterCon(strs[1],objects.get(i)))){
              comboBoxList.add(toComboTree(strs[0],dicValue,objects.get(i),null));
            }
        }
        objects=null;
        return comboBoxList;
    }
    //过滤条件
    private boolean filterCon(String condition,Object comboTree){
        String [] conditions=condition.split(",");
        for(String con:conditions){
            String[] con2=con.split("=");
            if(con2.length!=2)
                continue;
            if(con2[1]!=null)
                con2[1]=con2[1].replace("'","");
            try{
                if(!Objects.equals(con2[1],PropertyUtils.getProperty(comboTree,StringUtil.toFieldName(con2[0]))+""))
                    return false;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return true;
    }
    private ComboTree toComboTree(String key,String value,BaseTreeEntity treeEntity,String pid){
        try{
            treeEntity.setId(PropertyUtils.getProperty(treeEntity,StringUtil.toFieldName(key))+"");
            treeEntity.setText(PropertyUtils.getProperty(treeEntity, StringUtil.toFieldName(value)) + "");
            if(pid!=null)
                treeEntity.setpId(pid);
            if(CollectionUtils.isNotEmpty(treeEntity.getChildren())){
                for(BaseTreeEntity baseTreeEntity:treeEntity.getChildren()){
                    toComboTree(key,value,baseTreeEntity,treeEntity.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ComboTree tree=new ComboTree();
        tree.setId(treeEntity.getId());
        tree.setText(treeEntity.getText());
        tree.setChildren(treeEntity.getChildren());
       return tree;
    }

}
