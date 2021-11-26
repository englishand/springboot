package com.zhy.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 测试Full模式和Lite模式
 */
public class IOCTest {

    private String name = "123";
    private String pass = "456";



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AopConfig.class);
        String[] beanNames = context.getBeanNamesForType(IOCTest.class);
        for (String name:beanNames){
            IOCTest test = context.getBean(name,IOCTest.class);
            System.out.println("beanName: "+name);
            System.out.println(test.getClass());
            System.out.println(test.toString());
        }
    }
}
