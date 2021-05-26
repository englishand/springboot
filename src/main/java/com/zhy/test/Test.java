package com.zhy.test;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class Test {

    private static class Node<K,V>{
        int hash;
        K key;
        V value;
        Node<K,V> next;
        Node(int hash,K key,V value,Node<K,V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public static void main(String[] args){
        String sf  = String.format("[%tT] [%s] %s",new Date(),"info","你好");
        log.info(sf);

        String phone = "1234    ";
        try {
            log.info(URLEncoder.encode(phone, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Map m1 = new HashMap();
        m1.put("age",3);m1.put("age",7);
        Map m2 = new HashMap();m2.put("age",5);
        Map m3 = new HashMap();m3.put("age",2);
        Map m4 = new HashMap();m4.put("age",1);
        Map m5 = new HashMap();m5.put("age",6);
        Map m6 = new HashMap();m6.put("age",4);
        List list = new ArrayList();
        list.add(m1);
        list.add(m2);
        list.add(m3);list.add(m4);list.add(m5);list.add(m6);list.add(m1);list.add(m2);
        list.sort(new Comparator<HashMap>() {
            @Override
            public int compare(HashMap o1, HashMap o2) {
                Integer i1 = (Integer) o1.get("age");
                Integer i2 = (Integer) o2.get("age");
                return i1.compareTo(i2);
            }
        });
        log.info(list.toString());


        //也就是说list.size是值实际占用的大小，而其存放元素的容器transient Object[] elementData初始大小是可以通过定义控制的。
        List list1 = new ArrayList(4);
        list1.add(list);
        list1.add("");
        log.info(list1.size()+"");

        int newCapacity = 6;
        newCapacity = newCapacity+(newCapacity >> 1);
        log.info(newCapacity+"");

        List list2 = new LinkedList<String>();
        list2.add("111111");list2.add("222222");list2.add("333333");list2.add("444444");list2.add("5555555");list2.add("666666");
        log.info(list2.get(1).toString());

        Set set = new HashSet();
        boolean result = set.add(1);
        boolean result2 = set.add(1);
        log.info(result+"|"+result2+"|"+set.toString());

        StringBuffer sb = new StringBuffer();
        for (int x=0;x<20;x++){
            Object key = x;
            Object val = x;
            int h;
            int hash = (h = key.hashCode()) ^ (h >>> 16);
            log.info(key.hashCode()+"|"+hash+"|"+(3059181 >>> 16)+"|"+(3059181^(3059181 >>> 16)));

            int n=16;//hashmap的初始值
            int i = (n - 1) & hash;
            sb.append("{"+i+","+x+"}|");
        }
        log.info("hashmap表示为{下标，key}：{}",sb.toString());

        log.info(System.getProperty("user.dir").replace("\\","/")+System.getProperty("line.separator")+"测试");

        String str = "1||3";
        String[] array = str.split("\\|");
        for (int i=0;i<array.length;i++){
            log.info(array[i]+"");
        }

        if (true){
            System.out.println("asdfas");
        }
        else {
            System.out.println("2222222");
        }

        int b=1;
        if (b==1) b += 1;
        else b+=2;
    }
}
