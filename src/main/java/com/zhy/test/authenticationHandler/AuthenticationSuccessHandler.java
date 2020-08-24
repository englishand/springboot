package com.zhy.test.authenticationHandler;

import com.zhy.test.token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service("authenticationSuccuessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;

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
                getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
            }
        }else {
            getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
        }
        return;
    }

}
