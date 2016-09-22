package com.sys.util;

import com.sys.annotation.Ehcache;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * @author zzl
 *         Date:2014-10-09
 */
public class CacheUtil {
    /**
     * 获取缓存
     * @param cache
     * @return
     */
    public static net.sf.ehcache.Ehcache getCache(String cache){
        return CacheManager.getInstance().getEhcache(cache);
    }

    /**
     * 清理缓存
     * @param cache
     */
    public static void clearCache(String cache){
        getCache(cache).removeAll();
    }

    /**
     * 清理所有缓存
     */
    public static void clearAllCache(){
        CacheManager.getInstance().clearAll();
    }
}
