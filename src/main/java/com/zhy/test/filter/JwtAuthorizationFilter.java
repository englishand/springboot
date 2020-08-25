package com.zhy.test.filter;

import com.zhy.test.token.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * 登录成功之后对token进行校验操作
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String tokenHeader = request.getHeader(JwtTokenProvider.TOKEN_HEADER);
        //如果请求头中没有Authorization信息则直接放行
        if (tokenHeader==null || !tokenHeader.startsWith(JwtTokenProvider.TOKEN_PERFIX)){
            chain.doFilter(request,response);
            return;
        }
        //如果请求头中有token，则进行解析，并且设置认证信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        //校验usernamepasswordAuthenticationtoken
        super.doFilterInternal(request,response,chain);
    }

    //从token中获取用户信息并新建一个token
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader){
        String token = tokenHeader.replace(JwtTokenProvider.TOKEN_PERFIX,"");
        boolean isValid = JwtTokenProvider.validateToken(token);
        if (!isValid){
            return null;
        }
        String username = JwtTokenProvider.getUsername(token);
        String role = JwtTokenProvider.getUserRole(token);
        if (username!=null){
            return new UsernamePasswordAuthenticationToken(username,null,
                    Collections.singleton(new SimpleGrantedAuthority(role)));
        }
        return null;
    }

}
