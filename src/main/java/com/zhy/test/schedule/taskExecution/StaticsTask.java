package com.zhy.test.schedule.taskExecution;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StaticsTask {

    @Scheduled(cron = "0/2 * 23 * * ?")
    public static void test(){
        log.info(Thread.currentThread().getName());
        log.info("测试"+ DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
    }
}
