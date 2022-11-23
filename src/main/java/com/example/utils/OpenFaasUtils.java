package com.example.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName OpenfaasUtil
 * @Author PanWZ
 * @Data 2022/11/8 - 17:04
 * @Version: 1.8
 */
public class OpenFaasUtils {

    private static final Logger log = LoggerFactory.getLogger(OpenFaasUtils.class);

    /* ServiceComposition */

    /**
     * @Description: Choose the nearest catStation
     *                ：0,22
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestDistancePath1(int startStation, int endStation) {
        log.info("***openfaas request start! -> ServiceComposition -> ShortestDistance***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-servicecomp";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: ShortestTime
     *               ：W1,U1,1,1,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestTimePath1(String startStation, String endStation, int droneId
            , int carId, int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ServiceComposition -> ShortestTime***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-servicecomp-t";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: ShortestEnergy
     *               ：W1,U1,0,0,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestEnergyPath1(String startStation, String endStation, int droneId
            , int carId, int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ServiceComposition -> ShortestEnergy***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-servicecomp-e";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: energy consumption under deadline constrain
     *               ：W1,U1,0,0,30,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestEnergyInTimePath1(String startStation, String endStation, int droneId
            , int carId, int deadline , int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ServiceComposition -> ShortestEnergyInTime***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-servicecomp-et";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + deadline + "," + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");

        return s;
    }

    /* ResourceAllocation */

    /**
     * @Description: Choose the nearest catStation
     *               ：0,22
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestDistancePath2(int startStation, int endStation) {
        log.info("***openfaas request start! -> ResourceAllocation -> ShortestDistance***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-serviceallo";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: getShortestTim
     *               ：W1,U1,1,1,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestTimePath2(String startStation, String endStation, int droneId
            , int carId, int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ResourceAllocation -> ShortestTime***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-serviceallo-t";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: ShortestEnergy
     *               ：W1,U1,0,0,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestEnergyPath2(String startStation, String endStation, int droneId
            , int carId, int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ResourceAllocation -> ShortestEnergy***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-serviceallo-e";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /**
     * @Description: energy consumption under deadline constrain
     *               ：W1,U1,0,0,30,1000,1265 897 2165 4444 3730
     * @author pwz
     * @date 2022/11/10 15:36
     */
    public static String getShortestEnergyInTimePath2(String startStation, String endStation, int droneId
            , int carId, int deadline , int weight, String carToUserDistance) {
        log.info("***openfaas request start! -> ResourceAllocation -> ShortestEnergyInTime***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-serviceallo-et";
        log.info("request -> openfaas: " + url);
        String body = startStation + "," + endStation + "," + droneId + "," + carId + ","
                + deadline + "," + weight + "," + carToUserDistance;
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        log.info("*******openfaas request end!*******");
        return s;
    }

    /* SecurityService */

    public static double[] geoDp(double[] location) {
        log.info("***openfaas request start! -> SecurityService -> geoDp***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-security-gi";
        log.info("request -> openfaas: " + url);
        String body = location[0] + "," + location[1];
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        String[] split = s.split(",");
        log.info("*******openfaas request end!*******");
        return new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
    }

    public static double[] geoDpEnhance(double[] location) {
        log.info("***openfaas request start! -> SecurityService -> geoDpEnhance***");
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://192.168.50.253:31112/function/faas-security-egi";
        log.info("request -> openfaas: " + url);
        String body = location[0] + "," + location[1];
        HttpEntity<String> entity = new HttpEntity<>(body, null);
        log.info("request body: " + body);
        String s = restTemplate.postForObject(url, entity, String.class);
        log.info("request successful! response body: " + s);
        String[] split = s.split(",");
        log.info("*******openfaas request end!*******");
        return new double[]{Double.parseDouble(split[0]), Double.parseDouble(split[1])};
    }
}
