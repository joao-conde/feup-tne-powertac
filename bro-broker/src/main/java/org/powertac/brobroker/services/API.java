package org.powertac.brobroker.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.powertac.brobroker.domain.PartialCleared;
import org.powertac.brobroker.domain.PredictionResponse;
import org.powertac.brobroker.domain.PredictionKey;
import org.powertac.brobroker.repos.ClearedFuturesRepo;
import org.powertac.brobroker.repos.ClearedRepo;
import org.powertac.brobroker.repos.WeatherForecastRepo;
import org.powertac.brobroker.repos.WeatherReportRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.google.gson.Gson;

@Service
public class API {

    private WeatherForecastRepo weatherForecastRepo = new WeatherForecastRepo();

    private WeatherReportRepo weatherReportRepo = new WeatherReportRepo();

    private ClearedRepo clearedRepo = new ClearedRepo();

    private ClearedFuturesRepo clearedFuturesRepo = new ClearedFuturesRepo();

    private Gson gson = new Gson();

    private PredictionResponse getPrediction(Integer timeslot, String url) {
        String data = buildPredictionData(timeslot-1);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(data);
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            String prediction = new BasicResponseHandler().handleResponse(response);
            PredictionResponse predictionResponse = gson.fromJson(prediction, PredictionResponse.class);
            return predictionResponse;
        } catch (Exception e) {
            return new PredictionResponse();
        }
    }

    /**
     * 
     * @param i Timeslot
     * @return
     */
    private String buildPredictionData(Integer i) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"data\":[[");
        sb.append(i % 24 + ",");
        sb.append(i % 168 + ",");
        for (int j = 24; j > 0; j--) {
            sb.append(clearedFuturesRepo.findById(i - j).getQuantity().toString() + ",");
            sb.append(clearedFuturesRepo.findById(i - j).getMeanPrice().toString() + ",");
            sb.append(weatherReportRepo.findById(i - j).getTemperature() + ",");
            sb.append(weatherReportRepo.findById(i - j).getWindSpeed() + ",");
        }
        sb.append(weatherReportRepo.findById(i).getTemperature() + ",");
        sb.append(weatherReportRepo.findById(i).getWindSpeed() + ",");
        ArrayList<PartialCleared> partialCleared = clearedRepo.findById(i-1).getFutureCleared();
        for (int k = 0; k < 24; k++) {
            sb.append(partialCleared.get(k).getQuantity() + ",");
            sb.append(partialCleared.get(k).getMeanPrice() + ",");
        }

        for (int j = 1; j <= 24; j++) {
            sb.append(weatherForecastRepo.findById(new PredictionKey(i, i + j)).getTemperature() + ",");
            if (j < 24) {
                sb.append(weatherForecastRepo.findById(new PredictionKey(i, i + j)).getWindSpeed() + ",");
            } else {
                sb.append(weatherForecastRepo.findById(new PredictionKey(i, i + j)).getWindSpeed());
            }
        }
        sb.append("]]}");
        return sb.toString();
    }

	public ArrayList<Double> predictAmounts(int currentTimeslot) {
        return getPrediction(currentTimeslot, "http://localhost:5000/predict/energy").getArray();
	}

	public ArrayList<Double> predictPrices(int currentTimeslot) {
        return getPrediction(currentTimeslot, "http://localhost:5000/predict/price").getArray();
	}
}