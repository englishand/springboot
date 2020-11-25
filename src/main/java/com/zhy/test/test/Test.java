package com.zhy.test.test;

import com.zhy.test.cache.CacheManagerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Test {

    public static void main(String[] args){

        String pattern = "/login/in";
        String path = "login/in";
        String seprator = "/";

        System.out.println(pattern.endsWith(seprator));
        System.out.println(path.endsWith(seprator));
        System.out.println(pattern.startsWith(seprator,7));
        System.out.println(path.startsWith(seprator,5));
        int a=1;
        for (int i=0;i<10;i++){
            if(a==i) {
                continue;
            }else {
                System.out.println("*****************");
            }
            System.out.println("i="+i);
        }

        Date date1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date2 = sdf.parse("20200918");
            System.out.println(date2.compareTo(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String acc = "2345678.123456789";
        BigDecimal decimal = new BigDecimal(acc).setScale(4,BigDecimal.ROUND_HALF_UP);
        System.out.println(decimal);


        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH,1);
        cal.set(Calendar.DATE,1);
        cal.add(Calendar.DAY_OF_YEAR,-1);
        System.out.println(cal.getTime());


        String substr = "123456";
        System.out.println(substr.substring(4));

        StringBuilder sb = new StringBuilder();
        for (int i=0;i<6;i++){
            Random r = new Random();
            char c = (char) (r.nextInt(10)+'0');
            sb.append(c);
        }
        System.out.println(sb);

        //这里是获取不到的，因为跟spring容器里的factory不是同一个对象
        String name = new CacheManagerFactory().getUserManager().getFromCache("username").toString();
        System.out.println("缓存的名称："+name);
    }



}
