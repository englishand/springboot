package com.zhy.service.Impl;

import com.zhy.dao.BaseDao;
import com.zhy.entity.Role;
import com.zhy.entity.User;
import com.zhy.mapping.UserMapper;
import com.zhy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserMapper userDao;

    @Override
    public List<Role> selectByUserId(String userId) {
        return null;
    }

    @Override
    public User selectById(String userId) {
        return userDao.selectByPrimaryKey(userId);
    }

    @Override
    public List<User> selectByUser(User user) {
        return null;
    }

    @Override
    public int update(User user) {
        return userDao.updateByPrimaryKey(user);
    }

    @Override
    public int updateById(String userId) {
        return 0;
    }

    @Override
    public int updateLockedByUsername(String username) {
        return userDao.updateLockedByUsername(username);
    }


    @Override
    protected BaseDao getBaseDao() {
        return this.userDao;
    }

}
