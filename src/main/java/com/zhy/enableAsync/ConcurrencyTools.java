package com.zhy.enableAsync;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 并发工具类
 */
public class ConcurrencyTools {

    private static final int THREAD_COUNT=30;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore semaphore = new Semaphore(10,false);
    private static CountDownLatch c = new CountDownLatch(10);//等待10个线程完成
    private static CyclicBarrier barrier = new CyclicBarrier(10);//拦截的线程数
    private static CyclicBarrier barrier2= new CyclicBarrier(10,new CT());
    //假设有10个sheet，则需要10个线程处理
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    //保存每个sheet计算出的结果
    public static ConcurrentHashMap<String,Integer> sheetCount = new ConcurrentHashMap<>();
    private static double count ;
    private static int j=234;

    static class CT extends ConcurrencyTools implements Runnable{
        @Override
        public void run() {
            System.out.println(new Date()+"在所有线程到达屏障时，优先执行 Runnable barrierAction 即 new CT()");
            int result = 0;
            for (Map.Entry<String,Integer> item:sheetCount.entrySet()){
                result += item.getValue();
            }
            sheetCount.put("result",result/10);
        }
    }

    /**
     * 测试并发工具类：Semaphore
     * 在代码中，有30个线程在执行，但只允许10个并发的执行。其构造方法Semaphore(int permits);接收一个整型的数字，表示可用的许可证数量。
     * 构造方法Semaphore(int permits,boolean pair);其中pair是指公平or非公平策略
     * 其他方法：
     *      int availablePermits();返回此信号量中当前可用的许可证数
     *      int getQueueLength();返回正在等待获取许可证的线程数
     *      boolean hasQueuedThreads();是否有线程正在等待获取许可证
     *      void reducePermits(int reduction);减少reduction个许可证，是个protected方法
     *      Collection getQueuedThreads();返回所有等待获取许可证的线程集合，是个protected方法
     */
    public void testSemaphore(){
        for (int i=0;i<THREAD_COUNT;i++){
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();//获取许可
                        //业务代码
                        System.out.println(Thread.currentThread().getName()+"---save data---，当前可用："+semaphore.availablePermits());
                        semaphore.release();//释放许可
                        System.out.println(Thread.currentThread().getName()+"---释放许可---,当前可用："+semaphore.availablePermits());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        threadPool.shutdown();
    }

    //场景：使用join来完成Excel中sheet的解析
    public void TestJoin(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread1 finished");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 finished");
            }
        });
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread3 finished");
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            //可以看到当前线程即main线程等待thread1和thread2执行完毕再执行
            thread1.join();
            thread2.join();
            System.out.println("all thread finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    //场景：使用CountDownLatch,来完成Excel中sheet的解析
    public void testCountDownLatch(){
        Thread[] threads = new Thread[10];
        for (int i=0;i<10;i++){
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!Thread.currentThread().getName().equals("Thread-2")){
                        System.out.println(new Date()+"---"+Thread.currentThread().getName()+" 执行完成");
                    }else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(new Date()+"---"+Thread.currentThread().getName()+" 最后一个线程完成...");
                    }
                    c.countDown();//调用await方法阻塞当前线程，直到计数器为0
                }
            });
            threads[i].start();
        }
        try {
            c.await();
            System.out.println(new Date()+"---"+Thread.currentThread().getName()+"当前线程会被阻塞");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("所有线程解析完成");
    }

    //使用CyclicBarrier举例
    public void testCyclicBarrier(){
        Thread[] threads = new Thread[10];
        for (int i=0;i<10;i++){
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        int j = Integer.parseInt(Thread.currentThread().getName().substring(7))*1000;
                        Thread.sleep(j);
                        System.out.println(new Date()+"---"+Thread.currentThread().getName()+"线程到达屏障");
                        int num = barrier.await();//到达屏障的线程的索引，num-1指到达的第一个线程，0最后到达的一个线程
                        //getParties()返回要求启动此barrier的参与者数量
                        if (num==(barrier.getParties()-1)){
                            System.out.println(new Date()+Thread.currentThread().getName()+"此线程是第一个到达屏障");
                        }else if (num==0){
                            System.out.println(new Date()+Thread.currentThread().getName()+"被唤醒，此线程是最后一个到达屏障,所有线程被唤醒");
                        }
                        System.out.println(new Date()+"执行线程任务"+Thread.currentThread().getName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }
    }

    /**
     *  CyclicBarrier应用场景2：多线程计算数据，最后合并计算的数据。如：一个Excel保存了用户所有银行流水，每个sheet保存一个账户的近一年的每笔银行流水，现要统计用户的日均银行
     *  流水，先用多线程处理每个sheet里的银行流水，执行完之后，再用barrierAction用这些线程的计算结果，计算整个Excel的日均银行流水。
     */
    private void count(){
        System.out.println(new Date());
        for (int i=0;i<10;i++){
            executor.execute(() -> {//使用lambda匿名函数代替 new Runable(){public void run(){}}
                try {
                    ++j;
                    if (Thread.currentThread().getName().equals("pool-3-thread-3")){
                        j=888;
                        sheetCount.put(Thread.currentThread().getName(),j);
                    }else {
                        //模拟个线程计算过程,存储计算结果
                        TimeUnit.SECONDS.sleep(1);
                        sheetCount.put(Thread.currentThread().getName(),j);
                    }

                    //计算完成，插入屏障
                    int num = barrier2.await();//主要来执行barrierAction,执行完后释放线程
                    if (num==0){
                        System.out.println(new Date()+Thread.currentThread().getName()+"线程最后一个到达");
                    }
                    count = sheetCount.get("result");
                    System.out.println(new Date()+"线程"+Thread.currentThread().getName()+"运行结束，最终的计算结果："+count);

                    //结合CountDownLatch,阻塞当前线程线程和主线程，直到所有线程执行完成
                    c.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            c.await();//阻塞当前线程和主线程，因为Cyclic不能阻塞主线程，导致结果count先返回
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }


    public static void main(String[] args){
        ConcurrencyTools tools = new ConcurrencyTools();
//        tools.testSemaphore();//测试Semaphore
//        tools.TestJoin();
        tools.testCountDownLatch();
//        tools.testCyclicBarrier();

//        tools.count();
//        System.out.println(new Date()+"---"+tools.count);

    }

}
