package com.zhy.test.soapTemplate;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Test {

    @org.junit.Test
    public void mains(){

        StringBuilder sb = new StringBuilder();
        System.out.println(SoapTemplateInit.blackList);
    }
}
