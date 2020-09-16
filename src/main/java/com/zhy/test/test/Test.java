package com.zhy.test.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
            System.out.println("a="+a);
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
    }
}
