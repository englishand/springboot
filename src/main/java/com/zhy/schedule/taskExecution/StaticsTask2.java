package com.zhy.schedule.taskExecution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class StaticsTask2 extends ScheduleConfig{

    @Override
    protected void proccessTask() {
        log.info("基于接口SchedulingConfigurer的动态定时任务2:"
                + LocalDateTime.now()+"，线程名称："+Thread.currentThread().getName()
                + " 线程id："+Thread.currentThread().getId());
    }

    @Override
    protected String getCron() {
        return "0/1 * 23 * * ?";
    }
}
