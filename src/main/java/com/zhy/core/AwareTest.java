package com.zhy.core;


import com.zhy.core.util.CoreUtil;
import com.zhy.entity.Student;
import com.zhy.schedule.taskExecution.ScheduleConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AwareTest{

    @Autowired
    private Student student;

    @Test
    public void Test(){
        Student student = (Student) CoreUtil.getObject(Student.class);
        log.info(student.toString());

        ScheduleConfig scheduleConfig = (ScheduleConfig) CoreUtil.getObject(ScheduleConfig.class);
        log.info(scheduleConfig.toString());
    }

    @Test
    public void Test2(){
        log.info(this.student.toString());
    }

}
