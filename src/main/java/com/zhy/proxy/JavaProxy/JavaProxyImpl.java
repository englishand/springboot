package com.zhy.proxy.JavaProxy;

import com.zhy.proxy.JavaProxy.interf.JavaProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaProxyImpl implements JavaProxy {

    @Override
    public void gotoSchool(String name) {
        log.info(name+":我要去上学");
    }

    @Override
    public void gotoHosptol(String name) {
        log.info(name+":我要去旅馆");
    }
}
