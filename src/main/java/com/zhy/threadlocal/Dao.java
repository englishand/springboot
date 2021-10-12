package com.zhy.threadlocal;

import com.zhy.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * ThreadLocal<T>其实是与线程绑定的一个变量。ThreadLocal和Synchronized都用于解决多线程并发访问。
 * 区别：Synchronized用于线程间的数据共享，而ThreadLocal用于线程间的数据隔离。Synchronized是利用锁机制，使变量和代码块
 * 在某一时刻只能被一个线程访问，实现多线程通讯时获得数据共享。而ThreadLocal为每一个线程提供了变量的副本，使每个线程在某一时间访问到的不是同一个对象。
 */
@Slf4j
public class Dao implements Runnable{

    //使用threadlocal保存User变量
    static ThreadLocal<User> userThreadLocal = new ThreadLocal<User>();
    private static List<User> users = new ArrayList<>(10);

    public static User getUser(User user){
//        if (userThreadLocal.get() == null){
//            userThreadLocal.set(user);
//            return user;
//        }else {
//            return userThreadLocal.get();
//        }
        if (users.size()==0 || users.get(0) == null){
            users.add(user);
            return user;
        }else {
            return users.get(0);
        }
    }

//    @Override
//    public void run() {
//        String threadName = Thread.currentThread().getName();
//        User user = new User();
//        if (threadName.contains("1")){
//            user.setUsername("zhy");
//            user.setId("123456");
//        }else {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        user  = getUser(user);
//        log.info(threadName+"-"+user.getId()+"-"+user.getUsername());
//    }

    @Override
    public synchronized void  run(){
        String threadName = Thread.currentThread().getName();
        User user = new User();
        if (threadName.contains("0")){
            user.setUsername("zhy");
            user.setId("123456");
        }else {
            System.out.println("asdfasfdsa");
        }
        user  = getUser(user);
        log.info(threadName+"-"+user.getId()+"-"+user.getUsername());
    }


    public static void main(String[] args){

        for (int i=0;i<2;i++){
//            Thread thread = new Dao();
            Thread thread = new Thread(new Dao());
            thread.start();


        }
        System.out.println("是否最后执行");
    }

}
