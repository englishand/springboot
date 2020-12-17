package com.zhy.test.controller;

import com.zhy.test.entity.User;
import com.zhy.test.service.UserService;
import com.zhy.test.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

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
}
