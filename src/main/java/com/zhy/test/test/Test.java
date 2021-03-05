package com.zhy.test.test;

import com.zhy.test.cache.CacheManagerFactory;

import java.io.UnsupportedEncodingException;
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


        String ab = "测试字节2021-02-03";
        System.out.println(ab.replaceAll("-","/"));

        byte[] b  = ab.getBytes();
        String c = "";
        byte[] d = new byte[1];
        for (int i=0;i<b.length;i++){

            if (i==2) {
                try {
                    c = new String(b, "GBK");
                    System.out.println(c);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        String regex = "[^0-9]+";
        String contentNum = reqXml.replaceAll(regex, "").trim();
        System.out.println(contentNum);

        String txt = "账户有未解冻的冻结记录。                                                       1168010027878406                                    |";
        System.out.println(txt.length());
        System.out.println(txt.trim().length());
        int number = txt.length() - txt.trim().length() - 36;
        System.out.println(number);
        contentNum = txt.replaceAll(regex,"").trim();
        System.out.println(contentNum+"/"+contentNum.length());

        System.out.println(System.getProperty("user.dir"));

        String name = "                              ";

        try {
            name = new String("短信平台".getBytes(),"utf-8") + name.substring("短信平台".getBytes("utf-8").length + "短信平台".length());
            System.out.println(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("///"+System.getProperty("line.separator")+"///");;

        System.out.println("XASDF".toLowerCase());
        String version = "1.0";
        System.out.println(String.format("%-9s",version));

        String content = "0000000001123456789012345678900816321000000000000000zhangsan                                1130613165945163 1106151203030025200.67           CNY81588.69          011008001045                            6217520018882876889                                  ";
        byte[] bytes = content.getBytes();
        System.out.println(bytes.length);

        DecimalFormat df = new DecimalFormat("#0.00");
        String transamt = "01238.23";
        System.out.println(df.format(Double.parseDouble(transamt)));

        String tranDate = "0615120303";
        tranDate =tranDate.substring(0, 2).replaceFirst("^0*", "")+"月"+ tranDate.substring(2, 4).replaceFirst("^0*", "")+"日 "+tranDate.substring(4, 6)+":"+tranDate.substring(6, 8)+":"+tranDate.substring(8, 10);
        System.out.println(tranDate);
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
