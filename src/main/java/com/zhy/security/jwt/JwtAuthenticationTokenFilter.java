package com.zhy.security.jwt;

import com.zhy.cache.CacheManagerFactory;
import com.zhy.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private CacheManagerFactory cacheManagerFactory;
    private String tokenHeader = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //先从url中获取token
        String authToken = request.getParameter("token");
        String authHeader = request.getHeader(this.tokenHeader);
        if (StringUtils.isNotBlank(authHeader) && authHeader.startsWith(tokenHeader)){
            //如果header中存在token，则覆盖掉url中的token
            authToken = authHeader.substring(tokenHeader.length());//即bearer周的内容
        }
        if (StringUtils.isNotBlank(authToken)){
            String username = jwtTokenUtils.getUsernameFromToken(authToken);
            logger.info("checking authentication {} ",username);
            if(username !=null && SecurityContextHolder.getContext().getAuthentication() ==null){
                //从已有的user缓存中取user
                User user = (User) cacheManagerFactory.getUserManager().getFromCache("username");
                //检查token是否有效
                if (jwtTokenUtils.validateToken(authToken,user)){
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //这是用户登录状态
                    logger.info("authenticated user {},setting security context",username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }


}
