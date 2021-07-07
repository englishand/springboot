package com.zhy.designPattern.impl;

import com.zhy.designPattern.ApiEnum;
import com.zhy.designPattern.ApiService;
import org.springframework.stereotype.Service;

@Service
public class TestApiService01 implements ApiService {

    @Override
    public ApiEnum op() {
        return ApiEnum.API_01;
    }

    @Override
    public Object invoke(Object obj) {
        return "执行api接口01";
    }
}
