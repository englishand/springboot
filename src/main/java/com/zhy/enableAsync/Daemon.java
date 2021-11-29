package com.zhy.enableAsync;

import java.util.Set;

/**
 * 此类为验证守护线程和非守护线程是否能影响JVM的退出
 */
public class Daemon {

    //辅助方法：线程的组、是否为守护线程、以及对应的优先级
    private static void dumpAllThreadsInfo(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread:threadSet){
            System.out.println("thread name:"+thread.getName()+"|group:"+thread.getThreadGroup()+
                    "|isDaemon:"+thread.isDaemon()+"|priority:"+thread.getPriority());
        }
    }

    //验证普通的（非守护线程）线程会影响进程（JVM）退出
    private static void testNormalThread(){
        long startTime = System.currentTimeMillis();
        new Thread("normalThread"){
            @Override
            public void run() {
                super.run();
                //保持睡眠，确保在执行dumpAllThreadsInfo时，不会因为该线程退出导致dumpAllThreadsInfo无法继续执行
                makeThraedSleep(5000);
                System.out.println("nomalThread.time cost :"+(System.currentTimeMillis()-startTime));
            }
        }.start();

        //主线程睡眠3秒，确保子线程执行完成
        makeThraedSleep(3000);

        dumpAllThreadsInfo();
        System.out.println("mainThread.time cost "+(System.currentTimeMillis()-startTime));
    }

    //验证JVM不等待守护线程就会结束
    private static void testDaemonThread(){
        long startTime = System.currentTimeMillis();
        Thread daemonThreadSetByUser = new Thread("daemonThreadSetByUser"){
            @Override
            public void run() {
                super.run();
                makeThraedSleep(5000);
                System.out.println("daemonThreadSetByUser.time cost :"+(System.currentTimeMillis()-startTime));
            }
        };
        daemonThreadSetByUser.setDaemon(true);
        daemonThreadSetByUser.start();

        //主线程睡眠3秒
        makeThraedSleep(3000);
        dumpAllThreadsInfo();
        System.out.println("mainThread.time cost :"+(System.currentTimeMillis()-startTime));
    }

    //线程睡眠
    private static void makeThraedSleep(long millSeconds){
        try {
            Thread.sleep(millSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Daemon.testNormalThread();
        Daemon.testDaemonThread();
    }
}
