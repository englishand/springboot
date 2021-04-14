package com.zhy.controller;

import com.zhy.entity.User;
import com.zhy.service.UserService;
import com.zhy.utils.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("listByUser")
    public ResponseResult listByUser(User user){
        ResponseResult result ;
        int i = userService.update(user);
        if (i>0){
            result = ResponseResult.successWithMessage("更新成功");
        }else {
            result = ResponseResult.errorWithMessage("更新用户信息失败");
        }
        return result;
    }

    @RequestMapping("/selectUser")
    public ModelAndView selectId(String userId, ModelAndView modelAndView){
        //测试拦截器HandlerInterceptor
        modelAndView.setViewName("/welcome");

        Map map = new HashMap();
        map.put("abc","edf");
        modelAndView.addAllObjects(map);

        Map map2 = modelAndView.getModelMap();
        log.info("测试拦截器是否对modelAndView进行变更：{}",map2);

        //测试BaseDao
        User user = userService.selectById("3");
        log.info("测试BaseDao:{}",user.toString());

        return modelAndView;
    }
}
