package com.zhy.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Accessors(fluent=true),getter和setter方法名同属性名，且setter方法返回当前对象
 * @Accessors(chain=true),setter方法返回当前对象,用时注意与BindingResult调用的setter方法不一致问题。
 * @Accessors(prefix="f"),setter和getter将忽略属性名的前缀
 */
@Getter
@Setter
@Component
@Accessors(chain = true)
@Slf4j
public class Student {

    private final Logger monitorLog = LoggerFactory.getLogger("monitorLog");

    @Value("${config.username}")
    @NotEmpty(message = "name不能为空")
    private String username;

    @Value("${config.password}")
    @Size(min = 5,max = 10,message = "密码长度在5-10")
    private String password;

    @Value("${config.url}")
    @Email
    private String url;


    /**
     * fluent = true,getter方法
     * @return
     */
    public String username(){
        return username;
    }

    /**
     * fluent = true,setter方法
     * @param username
     * @return
     */
    public Student username(String username){
        this.username = username;
        return this;
    }

    public String toString(){
        return username+"--"+password+"--"+url;
    }
    public String getValueString(){
        return "username:"+username;
    }
    public String getStudentString(){
        monitorLog.info("返回结果为：{}",password);
        return "password:"+password;
    }
    public String testString(){
        return "url:"+url;
    }

    /**
     * PostConstruct 修饰非静态的void方法
     * 该方法会在服务器加载servlet的时候运行，且只被运行一次，在构造方法之后执行，在init之前执行。
     * 执行顺序：Constructor(构造方法)->@Autowired(依赖注入)->@PostConstruct(注释的方法)
     */
    @PostConstruct
    public void init(){
        Student student = this;
    }


    /**
     * 修饰非静态的void方法
     * 当bean在容器销毁前，调用@PreDestory注解的方法。
     */
    @PreDestroy
    public void testPreDestroy(){
        log.info("222222222222222222222");
    }
}
