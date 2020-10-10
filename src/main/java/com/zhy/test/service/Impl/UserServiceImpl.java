package com.zhy.test.service.Impl;

import com.zhy.test.dao.BaseDao;
import com.zhy.test.entity.Role;
import com.zhy.test.entity.User;
import com.zhy.test.mapping.UserMapper;
import com.zhy.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userDao;

    @Override
    public List<Role> selectByUserId(String userId) {
        return null;
    }

    @Override
    public int update(User user) {
        return userDao.updateByPrimaryKey(user);
    }


    @Override
    protected BaseDao getBaseDao() {
        return this.userDao;
    }



}
