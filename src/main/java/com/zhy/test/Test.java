package com.zhy.test;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
@Slf4j
public class Test {

    public static void main(String[] args){
        String sf  = String.format("[%tT] [%s] %s",new Date(),"info","你好");
        log.info(sf);

        String aaa = "ABCD";
        log.info(aaa.toLowerCase());

        String phone = "1234    ";
        try {
            log.info(URLEncoder.encode(phone, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
