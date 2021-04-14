package com.zhy.service.Impl;

import com.zhy.cache.CacheManagerFactory;
import com.zhy.entity.Role;
import com.zhy.entity.User;
import com.zhy.service.LoginService;
import com.zhy.utils.ResponseResult;
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
        log.info("测试security中AuthenticationSuccessHandler的请求重定向封装问题：{}",username);
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
