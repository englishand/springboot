package com.zhy.test.proxy.JavaProxy;

import java.lang.reflect.Proxy;

public class JavaProxyTest {

    public static void main(String[] args) throws Exception{
        JavaProxyInterface javaProxyInterface = new JavaProxyInterface() {
            @Override
            public void gotoSchool() {
                System.out.println("gotoSchool");
            }

            @Override
            public void totoWork() {
                System.out.println("totoWork");
            }

            @Override
            public void oneDay() {
                System.out.println("--------------");
                gotoSchool();
                totoWork();
                System.out.println("----------------");
            }

            @Override
            public final void oneDayFinal() {
                gotoSchool();
                totoWork();
            }
        };

        JavaProxyInterface newJavaProxyInterface = (JavaProxyInterface) Proxy.newProxyInstance(
                JavaProxyTest.class.getClassLoader(),
                new Class[]{JavaProxyInterface.class},
                new MyInvocationHandler(javaProxyInterface));

        newJavaProxyInterface.gotoSchool();
        newJavaProxyInterface.totoWork();
        newJavaProxyInterface.oneDay();
        newJavaProxyInterface.oneDayFinal();
    }

}
