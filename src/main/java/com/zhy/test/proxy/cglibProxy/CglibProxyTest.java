package com.zhy.test.proxy.cglibProxy;


import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class  CglibProxyTest {

    public static void main(String[] args) {

        //在指定目录下生成动态代理类,我们可以反编译看一下里面是什么
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"D:\\java\\java_workplace");

        MthInvoker mthInvoker = new MthInvoker();
        //生成Enhancer对象，类似于JDK动态代理的Proxy类
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(CglibSon.class);
        //设置回调函数
        enhancer.setCallback(mthInvoker);
        //用create方法创建代理类
        CglibSon cglibSon = (CglibSon) enhancer.create();

        cglibSon.gotoHome();
    }
}
