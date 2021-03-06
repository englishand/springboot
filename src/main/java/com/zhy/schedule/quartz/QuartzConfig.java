package com.zhy.schedule.quartz;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobDataMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

//@Configuration
@Slf4j
public class QuartzConfig {

    /**
     * 第一种创建jobDetail方式
     * 创建jobDetailFacToryBean，设置目标类和方法，但不支持传参
     * @return jobDetail
     */
    @Bean
    MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean(){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetBeanName("quartzTest");
        bean.setTargetMethod("doTask");
        return bean;
    }

    /**
     * 第二种创建jobDetail方式
     *  使用JobDetailFactoryBean获取jobDetail,任务类继承QuartzJobBean,
     *  可以使用JobDataMap传递参数，任务类中只需要提供参数的名和set方法即可。
     * @return jobDetail
     */
    @Bean
    JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        JobDataMap map = new JobDataMap();
        map.put("name","zhy");
        bean.setJobDataMap(map);
        bean.setJobClass(QuartzTest.class);
        return bean;
    }

    /**
     * 第一种触发器：一次性的调度（仅是安排单独的任务在指定的时间及时执行）或在指定的时间激活并多次执行任务
     * @return
     */
    @Bean
    SimpleTriggerFactoryBean simpleTriggerFactoryBean(){
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());
        Date date = new Date();
        log.info("当前时间为：{}", DateFormatUtils.format(date,"yyyyMMdd HH:mm:ss"));
        bean.setStartTime(date);
        bean.setStartDelay(2000);
        bean.setRepeatInterval(3000);
        bean.setRepeatCount(3);
        return bean;
    }

    /**
     * 第二种触发器：要执行的任务是基于日期的，并按照cron规则。
     * @return
     */
    @Bean
    CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(jobDetailFactoryBean().getObject());
        bean.setCronExpression("0/2 * 23 * * ?");
        return bean;
    }

    /**
     * 将trigger配置到ScheduleFactory,用于启动定时器（容器启动即触发）
     * @return
     */
//    @Bean
    SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean();

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("quartz-pool-%d").build();
        Executor executor = Executors.newScheduledThreadPool(5,factory);
        bean.setTaskExecutor(executor);

        bean.setTriggers(simpleTriggerFactoryBean().getObject() ,cronTriggerFactoryBean().getObject());
        return bean;
    }
}
