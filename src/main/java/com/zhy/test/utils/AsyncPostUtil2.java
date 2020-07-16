package com.zhy.test.utils;

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
            System.out.println(s);
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

    public String send(){
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        Future<HttpResponse> future=client.execute(post, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {

            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }
        });
        String result="";
        try {
            HttpResponse response = future.get();
            result = EntityUtils.toString(response.getEntity(),"UTF-8");
            System.out.println("result: "+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
