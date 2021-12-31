package com.zhy.test;

public class ThreadSwitchTest {


    private static String mData;//数据副本
    public ThreadSwitchTest(){
        initSwitchThread();
    }
    //发送数据，供外部调用
    public void sendData(String data){
        //拷贝数据
        mData = data;
    }
    private void initSwitchThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (null!=mData && mData.length()>0){
                        System.out.println("process data,run in thread:"+Thread.currentThread().getName()+",data="+mData);
                        //数据发送后，清除数据副本mdata
                        mData = null;
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"ThreadB").start();
    }

    //建立线程A,在A中调用上面实例的数据拷贝方法sendData()将数据切换到线程B中处理
    public static void main(String[] args) {
        ThreadSwitchTest threadSwitchTest = new ThreadSwitchTest();
        Thread threadA = new Thread(new Runnable() {
            @Override
            public void run() {
                //向线程A发送数据
                String data = "192.168.110.182";
                System.out.println("send data,run in thread:"+Thread.currentThread().getName()+",data="+data);
                //通过ThreadSwitchTest将数据拷贝到线程B中
                threadSwitchTest.sendData(data);
            }
        },"ThreadA");
        threadA.start();
    }


}
