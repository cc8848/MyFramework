package com.sys.service.impl;

import com.sys.entity.SysDepEntity;
import com.sys.entity.TreeBean;
import com.sys.service.SysDepService;
import com.sys.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("sysDepService")
@Transactional
public class SysDepServiceImpl extends CommonService implements SysDepService {
    @Override
    public List<TreeBean> getTreeList(long pid) {
        List<SysDepEntity>  list=getDataListByHql("from SysDepEntity  order by xh,id desc",new Object[]{});
        Map<Long,List<TreeBean>> treeMap=new HashMap<>();
        List<TreeBean> list1=null;
        TreeBean treeBean=null;
        for(SysDepEntity entity:list){
            if(entity.getPid()==null)
                entity.setPid(0l);
            if(entity.getId()==pid)
                treeBean=new TreeBean(entity.getId(),entity.getDepName(),null);
            list1=treeMap.get(entity.getPid());
            if(list1==null)
                list1=new ArrayList<>();
            list1.add(new TreeBean(entity.getId(),entity.getDepName(),null));
            treeMap.put(entity.getPid(),list1);
        }
        list=null;
        list1=null;
        List<TreeBean> list2=null;
        for(Long ePid:treeMap.keySet()){
            list1=treeMap.get(ePid);
            if(list1!=null){
                for(TreeBean tree:list1){
                    list2=treeMap.get(tree.getId());
                    if(list2!=null)
                        tree.setChildren(list2);
                }
            }
        }
        list2=null;
        list1=null;
        List<TreeBean> childList=treeMap.get(pid);
        if(treeBean!=null){
            treeBean.setChildren(childList);
            List<TreeBean> treeBeans=new ArrayList<>();
            treeBeans.add(treeBean);
            return treeBeans;
        }else{
            return childList;
        }
    }

    @Override
    public boolean deleteWithChildren(Long id) {
        return updateByHql("delete from SysDepEntity where id like ?",new Object[]{id+"%"})>0;
    }
    @Override
    public List<Long> getChildrenId(long pid) {
       List<TreeBean> treeList=getTreeList(pid);
       Set<Long> set=new HashSet<>();
       set.add(pid);
       if(treeList!=null){
           for(TreeBean bean:treeList){
               set.addAll(getChildId(bean));
           }
       }
        return new ArrayList<>(set);
    }
    private Set<Long> getChildId(TreeBean bean){
        Set<Long> set=new HashSet<>();
        set.add(bean.getId());
        List<TreeBean> list=bean.getChildren();
        if(list!=null&&list.size()>0){
            for(TreeBean bean1:list){
                set.addAll(getChildId(bean1));
            }
        }
        return set;
    }
}
