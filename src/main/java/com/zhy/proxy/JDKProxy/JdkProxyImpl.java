package com.zhy.proxy.JDKProxy;

import com.zhy.proxy.JDKProxy.interf.JdkProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdkProxyImpl implements JdkProxy {

    private String name;

    public JdkProxyImpl(String name) {
        this.name = name;
    }

    @Override
    public String gotoSchool(String name) {
        log.info(name+":我要去上学");
        return "name:"+name;
    }

    @Override
    public void gotoHosptol(String name) {
        log.info(name+":我要去旅馆");
    }

    @Override
    public Object reBack(Object param) {
        return param;
    }
}
