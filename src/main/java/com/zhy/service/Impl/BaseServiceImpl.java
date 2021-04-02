package com.zhy.service.Impl;

import com.zhy.dao.BaseDao;
import com.zhy.service.BaseService;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected abstract BaseDao getBaseDao();

    @Override
    public List<T> listByObj(Object o) {
        return getBaseDao().listByObj(o);
    }

    @Override
    public T selectByPrimaryKey(String id) {
        return (T) getBaseDao().selectByPrimaryKey(id);
    }

    @Override
    public int insert(T t) {
        return getBaseDao().insert(t);
    }
}
