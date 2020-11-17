package com.zhy.test.proxy.JavaProxy;

import com.zhy.test.proxy.JavaProxy.interf.JavaProxy;

public class JavaProxyImpl implements JavaProxy {

    @Override
    public void gotoSchool() {
        System.out.println("我要去上学");
    }
}
