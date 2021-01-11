package com.zhy.test.exception.interrupedException;

public class InterrupTest implements Runnable{

    //volatile关键字，当变量被volatile修饰时，会保证修改的值会立即更新至主存，当有其他线程读取该值时，它会从主内存中读取新值。
    private volatile boolean isStoped = false;

    public void run() {
        while (!isStoped){
            System.out.println("in run()---isstop is: "+isStoped);
            try {
                System.out.println("-----------------------");
                if (true){
                    System.out.println("*********************");
                    System.out.println(Thread.currentThread().getName());
                    Boolean a = Thread.currentThread().isInterrupted();
                    System.out.println("in run() "+a);
                    Thread.sleep(2000);
                    System.out.println("in run() woke up");
                }
            }catch (Exception e){
                if (e instanceof InterruptedException){
                    System.out.println(Thread.currentThread().getName());
                    Thread.currentThread().interrupt();
                    Boolean b = Thread.interrupted();
                    boolean c = Thread.interrupted();
                    System.out.println("b "+b);
                    System.out.println("c "+c);
                }
            }
        }
        System.out.println(Thread.currentThread().getName()+"：线程关闭");
    }

    public void stop(){
        isStoped = true;
    }

    public static void main(String[] args){
        InterrupTest it = new InterrupTest();
        Thread thread = new Thread(it);
        thread.start();
        System.out.println(thread.getName());
        try{
            thread.sleep(5000);
            System.out.println("阻塞释放");

            //停止线程方法
//            System.out.println("in main()-- stop other thread");
//            it.stop();

            System.out.println(it.isStoped);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());
        System.out.println("in main() --interrupting other thread");
        thread.interrupt();

        System.out.println("in main() --leaving");
        System.out.println("in main()--"+it.isStoped);
    }
}
