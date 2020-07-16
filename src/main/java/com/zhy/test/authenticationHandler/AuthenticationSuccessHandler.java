package com.zhy.test.authenticationHandler;

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if (savedRequest != null) {
            logger.info("测试登录已经失败"+savedRequest);
        }else {
            logger.info("测试登录已经成功");
            getRedirectStrategy().sendRedirect(request,response,"/login/loginIn");
        }
        super.onAuthenticationSuccess(request,response,authentication);
        logger.info("User: "+request.getParameter("username")+ "login successfully");
//        this.returnJson(response);
    }

    private void returnJson(HttpServletResponse response) throws IOException{
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().println("{\"exceptionId\":\"null\",\"messageCode\":\"200\"," +

                "\"message\": \"Login successfully.\",\"serverTime\": " + System.currentTimeMillis() +"}");
    }
}
