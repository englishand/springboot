package com.zhy.test.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestAnnotation {

    @Autowired
    private Student student;

    @Test
    public void mains(){
        System.out.println(this.student.username());
        Student student = this.student.username("测试accessors");
        System.out.println(student.username());
    }
}
