package com.zhy.test.proxy.static_proxy;

public class Agent implements Singer{

    private Star star;

    public Agent(Star star){
        //super();
        this.star = star;
    }

    @Override
    public void sing() {
        System.out.println("在歌手唱歌之前收钱....");
        star.sing();
    }
}
