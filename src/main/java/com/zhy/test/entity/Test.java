package com.zhy.test.entity;

import org.springframework.cglib.beans.BeanMap;

import java.util.concurrent.TimeUnit;

public class Test {

    private String name ;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args){
        Test test = new Test();
        test.setAge(10);
        test.setName("nihao");

        try {
            System.out.println("休眠开始"+System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(5);
            System.out.println("休眠时间结束"+System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BeanMap beanMap = BeanMap.create(test);
        System.out.println(beanMap.get("age"));
        System.out.println(beanMap.keySet());
        System.out.println(beanMap.values());
    }
}
