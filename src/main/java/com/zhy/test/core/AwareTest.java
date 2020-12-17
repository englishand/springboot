package com.zhy.test.core;


import com.zhy.test.core.util.CoreUtil;
import com.zhy.test.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AwareTest{

    @Autowired
    private Student student;

    @Test
    public void Test(){
        Student student = (Student) CoreUtil.getObject(Student.class);
        System.out.println(student.toString());
    }

    @Test
    public void Test2(){
        System.out.println(this.student.toString());
    }

}
