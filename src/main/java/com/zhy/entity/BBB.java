package com.zhy.entity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BBB extends AAA{

    private static String name ="zhy";
    private String password ="personal";
    public String getName(){return this.name;}
    public String getPassword(){return this.password;}
    public String toString(){
        return this.getName()+":"+this.getPassword();
    }

    protected void loadStringAAA(){
        log.info("bbbbbbbbbbbbbbbbbbbb");
        super.loadStringAAA();
    }

    public void load(){
        super.load();
    }

    public static void main(String[] args){
        BBB bbb = new BBB();
        bbb.load();
    }
}
