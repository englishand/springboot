package com.zhy.proxy.cglibProxy;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 拦截器
 */

@Slf4j
public class MthInvoker implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //这里用的不是反射。
        //代理对象o:com.zhy.test.proxy.cglibProxy.CglibSon$$EnhancerByCGLIB$$6ff65d77@646007f4
        //目标类方法method:public void com.zhy.test.proxy.cglibProxy.CglibSon.gotoHome()
        //objects:方法参数
        //表示要触发父类的方法对象methodProxy:sig1:gotoHome()V
        //                               sig2:CGLIB$gotoHome$0()V
        Object intercept = methodProxy.invokeSuper(o,objects);
        log.info("对目标类进行增强：你可以回家了");
        return intercept;
    }
}
