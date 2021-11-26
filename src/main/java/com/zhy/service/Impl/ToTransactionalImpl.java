package com.zhy.service.Impl;

import com.zhy.entity.Role;
import com.zhy.entity.User;
import com.zhy.service.LoginService;
import com.zhy.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service("toTransactionalImpl")
@Transactional
public class ToTransactionalImpl {

    private static Logger logger = LoggerFactory.getLogger(ToTransactionalImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext context;
    private ToTransactionalImpl toTrans;//代理对象

    @PostConstruct//初始化方法
    private void testProxy(){//判断toTransactional是否是代理对象，以及通过那种方式代理的
        toTrans = context.getBean(ToTransactionalImpl.class);
        boolean isAopProxy = AopUtils.isAopProxy(toTrans);
        boolean isCglib = AopUtils.isCglibProxy(toTrans);
        boolean isJdk = AopUtils.isJdkDynamicProxy(toTrans);
        logger.info(String.format("测试toTrans事务代理方式：isAopProxy[%s] | isCglib[%s] | isJdk[%s]",isAopProxy,isCglib,isJdk));
        //测试toTrans事务代理方式：isAopProxy[true] | isCglib[true] | isJdk[false]
    }

    public void transactionalExample(User user, Role role){
        String uuid = UUID.randomUUID().toString();
        user.setUserDescript("测试事务");
        if (StringUtils.isEmpty(user.getId())){
            user.setId(uuid.replaceAll("-",""));
        }

        userService.insert(user);
        user.setId(uuid);
        userService.insert(user);
    }

}
