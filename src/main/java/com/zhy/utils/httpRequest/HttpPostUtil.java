package com.zhy.utils.httpRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpPostUtil {


    public String post(){
        String result = "";
        int code;
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8088/projectone/annotation/getValueString/zhy");

        post.addHeader("charset","UTF-8");

        post.addHeader("Content-Type","application/x-www-form-urlencoded");
//        post.addHeader("Content-Type","text/xml");
//        post.setEntity(new StringEntity("xmldata这是发送的报文","UTF-8"));

        List<NameValuePair> nvp = new ArrayList<NameValuePair>();
        nvp.add(new BasicNameValuePair("transCode","httpPostUtil"));

        try {
            post.setEntity(new UrlEncodedFormEntity(nvp, HTTP.UTF_8));//使用form表单格式的提交，Content-Type:application/x-www-form-urlencoded
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
            log.info("HttpPostUtil返回的数据："+result);
            code = response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

}
