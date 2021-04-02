package com.zhy.service.Impl;

import com.zhy.entity.Role;
import com.zhy.entity.User;
import com.zhy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ToTrasactionalImpl {

    @Autowired
    private UserService userService;

    public void transactionalExample(User user, Role role){
        String uuid = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(user.getId())){
            user.setId(uuid.replaceAll("-",""));
        }

        userService.insert(user);
        user.setUserDescript("测试事务");
        user.setId(uuid);
        userService.insert(user);
    }
}
