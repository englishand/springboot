package com.zhy.proxy.cglibProxy;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 目标类
 */
@Slf4j
public class CglibSon {

    public void gotoHome(Date date){
        log.info("--------已经"+date+",我要回家哈---------");
    }

}
