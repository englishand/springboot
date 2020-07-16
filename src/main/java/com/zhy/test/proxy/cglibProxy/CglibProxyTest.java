package com.zhy.test.proxy.cglibProxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class  CglibProxyTest {

    @Test
    public void main() throws Exception{
        CglibSon son = new CglibSon();
        Enhancer enhancer = new Enhancer();
        Callback s = (Callback) new MthInvoker(son);
        enhancer.setSuperclass(son.getClass());
        Callback callback[] = new Callback[]{s};
        enhancer.setCallbacks(callback);

        CglibSon cglibSon = (CglibSon) enhancer.create();
        //这里可以看到这个类被代理，在执行方法前执行aopMethod().
        //这里需要注意的是oneDay（）方法和onedayFinal（）的区别。onedayFinal的方法aopMethod执行2次，oneDay的aopMethod执行1次 ,注意这里和jdk的代理的区别
        cglibSon.gotoHome();
        cglibSon.gotoSchool();
        cglibSon.onday();
        cglibSon.onedayFinal();
    }
}
