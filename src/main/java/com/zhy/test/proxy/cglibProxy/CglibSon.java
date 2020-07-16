package com.zhy.test.proxy.cglibProxy;

public class CglibSon {

    public CglibSon(){

    }
    public void gotoHome(){
        System.out.println("--------gotoHome---------");
    }
    public void gotoSchool(){
        System.out.println("--------gotoSchool----------");
    }
    public void onday(){
        gotoHome();
        gotoSchool();
    }
    public final void onedayFinal(){
        gotoHome();
        gotoSchool();
    }
}
