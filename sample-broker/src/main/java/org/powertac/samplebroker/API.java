package org.powertac.samplebroker;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
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
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:5000/predict/");
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(data);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response2 = httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
   }
}