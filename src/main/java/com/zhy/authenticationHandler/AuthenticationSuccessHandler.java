package com.zhy.authenticationHandler;

import com.zhy.cache.CacheManagerFactory;
import com.zhy.entity.User;
import com.zhy.jwt.JwtTokenUtils;
import com.zhy.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Service("authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CacheManagerFactory cacheManagerFactory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        logger.info("User: "+request.getParameter("username")+ ":login successfully");

        //发现下面一段没有用
//        if (savedRequest != null) {
//            String redirectUrl = savedRequest.getRedirectUrl();
//            if (redirectUrl!=null && !redirectUrl.contains("logOut")){
//                getRedirectStrategy().sendRedirect(request,response,redirectUrl);
//            }else {
//                //在这里通过request封装username,通过重定向返回到/login/loginIn,获取不到封装值，因此采用Cache方式存储
////                request.setAttribute("username",request.getParameter("username"));
//                cacheManagerFactory.getUserManager().addToCachea("username",request.getParameter("username"));
//                getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
//            }
//        }else {
//            getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
//        }
//        return;

        //开始使用jwt
//        String username = request.getParameter("username");
//        User userDetails = userRepository.findByUsername(username);
//        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(userDetails.getRoles().toString()));
//        userDetails.setAuthorities(authorities);
//        String token = jwtTokenUtils.generateToken(userDetails);
//        cacheManagerFactory.getUserManager().addToCachea("username",userRepository.findByUsername(username));
//        // 返回创建成功的token
//        // 但是这里创建的token只是单纯的token
//        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/json;charset=utf-8");
//        String tokenstr = "Bearer "+token;
//        response.setHeader("token",tokenstr);

        getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
    }

}
