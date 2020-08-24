package com.zhy.test.authenticationHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhy.test.utils.ResponseResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Service("authenticationLogoutHandler")
public class AuthenticationLogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String contextPath = request.getContextPath();
        response.sendRedirect(contextPath+"/login/logOut");
    }
}
