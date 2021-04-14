package com.zhy.entity;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
@Slf4j
public class AAA {

    private String name;
    private String password;
    private int age;
    public void setName(String name){
        this.name = name;
    }
    public void setPassword(String password){this.password = password;}
    public void setAge(int age){this.age=age;}
    public String toString(){
        return this.name+":"+this.password+":"+this.age;
    }

    private void loadString(){
        log.info("aaaaaaaaaaaaaaaa");
    }

    protected void loadStringAAA(){
        log.info("ccccccccccccccccc");
    }

    public void load(){
        this.loadString();
        loadStringAAA();
    }

    public static void main(String[] args){
        try {
            Method method = AAA.class.getMethod("setName", String.class);
            log.info(method.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //aaaaaaaaaaaa
    //bbbbbbbbbbbb
    //cccccccccccc
}
