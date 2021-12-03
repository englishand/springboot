package com.zhy.getBeansOfType.impl;

import com.zhy.getBeansOfType.ApiEnum;
import com.zhy.getBeansOfType.ApiService;
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
