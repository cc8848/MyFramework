package com.code.service.impl;

import com.code.entity.ModuleExtDetailEntity;
import com.code.entity.ModuleExtEntity;
import com.code.entity.ModuleExtRuleEntity;
import com.code.service.ModuleExtService;
import com.sys.annotation.Ehcache;
import com.sys.service.impl.CommonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("moduleExtService")
@Transactional
public class ModuleExtServiceImpl extends CommonService implements ModuleExtService {
    @Ehcache
    @Override
    public List<ModuleExtEntity> getModuleExtList(String tableName, boolean includeDetail) {
        String mainSql="select entity from ModuleExtEntity entity,ModuleExtRuleEntity rule ";
        mainSql+=" where entity.id=rule.moduleId and rule.moduleTable=?";
        List<ModuleExtEntity> moduleExtEntityList=getDataListByHql(mainSql,new Object[]{tableName});
        if(CollectionUtils.isNotEmpty(moduleExtEntityList)&&includeDetail){
            for(ModuleExtEntity entity:moduleExtEntityList){
                List<ModuleExtDetailEntity> detailList=getDataListByHql(" from ModuleExtDetailEntity where moduleExtEntity.id=?",new Object[]{entity.getId()});
                entity.setModuleExtDetails(detailList);
            }
        }
        return moduleExtEntityList;
    }
    @Ehcache(addOrdel = false)
    @Override
    public boolean updateModules(String tableName, String[] ids) {
        updateByHql("delete from ModuleExtRuleEntity where moduleTable =?",new Object[]{tableName});
        if(!ArrayUtils.isEmpty(ids)){
            for(String str:ids){
                save(new ModuleExtRuleEntity(str,tableName));
            }
        }
        return true;
    }

    @Override
    public boolean delModule(String id) {
        updateByHql("delete from ModuleExtDetailEntity where moduleExtEntity.id=? ",new Object[]{id});
        updateByHql("delete from ModuleExtEntity where id=?",new Object[]{id});
        return true;
    }

    @Override
    public List<ModuleExtEntity> getAllAndCheckedList(String tableName) {
        DetachedCriteria criteria =DetachedCriteria.forClass(ModuleExtEntity.class);
        List<ModuleExtEntity> list=findDataList(criteria);
        if(CollectionUtils.isNotEmpty(list)){
            List<String> moduleIdList=getDataListByHql("select moduleId from ModuleExtRuleEntity where moduleTable =?",new Object[]{tableName});
            Set<String> moduleSet=new HashSet<>(moduleIdList);
            for(ModuleExtEntity entity: list){
                if(moduleSet.contains(entity.getId())){
                    entity.setChecked(true);
                }
            }
        }
        return list;
    }
    @Ehcache
    @Override
    public List<ModuleExtEntity> getModulesDetailLink(String tableName, String type) {
        String mainSql="select entity from ModuleExtEntity entity,ModuleExtRuleEntity rule ";
        mainSql+=" where entity.id=rule.moduleId and rule.moduleTable=?";
        List<ModuleExtEntity> moduleExtEntityList=getDataListByHql(mainSql,new Object[]{tableName});
        if(CollectionUtils.isNotEmpty(moduleExtEntityList)){
            for(ModuleExtEntity entity:moduleExtEntityList){
                List<ModuleExtDetailEntity> detailList=getDataListByHql(" from ModuleExtDetailEntity where moduleExtEntity.id=? and module_type=?",new Object[]{entity.getId(),type});
                entity.setModuleExtDetails(detailList);
            }
        }
        return moduleExtEntityList;
    }
    @Ehcache(addOrdel = false)
    @Override
    public Serializable updateModules(ModuleExtEntity extEntity) {
        return mergeOneToMany(extEntity,"moduleExtEntity",extEntity.getModuleExtDetails());
    }
}
