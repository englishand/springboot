package com.zhy.test.getObject;

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

    public static User getUserByClass() throws Exception{
        Class aClass = Class.forName("com.zhy.test.getObject.User");
        return (User)aClass.newInstance();
    }

    public static void main(String[] args){
        File file = new File("aa.obj");
//        User user = new User();
        try
        {
//            FileOutputStream fos = new FileOutputStream(file);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            FileInputStream fis = new FileInputStream(file);
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            oos.writeObject(user);
//            User u = (User)ois.readObject();

//            User u = user.clone();

            User u = getUserByClass();

            System.out.println(u.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
