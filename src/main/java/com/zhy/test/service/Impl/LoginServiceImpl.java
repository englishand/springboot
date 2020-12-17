package com.zhy.test.service.Impl;

import com.zhy.test.cache.CacheManagerFactory;
import com.zhy.test.entity.Role;
import com.zhy.test.entity.User;
import com.zhy.test.service.LoginService;
import com.zhy.test.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private ToTrasactionalImpl toTrasactional;
    @Autowired
    private CacheManagerFactory cacheManagerFactory;

    @Override
    public ResponseResult loginIn(String username, String password, HttpServletRequest request) {
        User user = new User();
        Role role = new Role();
        if (StringUtils.isEmpty(username)){
            username = (String)cacheManagerFactory.getUserManager().getFromCache("username");
            user.setUsername(username+"测试事务");
        }
        try{
            toTrasactional.transactionalExample(user,role);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseResult.successWithData(username);
    }

}
