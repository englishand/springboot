package com.zhy.springbootAutoConfigure;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

//@ConfigurationProperties(prefix = "spring.datasource")//作用于类，获取配置文件对应的所有属性值
//@Component
@Configuration
public class Properties {

    @ConfigurationProperties(prefix = "spring.datasource")//可以作用于方法也可以作用于类，用法类似
    @Bean(name = "readDataSource")
    public DataSource readDataSource(){
        return new DruidDataSource();
    }
}
