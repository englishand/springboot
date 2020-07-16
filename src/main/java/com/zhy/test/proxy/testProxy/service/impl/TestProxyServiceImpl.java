package com.zhy.test.proxy.testProxy.service.impl;

import com.zhy.test.proxy.testProxy.service.TestProxyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestProxyServiceImpl implements TestProxyService {

    @Transactional
    public void updateActual() {
        System.out.println("测试");
    }
}
