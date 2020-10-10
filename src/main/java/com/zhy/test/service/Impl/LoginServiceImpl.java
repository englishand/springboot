package com.zhy.test.service.Impl;

import com.zhy.test.entity.User;
import com.zhy.test.service.LoginService;
import com.zhy.test.service.UserService;
import com.zhy.test.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult loginIn(String username, String password, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = new User();
        String uuid = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(user.getId())){
            user.setId(uuid.replaceAll("-",""));
        }
//        try{
//            userService.insert(user);
//            user.setUserDescript("测试事务");
//            user.setId(uuid);
//            userService.insert(user);
//        }catch (Exception  e){
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
        try{
            this.transactionalExample(user);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        if (ObjectUtils.isEmpty(username)){
            session.setAttribute("user",user);
            return ResponseResult.errorWithMessage("用户名为空！");
        }else {
            user.setUsername(username);
            user.setPassword(password);
            session.setAttribute("user",user);
            return ResponseResult.successWithData(username);
        }
    }

    @Transactional
    public void transactionalExample(User user){
        String uuid = UUID.randomUUID().toString();
        if (StringUtils.isEmpty(user.getId())){
            user.setId(uuid.replaceAll("-",""));
        }
//        try{
            userService.insert(user);
            user.setUserDescript("测试事务");
            user.setId(uuid);
            userService.insert(user);
//        }catch (Exception  e){
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
    }
}
