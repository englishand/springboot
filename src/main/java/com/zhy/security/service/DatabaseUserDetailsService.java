package com.zhy.security.service;

import com.zhy.entity.User;
import com.zhy.service.UserService;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 跟踪UserDetailsService,身份认证的调用流程
 *  AbstractAuthenticationProcessingFilter的doFilter方法，调用->
 *  UsernamePasswordAuthenticationFilter的attemptAuthenticatoin方法，调用->
 *  ProviderManager的authenticate方法，调用->
 *  AbstractUserDetailsAuthenticationProvider的authenticate方法，调用->
 *  当前对象的loadUserByUsername方法。
 *  通过以上过滤器
 */
@Service("databaseUserDetailsService")
public class DatabaseUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(DatabaseUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private static final int duration = 1*1;
    private static Map map = new HashMap();

    //规则定义：1小时之内5次机会，就触发限流行为
    Set<RequestLimitRule> rules =
            Collections.singleton(RequestLimitRule.of(duration, TimeUnit.MINUTES,2));
    RequestRateLimiter limiter = new InMemorySlidingWindowRequestRateLimiter(rules);

    /**
     * 在用户登陆时，spring会调用这个方法去获得user的信息（密码等），以对比页面传过来的用户名和密码是否正确。
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);

        List<String> roleCodeList = userRepository.queryUserOwnedRoleCodes(username);

        List<GrantedAuthority> authorities =
                roleCodeList.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());

        //配置限制登录错误次数开始
        //计数器加1，并判断该用户是否已经到了触发了锁定规则
        boolean reachLimit = limiter.overLimitWhenIncremented(username);

        if(reachLimit){ //如果触发了锁定规则，修改数据库non_Locked字段锁定用户
            user.setAccountNonLocked(false);
            userService.updatNonLockedById(user.getId(),0);//这里设置为false,故nonlocked值为0
            map.put(user.getId(),new Date());
        }
        Date old = (Date) map.get(user.getId());
        if (old != null){
            Date newDate  = new Date();
            boolean isTimeOut = (newDate.getTime()-old.getTime())/(60*1000)>=1;
            if (isTimeOut){//时间过期后，恢复用户正常状态
                user.setAccountNonLocked(true);
                userService.updateById(user);
                map.remove(user.getId());
            }
        }
        //配置限制登录错误次数结束

        user.setAuthorities(authorities);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(),authorities);

        logger.info("当前登录的用户真实信息为: "+user.toString());

        return user;
    }


}
