package com.zhy.test.utils.httpRequest;

import com.zhy.test.utils.SystemLogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionSendPostUtil {

    SystemLogUtil log = new SystemLogUtil();
    OutputStreamWriter out = null;
    BufferedReader br = null;

    public String sendPost(String path ,String xml) throws Exception{
        try {
            //创建指定连接的url对象
            URL url = new URL(path);
            //建立到url对象之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //如果使用url连接进行输出，使用dooutput的标志设置为true
            conn.setDoOutput(true);
            conn.setRequestProperty("connection","Keep-Alive");
            //设置请求的内容，不被存储
            conn.setRequestProperty("Cache-Control","no-cache");
            conn.setRequestProperty("Content-Type","application/json;charset=GB18030");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;DigExt)");

            conn.setRequestProperty("Content-Length",xml.length()+"");
            //构造写入数据的输出流
            out  = new OutputStreamWriter(conn.getOutputStream());
            //写入数据
            out.write(new String(xml.getBytes()));
            out.flush();

            if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                //将返回信息读取到内存中
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"GB18030"));
            }else {
                //将返回的错误信息读取到内从
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"GB18030"));
            }
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line=br.readLine())!=null){
                sb.append(getUTF8StringFromGBKString(line)  );
            }
            return sb.toString();
        }catch (Exception e){
            log.error("发送报文信息异常","error.log",e);
            throw e;
        }finally {
            try {
                if (br!=null){
                    br.close();
                }
                if (out!=null){
                    out.close();
                }
            }catch (Exception e){
                log.error("关闭流错误","error.log",e);
            }
        }
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

}
