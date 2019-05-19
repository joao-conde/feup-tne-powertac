package org.powertac.samplebroker;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class API {

    private static API api;

    public static API getInstance() {
        if (api == null)
            api = new API();
        return api;
    }

    public static String getPrediction(String[] data) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://localhost:5000/predict/");
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(data);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
    
            httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
   }
}