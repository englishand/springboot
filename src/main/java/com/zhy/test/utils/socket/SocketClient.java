package com.zhy.test.utils.socket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.Socket;

@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SocketClient {

    public void sendRequest(String xml,String ip,int port){
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            Socket socket = new Socket(ip,port);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] content = xml.getBytes();
            String tmp = ("00000000"+String.valueOf(content.length));
            String length = tmp.substring(tmp.length()-8);
            baos.write(length.getBytes());
            baos.write(content);

            try {
                writeStream(socket.getOutputStream(),baos.toByteArray());
                InputStream is = socket.getInputStream();
                Object result = readStream(is);

                log.info("返回数据为：{}",result.toString());
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }finally {
                if (socket!=null){
                    socket.close();
                }
            }
        } catch (IOException e) {
            log.error("Socket 通信异常",e);
        }
    }

    protected static void writeStream(OutputStream out,byte[] sndBuffer) throws IOException{
        out.write(sndBuffer);
        out.flush();
    }

    protected static String readStream(InputStream input ) throws IOException, InterruptedException {
        try {
            int available = 0;
            while (available==0){
                available = input.available();
            }
            byte[] resultBuffer = new byte[available];
            int offset=0;
            int readLength = input.read(resultBuffer,offset,resultBuffer.length);
            if (readLength < 0) {
                throw new RuntimeException("invalid_packet_data");
            }
            String recvStr = new String(resultBuffer,"UTF-8");
//        return recvStr.getBytes();
            return recvStr;
        }catch (Exception e){
            throw e;
        }
    }

//    @Test
    public static void main(String[] args){
        String ip = "127.0.0.1";
        int port = 9999;
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">    <soapenv:Header/>    <soapenv:Body>        <S05800101BMS1001>            <RequestHeader>                <ReqTm>160427</ReqTm>                <ReqDt>20200623</ReqDt>            </RequestHeader>            <RequestBody>                <ORGID>1000</ORGID>                <OWNTASKDESC>登陆</OWNTASKDESC>                <OWN>CEBP</OWN>                <EMAILFLAG></EMAILFLAG>                <OWNREF>172020062342076879</OWNREF>                <BESREF></BESREF>                <ICODE>REQ1001</ICODE>                <MSGS>                    <MSG>    <MSGID>Person01</MSGID>    <DESC>申请人</DESC>    <TYPE>Person</TYPE>    <DATA>        <GENDER></GENDER>        <NAME></NAME>        <ROLE></ROLE>        <DATE>2020-06-23</DATE>        <ADDRESS></ADDRESS>        <COUNTRY></COUNTRY>        <IDS>110101198401010036</IDS>    </DATA>    <VALUETYPE></VALUETYPE>    <VALUE></VALUE>    <TARGET>CEBP-TARGET</TARGET>    <CONFIG>CEBP-config91</CONFIG></MSG>                </MSGS>                <USERID>0000</USERID>                <AUTHFLAG>N</AUTHFLAG>                <SMSFLAG></SMSFLAG>                <TRXREF>172020062342076879</TRXREF>                <AUTHORG>SELF</AUTHORG>                <BRANCHID>1000</BRANCHID>            </RequestBody>        </S05800101BMS1001>    </soapenv:Body></soapenv:Envelope>";
        SocketClient socketClient = new SocketClient();
        socketClient.sendRequest(xml,ip,port);
    }
}
