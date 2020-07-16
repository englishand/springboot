package com.zhy.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhy.test.service.LoginService;
import com.zhy.test.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;


    @GetMapping("/in")
    public String index(){
        return "login";
    }

    @RequestMapping("/loginIn")
    public String loginIn(String username, String password, HttpServletRequest request){
        ResponseResult result = loginService.loginIn(username,password,request);
        log.info(result.toString());
        return "welcome";
    }

    @RequestMapping("/loginOut")
    public String loginOut(String username,HttpServletRequest request){
        HttpSession session = request.getSession();
        log.info(session.getAttribute("user").toString());
        session.removeAttribute("user");
        return "login";
    }


    @GetMapping("/level1/{path}")
    public String level1(@PathVariable("path")String path){
        return "level1/"+path;
    }
    @GetMapping("/level2/{path}")
    public String level2(@PathVariable("path")String path){
        return "level2/"+path;
    }
    @GetMapping("/level3/{path}")
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
