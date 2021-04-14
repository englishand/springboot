package com.zhy.schedule.taskExecution;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 基于接口SchedulingConfigurer的动态定时任务
 * 此类方法实现SchedulingConfigurer类，采用多线程方式跑定时任务，所以模拟两个实现类StaticsTask.java和StaticsTask2.java
 * 配置多线程运行
 * 可以使任务分配给不同的线程执行，例：StaticsTask
 */
public abstract class ScheduleConfig implements SchedulingConfigurer {

    private Logger logger = LoggerFactory.getLogger(ScheduleConfig.class);
    private String cron;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        //参数传入一个size为5的线程池
        taskRegistrar.setScheduler(taskScheduler());
        logger.info((taskScheduler() instanceof ScheduledExecutorService) +"");
        taskRegistrar.addTriggerTask(
                ()->{proccessTask();},
                //设置触发器
                triggerContext -> {
                    //初始化定时任务周期
                    if (StringUtils.isEmpty(cron)){
                        cron = getCron();
                    }
                    CronTrigger trigger = new CronTrigger(cron);
                    return trigger.nextExecutionTime(triggerContext);
                }
        );
    }

    //设置TaskScheduler用于注册计划任务
    public Executor taskScheduler(){
        //设置线程名称
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("schedule-pool-%d").build();
        //创建线程池
        return Executors.newScheduledThreadPool(5,namedThreadFactory);
    }

    //任务的处理函数，由派生类根据业务需要来实现
    protected abstract void proccessTask();

    //获取定时任务周期表达式，由派生类实现
    protected abstract String getCron();
}
