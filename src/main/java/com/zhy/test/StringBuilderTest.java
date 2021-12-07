package com.zhy.test;

import lombok.extern.slf4j.Slf4j;

/**
 * StringBuilder 是一个可以动态增加自身数据长度的类，其默认长度（capacity属性）为16。
 * 它有一个构造函数，可以指定其容器长度。当数据量小时，指定长度意义不大，但是当数据量比较大时，指定长度会对性能产生显著影响。
 */
@Slf4j
public class StringBuilderTest {

    public static void main(String[] args){

        int times = args.length>0?Integer.parseInt(args[0]):100;
        int length = args.length>1?Integer.parseInt(args[1]):73000000;

        log.info(times+"-----"+length);

        Long start = System.currentTimeMillis();
        int sblength =0;
        for (int i=0;i<times;i++){
            sblength = test(length);
        }
        Long end = System.currentTimeMillis();
        log.info(sblength+"");
        //单次运行时间
        System.out.printf("TIME TAKEN: %d ms.\n",(end-start)/times);
    }

    public static int test(int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i=0;i<10000000;i++){
            sb.append(i+"");
        }
        return sb.length();
    }

    /**
     * 100-----0
     * 68888890
     * TIME TAKEN: 285 ms.
     */
    /**
     * 100-----73000000
     * 68888890
     * TIME TAKEN: 253 ms.
     */
}
