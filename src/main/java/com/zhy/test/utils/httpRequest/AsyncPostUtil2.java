package com.zhy.test.utils.httpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncPostUtil2 {

    private static final Logger logg = LoggerFactory.getLogger(AsyncPostUtil2.class);
    private static final String url = "http://localhost:8088/projectone/annotation/getValueString/zhy";
    private static HttpPost post;
    static {
        post = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("password","123"));
        nvps.add(new BasicNameValuePair("transCode","post2"));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String sendRequest(){
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        logg.info("caller thread id is: "+Thread.currentThread().getId());
        Future<HttpResponse> future = client.execute(post, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                logg.info("callback thread id is: "+ Thread.currentThread().getId());
                logg.info(post.getRequestLine()+"->"+httpResponse.getStatusLine());
                //从这里可以看出，httpResponse 无法获取到返回信息，只能获取连接情况。
                try {
                    String content = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
                    logg.info("response content is: "+content);
                    System.out.println("**********调用结束*********************");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                logg.info(post.getRequestLine()+"->"+e);
                logg.info("callback thread id is: "+Thread.currentThread().getId());
            }

            @Override
            public void cancelled() {
                logg.info(post.getRequestLine()+"cancelled");
                logg.info("callback thread id is: "+Thread.currentThread().getId());
            }
        });

        String s = null;
        try {
            HttpResponse response = future.get();
            HttpEntity entity = response.getEntity();
            s = EntityUtils.toString(entity,"UTF-8");
            logg.info("接收返回信息：{}",s);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

}
