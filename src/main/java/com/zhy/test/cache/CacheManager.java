package com.zhy.test.cache;

import java.util.List;

public interface CacheManager {

    void addToCachea(Object key,Object value);

    Object getFromCache(Object key);

    void removeFromCache(Object key);

    List getAllFromCache();

    void initCache();

    void clearCache();
}
