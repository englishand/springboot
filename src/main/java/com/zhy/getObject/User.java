package com.zhy.getObject;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * super.clone();需要实现Cloneable接口，实现对象的克隆。如果不重写clone()方法，则在调用clone()方法实现的是浅复制（
 * 即引用的对象保持不变，原对象发生改变会直接影响到复制对象）。
 * 重写clone()方法,先调用super.clone()进行浅复制，然后在复制那些用到的易变的对象属性，从而达到深复制的效果。
 */
@Slf4j
public class User implements Serializable,Cloneable{

    private static Logger logger = LoggerFactory.getLogger(User.class);

    private byte[] a = {1,2,3,4};
    private byte[] b = {5,6,7,8};

    public String getName(){
        return "测试反序列化对象获取";
    }

    //浅复制
    public User lowClone(){
        User user = null;
        try{
            user = (User)super.clone();//浅复制
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    //深复制
    @Override
    public User clone(){
        User user = null;
        try {
            user = (User) super.clone();
            user.a = this.a.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User getUserByClass() throws Exception{
        Class aClass = Class.forName("com.zhy.getObject.User");
        return (User)aClass.newInstance();
    }

    public static void main(String[] args){
        File file = new File("user.obj");
        User u = null;
        User user = new User();
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            oos.writeObject(user);
            u = (User)ois.readObject();
            u = getUserByClass();
            log.info(u.getName());

            //测试深浅复制
            User cloneuser = user.clone();//a为深复制，b为浅复制
            logger.info("original.a==cloned.a:"+(user.a==cloneuser.a));
            user.a[0] = 99;
            logger.info("original.a:{},cloned.a:{}",user.a,cloneuser.a);

            logger.info("original.b==cloned.b:"+(user.b==cloneuser.b));
            user.b[0] = 99;
            logger.info("original.b==cloned.b:{}",(user.b));

            //每层clone()都顺着 super.clone() 的链向上调用的话最终就会来到Object.clone() ，于是根据上述的特殊语义就可以有 x.clone.getClass() == x.getClass() 。
            logger.info("User.class==cloned.User.class:"+(user.getClass()==cloneuser.getClass()));//true
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
