package com.zhy.test.getObject;

import lombok.Setter;

import java.io.File;
import java.io.Serializable;

public class User implements Serializable,Cloneable{

    public String getName(){
        return "测试反序列化对象获取";
    }
    public User clone(){
        User user = null;
        try{
            user = (User)super.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public User getUser(){
        User u = new User();
        return u.clone();
    }

    public User getUserByClass() throws Exception{
        Class aClass = Class.forName("com.zhy.test.getObject.User");
        return (User)aClass.newInstance();
    }


    public static void main(String[] args){
        User user = new User();
        File file = new File("aa.obj");
        try
        {
//            FileOutputStream fos = new FileOutputStream(file);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            FileInputStream fis = new FileInputStream(file);
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            oos.writeObject(user);
//            User u = (User)ois.readObject();

//            User u = user.clone();

            User u = user.getUserByClass();

            System.out.println(u.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
