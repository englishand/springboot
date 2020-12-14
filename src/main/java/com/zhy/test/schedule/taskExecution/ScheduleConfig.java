package com.zhy.test.schedule.taskExecution;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 配置多线程运行
 * 可以是任务分配给不同的线程执行，例：StaticsTask
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //参数传入一个size为10的线程池
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}
