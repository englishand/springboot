package com.zhy.test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = {"com.zhy.test.dao","com.zhy.test.mapping"})
@EnableScheduling
@SpringBootApplication
public class Projectstart {

    public static void main(String[] args){
        SpringApplication.run(Projectstart.class,args);
    }

}
