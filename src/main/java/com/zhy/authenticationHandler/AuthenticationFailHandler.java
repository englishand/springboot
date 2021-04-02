package com.zhy.authenticationHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.utils.ResponseResult;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service("authenticationFailHandler")
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        this.returnJson(request,response,exception);
    }

    public void returnJson(HttpServletRequest request,HttpServletResponse response,AuthenticationException exception)
        throws IOException{
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        ResponseResult result;
        if (exception instanceof BadCredentialsException ||
                exception instanceof UsernameNotFoundException){
            result = ResponseResult.errorWithMessage("账户名或者密码输入错误!");
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

        saveException(request,exception);
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}
