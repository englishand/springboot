package com.zhy.test.proxy.testProxy.controller;

import com.zhy.test.proxy.testProxy.service.TestProxyService;
import com.zhy.test.proxy.testProxy.service.impl.TestProxyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("proxy")
public class TestProxyController {

    @Autowired
    private TestProxyService testProxyService;

    @ResponseBody
    @RequestMapping("/test1")
    public void test1(){
        testProxyService.updateActual();
    }
}
