package com.zhy.test.authenticationHandler;

import com.zhy.test.cache.CacheManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service("authenticationSuccuessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private CacheManagerFactory cacheManagerFactory;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        logger.info("User: "+request.getParameter("username")+ ":login successfully");
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            if (redirectUrl!=null && !redirectUrl.contains("logOut")){
                getRedirectStrategy().sendRedirect(request,response,redirectUrl);
            }else {
                //在这里通过request封装username,通过重定向返回到/login/loginIn,获取不到封装值，因此采用Cache方式存储
//                request.setAttribute("username",request.getParameter("username"));
                cacheManagerFactory.getUserManager().addToCachea("username",request.getParameter("username"));
                getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
            }
        }else {
            getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
        }
        return;
    }

}
