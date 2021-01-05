package com.zhy.test.utils;

import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * httpclient工具类，使用jdk11自带的httpclient
 */
public class AsyncPostUtil {

    public static final String Default_charset ="UTF-8";
    private static HttpClient httpClient;
    private static String url = "http://localhost:8088/projectone/annotation/testRequestBodyWithXml";
    private static String param = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">    <soapenv:Header/>    <soapenv:Body>        <S05800101BMS1001>            <RequestHeader>                <ReqTm>160427</ReqTm>                <ReqDt>20200623</ReqDt>            </RequestHeader>            <RequestBody>                <ORGID>1000</ORGID>                <OWNTASKDESC>登陆</OWNTASKDESC>                <OWN>CEBP</OWN>                <EMAILFLAG></EMAILFLAG>                <OWNREF>172020062342076879</OWNREF>                <BESREF></BESREF>                <ICODE>REQ1001</ICODE>                <MSGS>                    <MSG>    <MSGID>Person01</MSGID>    <DESC>申请人</DESC>    <TYPE>Person</TYPE>    <DATA>        <GENDER></GENDER>        <NAME></NAME>        <ROLE></ROLE>        <DATE>2020-06-23</DATE>        <ADDRESS></ADDRESS>        <COUNTRY></COUNTRY>        <IDS>110101198401010036</IDS>    </DATA>    <VALUETYPE></VALUETYPE>    <VALUE></VALUE>    <TARGET>CEBP-TARGET</TARGET>    <CONFIG>CEBP-config91</CONFIG></MSG>                </MSGS>                <USERID>0000</USERID>                <AUTHFLAG>N</AUTHFLAG>                <SMSFLAG></SMSFLAG>                <TRXREF>172020062342076879</TRXREF>                <AUTHORG>SELF</AUTHORG>                <BRANCHID>1000</BRANCHID>            </RequestBody>        </S05800101BMS1001>    </soapenv:Body></soapenv:Envelope>";
    static {
        httpClient = HttpClient.newHttpClient();
    }

    public String sendPostRequest(long timeout,String charset) throws ExecutionException,InterruptedException {
        String returnMessage ;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(timeout))
                .POST(HttpRequest.BodyPublishers.ofString(param))
                .build();
        charset = StringUtils.isEmpty(charset) ? Default_charset : charset;
//        Thread.currentThread().interrupt();//测试线程中断
        CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture =
                httpClient.sendAsync(request,HttpResponse.BodyHandlers.ofString(Charset.forName(charset)));
        HttpResponse<String> httpResponse =httpResponseCompletableFuture.get();

        returnMessage = httpResponse.body();
        return returnMessage;
    }

    /**
     * Future用来代表异步的结果，并且提供了检查计算完成，等待完成，检索结果完成等方法。简而言之就是提供一个异步运算结果的一个建模。
     * 它可以让我们把耗时的操作从我们本身的调用线程中释放出来，只需要完成后再进行回调。
     *
     * CompletableFuture是JDK8提出的一个支持非阻塞的多功能的Future，同样也是实现了Future接口。
     */
}
