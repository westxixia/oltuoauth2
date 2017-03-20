package com.westxixia.shiro.cache;


import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.io.Serializable;
import java.util.Deque;


/**
 * <p>
 * Title:Session管理器
 * </p>
 * <p>
 * Description: Session管理器
 * </p>
 */
public class SessionCacheManager {
    /**
     * Shiro 缓存管理
     */
    private CacheManager cacheManager;

    /***
     * 默认构造函数
     */
    public SessionCacheManager() {
        super();
    }

    /**
     * @return the cacheManager
     */
    public CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * @param cacheManager the cacheManager to set
     */
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /***
     * 删除并发控制的缓存对象
     *
     * @param cacheName
     * @param key
     */
    public void removeSessionController(String cacheName, String key) {
        Cache<String, Deque<Serializable>> cache = this.getCacheManager().getCache(cacheName);
        cache.remove(key);
    }
}
