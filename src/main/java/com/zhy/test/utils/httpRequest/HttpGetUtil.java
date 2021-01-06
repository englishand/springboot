package com.zhy.test.utils.httpRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpGetUtil {

    public void sendRequest() throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet get = new HttpGet("url");

        CloseableHttpResponse response = client.execute(get);

        int resultcode = response.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(response.getEntity(),"UTF-8");

        response.close();
        client.close();
    }
}
