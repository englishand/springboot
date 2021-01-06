package com.zhy.test.utils.httpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpPostUtil {


    public String post(){
        String result = "";
        int code;
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8088/projectone/annotation/getValueString/zhy");

        post.addHeader("charset","UTF-8");
        post.addHeader("Content-Type","text/xml");
        post.setEntity(new StringEntity("xmldata","UTF-8"));
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
            code = response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

}
