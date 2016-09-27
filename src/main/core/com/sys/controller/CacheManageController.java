package com.sys.controller;

import com.sys.entity.CacheManageEntity;
import com.sys.util.CacheUtil;
import com.sys.util.SuccessMsg;
import net.sf.ehcache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzl
 * Date:2014-10-09
 */
@Controller
@RequestMapping("/cache")
public class CacheManageController {
    /**
     * 删除缓存
     * @param cName
     * @return
     */
    @RequestMapping(params = "del")
    @ResponseBody
    public SuccessMsg del(String cName){
        CacheUtil.clearCache(cName);
        return new SuccessMsg(true,"缓存清理成功!");
    }

    /**
     * 获取缓存信息
     * @return
     */
    @RequestMapping(params = "getcaches")
    @ResponseBody
    public List<CacheManageEntity> getCaches(){
       List<CacheManageEntity> list=new ArrayList<>();
       String[] cNames=CacheManager.getInstance().getCacheNames();
       if(cNames!=null){
           for(String str:cNames){
               net.sf.ehcache.Ehcache cache=CacheManager.getInstance().getEhcache(str);
               if(cache!=null)
                    list.add(new CacheManageEntity(str,cache.getSize(),cache.getMemoryStoreSize(),cache.getStatistics().getCacheHits(),cache.getStatistics().getCacheMisses()));
           }
       }
       return list;
    }

    /**
     * 转入缓存页面
     * @return
     */
    @RequestMapping(params = "cache")
    public String showCacheView(){
        return "cache/cachemanage";
    }
}
