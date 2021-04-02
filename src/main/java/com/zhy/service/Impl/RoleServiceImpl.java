package com.zhy.service.Impl;

import com.zhy.dao.BaseDao;
import com.zhy.entity.Role;
import com.zhy.mapping.RoleMapper;
import com.zhy.service.RoleService;
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
