package com.zhy.schedule.taskExecution;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 基于注解 @Scheduled的简单定时器，不需要继承 ScheduleConfig
 * 注意：执行的该任务会自动寻找已经存在的线程池中的线程来运行。
 */
@Component
@Slf4j
public class SingleScheduleTask {

    @Scheduled(cron = "0/1 * 23 * * ?")
    public static void test(){
        log.info("线程id："+Thread.currentThread().getId()+"|"+Thread.currentThread().getName()+"|"+ DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
    }
}
