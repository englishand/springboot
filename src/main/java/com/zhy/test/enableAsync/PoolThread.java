package com.zhy.test.enableAsync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Slf4j
@Service
public class PoolThread {


    /**
     *
     * @throws Exception
     */
    @Async("poolexecutor")//指定线程池名
    public void one() throws Exception{
        log.info("测试一");
        Long start = System.currentTimeMillis();
        Thread.sleep(100);
        Long end = System.currentTimeMillis();
        log.info("用时: "+(end-start)+"毫秒");
    }

    @Async("poolexecutor")
    public void two() throws Exception{
        log.info("测试2");
        Long start = System.currentTimeMillis();
//        Thread.sleep(1000);
        Long end = System.currentTimeMillis();
        log.info("用时："+(end-start)+"毫秒");
    }

    @Async("poolexecutor")
    public void three() throws Exception{
        log.info("测试3");
        Long start = System.currentTimeMillis();
//        Thread.sleep(200);
        Long end = System.currentTimeMillis();
        log.info("用时："+(end-start)+"毫秒");
        System.out.println("是否执行*************");
    }
}
