package com.zhy.controller;

import com.zhy.service.LoginService;
import com.zhy.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;


    @RequestMapping(value = "in")
    public String index(){
        return "login";
    }

    @RequestMapping("/loginIn")
    public String loginIn(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response){
        Map map = modelAndView.getModel();
        log.info("modelAndView:{}",map);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try{
            loginService.loginIn(username,password,request);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        String token  = response.getHeader("token");
        log.info("controller token: "+token);
        return "welcome";
    }

    @RequestMapping("/error")
    public String error(){
        return "error";
    }


    @RequestMapping("/welcome")
    public String loginWelcome(String username,String password,HttpServletRequest request){
        log.info("username:{},password:{}",username,password);
        return "welcome";
    }

    @RequestMapping("/logOut")
    public String loginOut(HttpServletRequest request){
        HttpSession session = request.getSession();
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
