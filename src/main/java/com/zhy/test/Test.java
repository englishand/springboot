package com.zhy.test;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class Test implements Serializable {

    private int age;
    private transient String name;//测试transient
    public Test(){}
    public Test(int age,String name){
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{"+ "age="+ age+",name='"+name+"\'" +'}';
    }

    public static class TestTransient{
        public static void main(String[] args){
            Test test = new Test(15,"hongyou");
            System.out.println(test);

            try {
                //将对象写入磁盘文件(序列化)
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
                oos.writeObject(test);
                oos.close();

                //从磁盘文件读取test对象（反序列化）
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.txt"));
                Test t = (Test) ois.readObject();
                System.out.println(t);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

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
        List list3 = new LinkedList();
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


        String msgString="1.0DK短信平台系统                  10010000000047202106090000000299772021060900000002997720210609|20210609150805||9000669|DK0020210609000101.txt";
//               msgString="1.0YB短信平台系统                  1001000000005320210609000000031301202106090000000313012021060906801|20210609155300||9000669|20210609_0000031300.txt";
//        msgString= "3.0LC丁香花财富平台                " +
//                "1001000000005620210324083013005061202103240830130050612021032400100";
        String ccc="1.000??俊骞冲?                  1002000000003320210609000000029977202106090000000299772021060920210609151206|00DK20210609000101|92006";
        String ddd="1.000鐭\uE15D俊骞冲彴                  1002000000003320210512183339000846202105121833390008462021051220210512183354|00LC20210512000168|90000";
        System.out.println("ccc:"+ccc.length());
        System.out.println("ddd:"+ddd.length());
        String [] msgarr = msgString.split("\\|");

        byte[] headStr = null;
        try {
            headStr = msgarr[0].getBytes("GBK");
            System.out.println(headStr.length);
            String p2 = subBytes(headStr, 97, headStr.length - 97);
            System.out.println("p2:"+p2);
            String svrName = subBytes(headStr, 5, 30);
            System.out.println("svrName:"+svrName);
            // 报文编码
            String conType = subBytes(headStr, 35, 4);
            System.out.println("conType:"+conType);

            String testByte = "0005060001202107130000037190015030000000000000000000测试电子汇票一                          1219110731366397  00713135541002+00000000000013.00CNY+00000001000847.68025351001045                                               18010000001260844               对方户名                                                              1219110731366397                                                                                                                                                                      ";
            testByte = "0005060001202105061002196910015113123000000000000000转账测试二                              020019411900010  220506172600001+00000000000040.00CNY+00000001238706.00011008001055                            940049002001941190                                                                       16019900167548137                                                                                                                                                                                                      ";
            byte[] strMsgs = testByte.getBytes("GBK");
            System.out.println(strMsgs+" ;长度是："+strMsgs.length);
            String otherPartyCard = subBytes(strMsgs,222,32).trim();
            //对方户名
            String otherPartyAccount = subBytes(strMsgs,254,70).trim();
            // filter
            String filter = subBytes(strMsgs, 324, 182).trim();
            System.out.println("对方账号："+otherPartyCard+";对方户名："+otherPartyAccount+";filler:"+filter+";");
            int a=0 ,aa=0;
            if (a==aa){
                System.out.println("测试if的走向");
            }else if (a==0){
                System.out.println("测试else if的走向");
            }

            String tokens = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIwIiwicGFzc3dvcmQiOiIyMTAyYjU5YTc1YWI4NzYxNmI2MmQwYjk0MzI1NjlkMCIsImV4cCI6MTYyNzI2NzE0NiwiaWF0IjoxNjI3MjYzNTQ2LCJhY2NvdW50IjoiYWRtaW4ifQ.OlpB_cGfINAJJlgYbIsBSwUpHj-WxOnUjYMhbpgLzwA";
            System.out.println("tokens是否是字符序列："+(tokens instanceof CharSequence));
            System.out.println(isBlank((CharSequence)tokens));

            String phones="16645402347";
            String phone2 = "16640622698";
            System.out.println("电话号码校验："+checkPhone(phone2));
            System.out.println("新校验："+PhoneFormatCheckUtils.isChinaPhoneLegal(phones));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //引用类型数组，元素内容为对象
        Test[] a = new Test[]{};
        String[] c = {"aa","bb",""};
        c[0] = "ccc";
        for (int i=0;i<c.length;i++){
            System.out.println(c[i]);
        }

    }
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles){
        boolean flag = false;
        try{
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
    public static boolean checkPhone(String phone){
        String regex = "^(13|14|15|17|18|19)[0-9]{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }
    public static boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16[5,6])|(17[0-8])|(18[0-9])|(19[1、5、8、9]))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }



    public static String subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        String rst = "";
        for (int i=begin;i<begin+count; i++)
        {
            bs[i-begin] = src[i];
        }
        try {
            rst = new String(bs, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rst.trim();
    }
}
