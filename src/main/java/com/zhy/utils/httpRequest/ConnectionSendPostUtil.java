package com.zhy.utils.httpRequest;

import com.zhy.utils.SystemLogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionSendPostUtil {

    private static SystemLogUtil log = new SystemLogUtil();
    OutputStreamWriter out = null;
    BufferedReader br = null;
    private static String RECEIVEDLOG = "received.log";

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
                //将返回的错误信息读取到内存中
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

    public static void main(String[] args){
        ConnectionSendPostUtil postUtil = new ConnectionSendPostUtil();
        String path = "130.1.12.152:8010";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cqr=\"www.cqrcb.com.cn\">\n" +
                "  <soapenv:Header/>\n" +
                "  <soapenv:Body>\n" +
                "    <cqr:S006002990000001>\n" +
                "      <RequestHeader>\n" +
                "        <VerNo>1.1</VerNo>\n" +
                "        <ReqSysCd>026001</ReqSysCd>\n" +
                "        <ReqSecCd>026001</ReqSecCd>\n" +
                "        <TxnTyp>RQ</TxnTyp>\n" +
                "        <TxnMod>0</TxnMod>\n" +
                "        <TxnCd>0</TxnCd>\n" +
                "        <TxnNme/>\n" +
                "        <ReqDt>20210308</ReqDt>\n" +
                "        <ReqTm>2021-03-0816:34:14</ReqTm>\n" +
                "        <AutoFlag/>\n" +
                "        <Flag1>0</Flag1>\n" +
                "        <Flag4>5</Flag4>\n" +
                "        <ReqSeqNo>W02021030886969117</ReqSeqNo>\n" +
                "        <ChnlNo>W0</ChnlNo>\n" +
                "        <BrchNo>0100</BrchNo>\n" +
                "        <TrmNo>000</TrmNo>\n" +
                "        <TlrNo/>\n" +
                "        <HMac>3D3121DE740471C1570C6297FF4AAEEF</HMac>\n" +
                "        <OldAcctFlag>0</OldAcctFlag>\n" +
                "        <UUID>07520210308001634148696911700000</UUID>\n" +
                "      </RequestHeader>\n" +
                "      <RequestBody>\n" +
                "        <SndSysNo>71</SndSysNo>\n" +
                "        <App>短信平台系统</App>\n" +
                "        <MsgID>PB186969117</MsgID>\n" +
                "        <MsgRefID>PB186969117</MsgRefID>\n" +
                "        <WorkDt>20210308</WorkDt>\n" +
                "        <MsgSndTm>20210308163414</MsgSndTm>\n" +
                "        <Oper>0690</Oper>\n" +
                "        <SmsMsg>508715（手机短信验证码，5分钟内有效），请勿将动态密码告知他人并确认交易网址为合法的哈尔滨银行网址。</SmsMsg>\n" +
                "        <RecdNum/>\n" +
                "        <request>\n" +
                "          <row>\n" +
                "            <SmsNo>15845600189</SmsNo>\n" +
                "          </row>\n" +
                "          <row>\n" +
                "            <SmsNo/>\n" +
                "          </row>\n" +
                "        </request>\n" +
                "      </RequestBody>\n" +
                "    </cqr:S006002990000001>\n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        try {

            String result = postUtil.sendPost(path,xml);
            log.info(result,RECEIVEDLOG);
        } catch (Exception e) {
            e.printStackTrace();
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
