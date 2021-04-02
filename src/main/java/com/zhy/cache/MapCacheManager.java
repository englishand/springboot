package com.zhy.cache;

import java.util.*;

public class MapCacheManager implements CacheManager{

    private Map cache;
    public MapCacheManager(){
        super();
        this.cache = new HashMap();
    }

    @Override
    public void addToCachea(Object key, Object value) {
        this.cache.put(key, value);
    }

    @Override
    public Object getFromCache(Object key) {
        return this.cache.get(key);
    }

    @Override
    public void removeFromCache(Object key) {
        this.cache.remove(key);
    }

    @Override
    public List getAllFromCache() {
        List ret = new ArrayList();
        Collection con = cache.values();
        if (con!=null){
            for (Iterator it=con.iterator();it.hasNext();){
                ret.add(it.next());
            }
        }
        return null;
    }

    @Override
    public void initCache() {
        //do nothing
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}
