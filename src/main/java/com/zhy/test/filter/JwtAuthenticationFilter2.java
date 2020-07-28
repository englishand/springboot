package com.zhy.test.filter;

import com.zhy.test.service.DatabaseUserDetailsService;
import com.zhy.test.token.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class JwtAuthenticationFilter2 extends OncePerRequestFilter {

    @Autowired
    private DatabaseUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(JwtTokenProvider.TOKEN_HEADER);
        String username = null;
        String authToen = null;
        if (header!=null && header.startsWith(JwtTokenProvider.TOKEN_PERFIX)){
            authToen = header.replace(JwtTokenProvider.TOKEN_PERFIX,"");
            try{
                username = tokenProvider.getUsername(authToen);
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }else {
            log.warn("could't find bearer string,will ignore the header");
        }
        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenProvider.validateToken(authToen)){
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user "+username+",setting security context");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }
}
