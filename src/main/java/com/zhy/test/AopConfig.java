package com.zhy.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 测试Full模式和Lite模式
 */
@Configuration
public class AopConfig {

    @Bean
    public IOCTest getIOCTest(){
        IOCTest iocTest = new IOCTest();
        iocTest.setName("aaa");
        iocTest.setPass("bbb");
        return iocTest;
    }

    @Bean
    public IOCTest getIOCTest2(){
        IOCTest iocTest = new IOCTest();
        iocTest.setName("aaa222");
        iocTest.setPass("bbb222");
        // 模拟依赖于user实例  看看是否是同一实例
        System.out.println(System.identityHashCode(getIOCTest()));
        System.out.println(System.identityHashCode(getIOCTest()));

        return iocTest;
    }

    public static class InnerConfig{
        @Bean
        public IOCTest testInner(){
            IOCTest iocTest = new IOCTest();
            iocTest.setName("aaaInner");
            iocTest.setPass("bbbInner");
            return iocTest;
        }
    }

}
