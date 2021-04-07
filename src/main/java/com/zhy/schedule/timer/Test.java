package com.zhy.schedule.timer;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Test {

    public static void main(String[] args){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("定时任务测试处理******************");
                System.out.println(DateFormatUtils.format(this.scheduledExecutionTime(),"yyyy-MM-dd HH:mm:ss"));
//                this.cancel();
            }
        };
        System.out.println(DateFormatUtils.format(new Date(),"HH:mm:ss"));
        timer.schedule(task,1000,2*1000);





    }
}
