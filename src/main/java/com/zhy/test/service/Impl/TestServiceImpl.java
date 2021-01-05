package com.zhy.test.service.Impl;

import com.zhy.test.service.TestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestServiceImpl implements TestService {

    @Override
    @Transactional
    public String getMsg() throws Exception {
        String msg = "您好！欢迎使用xx系统";
        if (false){
            throw new Exception("系统异常");
        }
        System.out.println("是否还执行-------------------xx系统");
        System.out.println(msg);
        return msg;
    }
}
