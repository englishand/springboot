package com.zhy.test.proxy.JavaProxy;

import com.zhy.test.proxy.JavaProxy.interf.JavaProxy;

import java.lang.reflect.Proxy;

public class JavaProxyTest {

    public static void main(String[] args) throws Exception{
        JavaProxy javaProxy = new JavaProxyImpl();

        System.out.println(JavaProxyTest.class.getClassLoader());//获取该类的类装载器
        System.out.println(javaProxy.getClass().getClassLoader());

        JavaProxy newJavaProxy = (JavaProxy) Proxy.newProxyInstance(
                javaProxy.getClass().getClassLoader(),
                javaProxy.getClass().getInterfaces(),
                new MyInvocationHandler(javaProxy));

        newJavaProxy.gotoSchool("渣渣辉");
    }
    /**
     * 1.ClassLoader作用：java程序写好以后是以.java（文本文件）的文件存在磁盘上，然后，我们通过(bin/javac.exe)编译命令把.java文件编译成.class文件（字节码文件），
     * 并存在磁盘上。但是程序要运行，首先一定要把.class文件加载到JVM内存中才能使用的，我们所讲的classLoader,就是负责把磁盘上的.class文件加载到JVM内存中
     * 2.Class加载时调用类加载器的顺序(启动类加载器/实际类加载器):当一个类要进行加载时，例：JavaProxyTest,它将会启动应用类加载器加载JavaProxyTest类，
     * 但应用类加载器不会直接调用，而是调用看是否有父加载器，结果有，是扩展类加载器，扩展类加载器也不会直接去加载，它看自己是否有父加载器没，结果它还是有的，是根类加载器。
     *
     * 所以这个时候根类加载器就去加载这个类，可在%JAVA_HOME%\jre\lib下，它找不到proxy.javaProxy.JavaProxyTest这个类，所以他告诉他的子类加载器，我找不到，你去加载吧，子类扩展类加载器去%JAVA_HOME%\lib\ext去找，也找不着，它告诉它的子类加载器 AppClassLoader，我找不到这个类，你去加载吧，结果AppClassLoader找到了，就加到内存中，并生成Class对象。
     * 这个时间时候启动类加载器（应用类加载器）和实际类加载器（应用类加载器）是同一个.
     */

}
