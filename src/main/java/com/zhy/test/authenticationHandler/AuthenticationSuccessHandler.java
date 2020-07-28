package com.zhy.test.authenticationHandler;

import com.zhy.test.service.DatabaseUserDetailsService;
import com.zhy.test.token.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("authenticationSuccuessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private DatabaseUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if (savedRequest != null) {
        }else {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
            final String token = tokenProvider.generateJsonWebToken(userDetails);
            handle(request,response,authentication,token);
            getRedirectStrategy().sendRedirect(request,response,"/login/loginIn?Authorization=Bearer "+token);
        }
//        super.onAuthenticationSuccess(request,response,authentication);
        logger.info("User: "+request.getParameter("username")+ ":login successfully");
    }

    protected void handle(HttpServletRequest request,HttpServletResponse response,Authentication authentication,String token) throws IOException{
        String targetUrl = determineTargetUrl(authentication);
        if (response.isCommitted()){
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
    }

    /**
     *
     * 实现自定义的跳转逻辑
     *
     * @param authentication
     * @return
     */
    protected String determineTargetUrl(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("user")) {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("admin")) {
                isAdmin = true;
                break;
            }
        }
        if (isUser) {
            return "/login/loginWelcome";
        } else if (isAdmin) {
            return "/login/loginWelcome";
        } else {
            throw new IllegalStateException();
        }
    }

}
