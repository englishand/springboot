package com.zhy.test.test;

import com.zhy.test.cache.CacheManagerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
        System.out.println(pattern.startsWith(seprator,6));
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

        String transactionIdWithNameSpace = String.format("%s%s", new String[] { "UN", "0001" });
        System.out.println(transactionIdWithNameSpace);

        String reqXml = "<Message> <Head> <TransName>NP09</TransName> <TransChannelId>01</TransChannelId> <TransTimestamp></TransTimestamp> </Head> <Body> <DepartmentId>core</DepartmentId> <CifNo>0000000200210343</CifNo> <PayAcctNo>6224254510700148812</PayAcctNo> <PayAcctName>6224254510700148812</PayAcctName> <PayAcctType>D</PayAcctType> <PaperType>1</PaperType> <PaperNo>23010219770310212X</PaperNo> <MerchantSeqNo>OEM20158580430913867</MerchantSeqNo> <MerchantDateTime>20200402131149</MerchantDateTime> <MerchantId>400020150721101709</MerchantId> <SubMerchantId>1</SubMerchantId> <SubMerchantName>1</SubMerchantName> <TransAmount>49.85</TransAmount> <TermCode>1</TermCode> <Remark1>15114608795</Remark1> <Remark2>50.00</Remark2> <MerchantUrl>http://58.61.36.163/wap_new/HebPhoneB2CPay/notify.php</MerchantUrl> <TransType>00</TransType> <Signature>383bcfd7a53b9deb8ecf777ab05bcf8b27e4421333e5f3be17a20a6cceb011de2b120d55ebb84235dd1624d50b98dfbc364edf864d6bb03e392dafb9ddf70543792f026bedcd134dfe36500a6c890a1a8c2b87c2c8f756ec4b01b770a9cdb4e7e880956ce0bb16d7f4fe104392823706022c01e9a87b120a93d2c705a7635ff6</Signature> </Body></Message>";
        reqXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + reqXml;
        String length = new DecimalFormat("00000000").format(reqXml.length());
        reqXml = length + reqXml;
        System.out.println(reqXml);

        Test test = new Test();
        try {
            test.TE();
        } catch (RuntimeException e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }

        String sf = String.format("[%tT] [%s] %s ",new Date(),"info","测试");
        String sf2 = String.format("[%tT] [%s] %s ",new Object[]{new Date(),"info","测试"});
        System.out.println(sf);
        System.out.println(sf2);


        String ab = "2021-02-03";
        System.out.println(ab.replaceAll("-","/"));
    }

    public void TE() throws RuntimeException{
        try{
            int i = 1/0;
        }catch (Exception e){
            throw new RuntimeException("这是测试i捕获的异常。");
        }finally {
            try {
                int j = 1/0;
            }catch (Exception e){
                throw new RuntimeException("这是测试j捕获的异常。");
            }
        }
    }


}
