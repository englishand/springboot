package com.zhy.filter;

import com.zhy.token.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/login/logIn");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            request.setCharacterEncoding("UTF-8");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

//        String token = getJwtFromRequest(request);
//        if(token==null){
//            return null;
//        }
//        String username = getUsernameFromJwt(token,authParameters.getJwtTokenSecret());
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails,null,userDetails.getAuthorities());
//        return authentication;
    }


    /**
     * 成功验证后调用的方法
     * 如果验证成功就生成token并返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        User user = (User) authResult.getPrincipal();
        String role = "";
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority:authorities){
            role = authority.getAuthority();
        }

        String token = JwtTokenProvider.createJwtToken(user.getUsername(),role);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        String tokenstr = JwtTokenProvider.TOKEN_PERFIX+token;
        response.setHeader(JwtTokenProvider.TOKEN_HEADER,tokenstr);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed,reson: "+failed.getMessage());
    }

    private String getJwtFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (token!=null && token.startsWith("Bearer")){
            return token.replace("Bearer","");
        }
        return null;
    }

    private String getUsernameFromJwt(String token,String signkey){
        return Jwts.parser().setSigningKey(signkey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

}
