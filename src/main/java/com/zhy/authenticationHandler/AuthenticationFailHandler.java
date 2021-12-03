package com.zhy.authenticationHandler;

import com.zhy.service.UserService;
import com.zhy.utils.ResponseResult;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("authenticationFailHandler")
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(AuthenticationFailHandler.class);

    @Autowired
    private UserService userService;

    //规则定义：1小时之内5次机会，就触发限流行为
    Set<RequestLimitRule> rules =
            Collections.singleton(RequestLimitRule.of(1 * 2, TimeUnit.MINUTES,2));
    RequestRateLimiter limiter = new InMemorySlidingWindowRequestRateLimiter(rules);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        logger.info("failHandler 处理中...");
        String userName = request.getParameter("username");
        //计数器加1，并判断该用户是否已经到了触发了锁定规则
        boolean reachLimit = limiter.overLimitWhenIncremented(request.getParameter("username"));

        if(reachLimit){ //如果触发了锁定规则，修改数据库non_Locked字段锁定用户
            userService.updateLockedByUsername(userName);
        }

        this.returnJson(request,response,exception);
    }

    public void returnJson(HttpServletRequest request,HttpServletResponse response,AuthenticationException exception)
        throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type","text/html;charset=utf-8");

        ResponseResult result;
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException){
            result = ResponseResult.errorWithMessage("账户名或者密码输入错误222222222222222222222!");
        }else if (exception instanceof LockedException){
            result = ResponseResult.errorWithMessage("账户被锁定，请联系管理员!");
        }else if (exception instanceof CredentialsExpiredException){
            result = ResponseResult.errorWithMessage("密码过期，请联系管理员!");
        }else if (exception instanceof AccountExpiredException){
            result = ResponseResult.errorWithMessage("账户过期，请联系管理员!");
        }else if (exception instanceof DisabledException) {
            result = ResponseResult.errorWithMessage("账户被禁用，请联系管理员!");
        } else {
            result = ResponseResult.errorWithMessage("登录失败!");
        }

        response.getWriter().write(result.toString());
    }
}
