package com.zhy.service.Impl;

import com.zhy.cache.CacheManagerFactory;
import com.zhy.entity.Role;
import com.zhy.entity.User;
import com.zhy.service.LoginService;
import com.zhy.service.UserService;
import com.zhy.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("loginServiceImpl")
public class LoginServiceImpl implements LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private ToTransactionalImpl toTransactional;
    @Autowired
    private CacheManagerFactory cacheManagerFactory;

    @Autowired
    private ApplicationContext context;
    private LoginService proxyLogin;//代理对象

    @PostConstruct//初始化方法
    private void testProxy(){//判断 proxyLogin是否是代理对象，通过那种方式代理的
        proxyLogin = context.getBean(LoginService.class);
        boolean isAopProxy = AopUtils.isAopProxy(proxyLogin);
        boolean isCglib = AopUtils.isCglibProxy(proxyLogin);
        boolean isJdk = AopUtils.isJdkDynamicProxy(proxyLogin);
        logger.info(String.format("测试proxyLogin事务代理方式：isAopProxy[%s] | isCglib[%s] | isJdk[%s]",isAopProxy,isCglib,isJdk));
        //测试proxyLogin事务代理方式：isAopProxy[true] | isCglib[true] | isJdk[false]
    }

    @Override
    @Transactional
    public ResponseResult loginIn(String username, String password, HttpServletRequest request) {

        //测试当前对象是否是代理对象
        boolean isAopProxy = AopUtils.isAopProxy(this);
        boolean isCglib = AopUtils.isCglibProxy(this);
        boolean isJdk = AopUtils.isJdkDynamicProxy(this);
        logger.info(String.format("测试this对象事务代理方式：isAopProxy[%s] | isCglib[%s] | isJdk[%s]",isAopProxy,isCglib,isJdk));
        //测试this对象事务代理方式：isAopProxy[false] | isCglib[false] | isJdk[false]

        //判断toTransactional是否是代理对象
        boolean isAopProxy2 = AopUtils.isAopProxy(toTransactional);
        boolean isCglib2 = AopUtils.isCglibProxy(toTransactional);
        boolean isJdk2 = AopUtils.isJdkDynamicProxy(toTransactional);
        logger.info(String.format("测试toTransactional事务代理方式：isAopProxy[%s] | isCglib[%s] | isJdk[%s]",isAopProxy2,isCglib2,isJdk2));
        //测试toTransactional事务代理方式：isAopProxy[true] | isCglib[true] | isJdk[false]

        User user = new User();
        List<Role> role = new ArrayList<>();
        log.info("测试security中AuthenticationSuccessHandler的请求重定向封装问题：{}",username);
        if (StringUtils.isEmpty(username)){
            user = (User)cacheManagerFactory.getUserManager().getFromCache("username");
            user.setUsername(username+"测试事务");
            role = user.getRoles();
        }
        toTransactional.transactionalExample(user,role);

        return ResponseResult.successWithData(username);
    }

}
