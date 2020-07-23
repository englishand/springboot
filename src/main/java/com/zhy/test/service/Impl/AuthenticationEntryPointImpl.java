package com.zhy.test.service.Impl;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决匿名用户访问无权限资源时的异常
 * 使用方式：HttpSecurity..authenticationEntryPoint(AuthenticationEntryPointImpl);
 * 出现的问题：登录之后无法正常跳转页面，具体如下：
 * Whitelabel Error Page
 * This application has no explicit mapping for /error, so you are seeing this as a fallback.
 *
 * Tue Jul 14 15:12:06 CST 2020
 * There was an unexpected error (type=Unauthorized, status=401).
 * Full authentication is required to access this resource
 */
@Service("authenticationEntryPointImpl")
class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if(isAjaxRequest(request)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
        }else{
            response.sendRedirect("/projectone/login/error");
        }

    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
    }
}
