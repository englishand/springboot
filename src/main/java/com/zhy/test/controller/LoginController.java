package com.zhy.test.controller;

import com.zhy.test.service.DatabaseUserDetailsService;
import com.zhy.test.service.LoginService;
import com.zhy.test.token.JwtTokenProvider;
import com.zhy.test.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private DatabaseUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenProvider tokenProvider;


    @RequestMapping(value = "in")
    public String index(){
        return "login";
    }

    @RequestMapping("/loginIn")
    public String loginIn(){
        return "welcome";
    }

    @RequestMapping("/error")
    public String error(){
        return "error";
    }


//    @RequestMapping("loginIn")
//    @ResponseBody
//    public ResponseResult loginIn(String username, String password,
//                                  HttpServletRequest request, HttpServletResponse response){
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        final String token = tokenProvider.generateJsonWebToken(userDetails);
//        ResponseResult result = ResponseResult.successWithData(token);
//        return result;
//    }

    @RequestMapping("/loginWelcome")
    public String loginWelcome(){
        return "welcome";
    }

    @RequestMapping("/loginOut")
    public String loginOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        log.info(session.getAttribute("user").toString());
        session.removeAttribute("user");
        return "login";
    }


    @RequestMapping(value = "/level1/{path}")
    public String level1(@PathVariable("path")String path){
        return "level1/"+path;
    }
    @RequestMapping(value = "/level2/{path}")
    public String level2(@PathVariable("path")String path){
        return "level2/"+path;
    }
    @RequestMapping(value = "/level3/{path}")
    public String level3(@PathVariable("path")String path){
        return "level3/"+path;
    }

    @ResponseBody
    @RequestMapping("/doBusiness")
    public ResponseResult doBusiness(HttpServletRequest request){
        HttpSession session = request.getSession();
        log.info(session.getAttribute("user")+"");
        return ResponseResult.success();
    }
}
