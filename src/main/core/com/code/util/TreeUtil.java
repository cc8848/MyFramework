package com.code.util;

import com.code.entity.BaseTreeEntity;
import com.sys.entity.SysMenuEntity;
import com.sys.service.ICommonService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lenovo on 2015/1/8.
 */
public class TreeUtil {
    /**
     * 组合树形结构
     * 若pid不为null则返回所有父节点id为pid的列表
     * @param list
     * @param pid
     * @return
     */
    public static  List combineTree(List<? extends BaseTreeEntity> list,String pid){
        Map<String,List<BaseTreeEntity>> treeMap=new ConcurrentHashMap<>();
        List<BaseTreeEntity> tempList=null;
        for(BaseTreeEntity treeEntity:list){
            tempList=null;
            tempList= treeMap.get(treeEntity.getPId());
            if(tempList==null) tempList=new ArrayList<>();
            tempList.add(treeEntity);
            treeMap.put(treeEntity.getPId(),tempList);
        }
        tempList=null;
        Set<String> addedId=new HashSet<>();
        for(String pidl:treeMap.keySet()){
            tempList=null;
            tempList=treeMap.get(pidl);
            if(CollectionUtils.isNotEmpty(tempList)){
                for(BaseTreeEntity treeEntity:tempList){
                    if(treeMap.get(treeEntity.getId())!=null){
                        treeEntity.setChildren(treeMap.get(treeEntity.getId()));
                        addedId.add(treeEntity.getId());
                    }

                }
            }
        }
        tempList=null;
        if(pid!=null)
            return treeMap.get(pid);
        if(MapUtils.isNotEmpty(treeMap)){
            tempList=new ArrayList<>();
            for(String pidl:treeMap.keySet()){
                if(!addedId.contains(pidl))
                    tempList.addAll(treeMap.get(pidl));
            }
        }
        return tempList;
    }
    public static void saveTreeList(List<? extends BaseTreeEntity> treeEntities,ICommonService commonService){
        saveChildren(treeEntities,"0",commonService);
    }
    private  static void saveChildren(List<? extends BaseTreeEntity> treeEntities,String pId,ICommonService commonService){
        for(BaseTreeEntity entity:treeEntities){
            entity.setpId(pId);
            Serializable id= commonService.saveReturnId(entity);
            if(CollectionUtils.isNotEmpty(entity.getChildren())){
                saveChildren(entity.getChildren(), (String) id,commonService);
            }
        }
    }
}
