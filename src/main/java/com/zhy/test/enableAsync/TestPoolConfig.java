package com.zhy.test.enableAsync;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestPoolConfig {

    @Autowired
    private PoolThread poolThread;

    @Test
    public void main() throws Exception{
        try {
            poolThread.one();
            poolThread.two();
            poolThread.three();
            Thread.sleep(3000);//为了让其他线程进行。
//            Thread.currentThread().join();//  阻塞当前调用线程，其他线程调用完后才调用
            System.out.println("主线程方法");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
