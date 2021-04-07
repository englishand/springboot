package com.zhy.proxy.JavaProxy;

import com.zhy.proxy.JavaProxy.interf.JavaProxy;

public class JavaProxyImpl implements JavaProxy {

    @Override
    public void gotoSchool(String name) {
        System.out.println(name+":我要去上学");
    }

    @Override
    public void gotoHosptol(String name) {
        System.out.println(name+":我要去旅馆");
    }
}
