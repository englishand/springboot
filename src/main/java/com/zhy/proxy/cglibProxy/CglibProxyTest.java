package com.zhy.proxy.cglibProxy;


import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class  CglibProxyTest {

    public static void main(String[] args) {

        //在指定目录下生成动态代理类,我们可以反编译看一下里面是什么
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,"D:\\java\\java_workplace");

        MthInvoker mthInvoker = new MthInvoker();
        //生成Enhancer对象，类似于JDK动态代理的Proxy类
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件。设置父类，因为cglib是针对指定的类生成一个子类，所以需要指定父类
        enhancer.setSuperclass(CglibSon.class);
        //设置回调函数
        enhancer.setCallback(mthInvoker);
        //用create方法创建代理类
        CglibSon cglibSon = (CglibSon) enhancer.create();

        cglibSon.gotoHome();
    }

    /**
     * Java动态代理和cglib代理的区别：
     * 1.java动态代理是利用反射机制并通过Proxy实例化生成一个实现代理类接口的匿名类，在调用具体方法后调用InvokeHandler来处理
     * 2.cgLib是利用asm开源包，对代理对象类的class文件加载进来，通过修改其字节码来生成子类来处理。
     *      1）：如果目标实现了接口，默认情况下会采用Jdk的动态代理实现Aop
     *      2): 如果目标实现了接口，可以强制使用cglib实现aop
     *      3): 如果目标没有实现接口，必须使用cglib来实现aop。spring会在jdk和cgLib之间自动切换
     *      4）：强制使用方法：添加cglib库，spring_home/cglib/*.jar,并在spring配置文件中加入<aop:aspectj-autoproxy proxy-target-class="true"/>
     *
     * Jdk动态代理和cglib生成字节码的区别：
     *  1：jdk动态代理只能对实现接口的类进行代理，不能针对类
     *  2.cglib代理是通过修改类的字节码生成子类来覆盖代理的方法，进行代理，应为是继承，所以方法和类不要用final修饰。
     */
}
