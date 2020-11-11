package com.zhy.test.test;

public class BBB extends AAA{

    private static String name ="zhy";
    private String password ="personal";
    public String getName(){return this.name;}
    public String getPassword(){return this.password;}
    public String toString(){
        return this.getName()+":"+this.getPassword();
    }

    protected void loadStringAAA(){
        System.out.println("bbbbbbbbbbbbbbbbbbbb");
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
