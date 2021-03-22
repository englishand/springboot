package com.zhy.test.utils.socket;

import lombok.extern.slf4j.Slf4j;

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
        int port = 8188;
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
        SocketClient socketClient = new SocketClient();
        socketClient.sendRequest(xml,ip,port);
    }
}
