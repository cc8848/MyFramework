package com.sys.entity;

/**
 * @author zzl
 * Date:2014-10-09
 */
public class CacheManageEntity {
    private String cacheName; //缓存名称
    private int cacheSize; //缓存中对象个数
    private long memoryStoreSize; //占内存大小
    private long cacheHits; //命中次数
    private long cacheMisses; //失误次数

    public CacheManageEntity() {
    }

    public CacheManageEntity(String cacheName, int cacheSize, long memoryStoreSize, long cacheHits, long cacheMisses) {
        this.cacheName = cacheName;
        this.cacheSize = cacheSize;
        this.memoryStoreSize = memoryStoreSize;
        this.cacheHits = cacheHits;
        cacheMisses = cacheMisses;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getMemoryStoreSize() {
        return memoryStoreSize;
    }

    public void setMemoryStoreSize(long memoryStoreSize) {
        this.memoryStoreSize = memoryStoreSize;
    }

    public long getCacheHits() {
        return cacheHits;
    }

    public void setCacheHits(long cacheHits) {
        this.cacheHits = cacheHits;
    }

    public long getCacheMisses() {
        return cacheMisses;
    }

    public void setCacheMisses(long cacheMisses) {
        cacheMisses = cacheMisses;
    }
}
