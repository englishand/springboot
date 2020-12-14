package com.zhy.test.schedule.quartz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class QuartzTest extends QuartzJobBean {

    private String name;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("hello:"+name+","+new Date());
        log.info("测试使用crontrigger触发器");
    }

    /**
     * 测试MethodInvokingJobDetailFactoryBean获取jobDetail方式
     */
    public void doTask(){
        log.info("这是测试MethodInvokingJobDetail获取方式："+ DateFormatUtils.format(System.currentTimeMillis(),"yyyyMMdd HH:mm:ss"));
    }


    public void setName(String name){
        this.name = name;
    }
}
