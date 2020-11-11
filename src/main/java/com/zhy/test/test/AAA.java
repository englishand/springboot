package com.zhy.test.test;

import java.lang.reflect.Method;

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
        System.out.println("aaaaaaaaaaaaaaaa");
    }

    protected void loadStringAAA(){
        System.out.println("ccccccccccccccccc");
    }

    public void load(){
        this.loadString();
        loadStringAAA();
    }

    public static void main(String[] args){
        try {
            Method method = AAA.class.getMethod("setName", String.class);
            System.out.println(method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    //aaaaaaaaaaaa
    //bbbbbbbbbbbb
    //cccccccccccc
}
