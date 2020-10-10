package com.zhy.test.dao;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BaseDao<T> {

    /**
     * 根据条件查询
     * @param t
     * @return
     */
    List<T> listByObj(T t);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    T selectByPrimaryKey(String id);

    /**
     * 将对象插入数据库
     * @param t
     * @return
     */
    int insert(T t);
}
