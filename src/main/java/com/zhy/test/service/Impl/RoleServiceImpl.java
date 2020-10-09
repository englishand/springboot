package com.zhy.test.service.Impl;

import com.zhy.test.dao.BaseDao;
import com.zhy.test.entity.Role;
import com.zhy.test.mapping.RoleMapper;
import com.zhy.test.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    private RoleMapper roleDao;

    @Override
    protected BaseDao getBaseDao() {
        return this.roleDao;
    }

}
