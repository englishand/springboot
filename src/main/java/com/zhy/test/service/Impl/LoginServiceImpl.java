package com.zhy.test.service.Impl;

import com.zhy.test.entity.User;
import com.zhy.test.service.LoginService;
import com.zhy.test.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public ResponseResult loginIn(String username, String password, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(username)){
            return ResponseResult.errorWithMessage("用户名为空！");
        }else {
            HttpSession session = request.getSession();
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            session.setAttribute("user",user);
            return ResponseResult.successWithData(username);
        }
    }
}
