package com.zhy.test.proxy.cglibProxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MthInvoker implements MethodInterceptor {

    private CglibSon cglibSon;
    public MthInvoker(CglibSon son){
        this.cglibSon = son;
    }
    private void aopMethod(){
        System.out.println("in aopmethod---------");
    }

    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
    throws Throwable{
        aopMethod();
        Object a = method.invoke(cglibSon,args);
        return a;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        return null;
    }
}
