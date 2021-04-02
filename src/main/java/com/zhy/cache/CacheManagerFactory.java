package com.zhy.cache;

import org.springframework.stereotype.Component;

@Component
public class CacheManagerFactory {

    private CacheManager userManager = new MapCacheManager();

    private CacheManager studentManager = new MapCacheManager();

    public CacheManager getUserManager() {
        return userManager;
    }

    public CacheManager getStudentManager() {
        return studentManager;
    }
}
