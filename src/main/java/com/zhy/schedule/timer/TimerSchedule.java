package com.zhy.schedule.timer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class TimerSchedule {

    public static void main(String[] args){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                log.info("定时任务测试处理******************");
                log.info(DateFormatUtils.format(this.scheduledExecutionTime(),"yyyy-MM-dd HH:mm:ss"));
//                this.cancel();
            }
        };
        log.info(DateFormatUtils.format(new Date(),"HH:mm:ss"));
        timer.schedule(task,1000,2*1000);





    }
}
