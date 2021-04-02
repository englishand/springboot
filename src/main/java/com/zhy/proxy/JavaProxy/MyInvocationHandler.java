package com.zhy.proxy.JavaProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvocationHandler的一个实现，实际上处理代理的逻辑在这里
 */
public class MyInvocationHandler implements InvocationHandler {

    private Object post;

    public MyInvocationHandler(Object post){
        this.post = post;
    }
    private void aopMethod(){
        System.out.println("before method");
    }

    //代理时实际执行的方法，如果要实现原方法，则需要调用method.invoke(post,args)
    //这里还调用了aopMethod(),可以类比于Spring中的切面before注解
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        aopMethod();
        args = new String[]{"张家辉"};
        return method.invoke(post,args);
    }
}
