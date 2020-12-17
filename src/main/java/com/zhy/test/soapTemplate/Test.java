package com.zhy.test.soapTemplate;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @RunWith：用于指定junit运行环境，是junit提供给其他框架测试环境接口扩展，为了便于使用spring的依赖注入，
 * spring提供了org.springframework.test.context.junit4.SpringJUnit4ClassRunner作为Junit测试环境
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test {

    @org.junit.Test
    public void mains(){
        System.out.println(SoapTemplateInit.blackList);
    }
}
