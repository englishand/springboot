package com.zhy.springbootAutoConfigure;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ZhySelector.class)//通过@Import注解来导入ImportSelector组件
public class ZhyConfig {

}
