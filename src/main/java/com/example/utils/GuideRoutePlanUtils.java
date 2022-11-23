package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.service.CarStationService;
import com.example.service.CustomerService;
import com.example.service.DroneStationService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/10/13 15:15
 * @Description:
 * @FileName: GuideRoutePlanUtil
 */
@Component
public class GuideRoutePlanUtils {

    public static GuideRoutePlanUtils guideRoutePlanUtils;

    @PostConstruct
    public void init() {
        guideRoutePlanUtils = this;
    }

    @Resource
    RestTemplate restTemplate;

    @Resource
    CustomerService customerService;

    @Resource
    DroneStationService droneStationService;

    @Resource
    CarStationService carStationService;

    /**
     * @Description: Analyze Autonavi unmanned vehicle path planning results, return the path length/m
     * @author pwz
     * @date 2022/10/13 15:54
     * @return int
     */
//    @Cacheable(value = "getDistanceOfPlanFromGuide")
    public static int getDistanceOfPlanFromGuide(String startStation, String consignee) {
        List<Double> origin = guideRoutePlanUtils.carStationService.getLocationByName(startStation);
        List<Double> des = guideRoutePlanUtils.customerService.getLocationByName(consignee);

        String url = "https://restapi.amap.com/v3/direction/driving?origin=" +
                origin.get(0) + "," + origin.get(1) + "&destination=" +
                des.get(0) + "," + des.get(1) + "&extensions=base&output=json&" +
                "key=db57812ceb3ba9d7f21906ff89e1b933";

//        String forObject = restTemplate.getForObject("https://restapi.amap.com/v3/direction/driving?origin=116.481028,39.989643&destination=116.465302,40.004717&extensions=all&output=json&key=db57812ceb3ba9d7f21906ff89e1b933", String.class);
        String forObject = guideRoutePlanUtils.restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONObject.parseObject(forObject);
        JSONObject route = (JSONObject) jsonObject.get("route");
        List paths = (List) route.get("paths");
        JSONObject path = (JSONObject) paths.get(0);
        int distance = Integer.parseInt((String) path.get("distance"));
        return distance;
    }

    /**
     * @description: Gets the driving distance between the station that can reach the specified user and the user
     * @author: pwz
     * @date: 2022/11/8 17:30
     * @param: [startStation, consignee]
     * @return: int
     **/
    public static String getCarToUserDistance(List<String> startStations, String consignee) {
        List<Double> des = guideRoutePlanUtils.customerService.getLocationByName(consignee);
        String res = "";
        for (String startStation : startStations) {
            List<Double> origin = guideRoutePlanUtils.carStationService.getLocationByName(startStation);
            String url = "https://restapi.amap.com/v3/direction/driving?origin=" +
                    origin.get(0) + "," + origin.get(1) + "&destination=" +
                    des.get(0) + "," + des.get(1) + "&extensions=base&output=json&" +
                    "key=db57812ceb3ba9d7f21906ff89e1b933";

//        String forObject = restTemplate.getForObject("https://restapi.amap.com/v3/direction/driving?origin=116.481028,39.989643&destination=116.465302,40.004717&extensions=all&output=json&key=db57812ceb3ba9d7f21906ff89e1b933", String.class);
            String forObject = guideRoutePlanUtils.restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = JSONObject.parseObject(forObject);
            JSONObject route = (JSONObject) jsonObject.get("route");
            List paths = (List) route.get("paths");
            JSONObject path = (JSONObject) paths.get(0);
            String distance = (String) path.get("distance");
            res = res + " " + distance;
        }
        return res.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(getCarToUserDistance(Arrays.asList("C3", "C6", "C9", "C10", "C11"), "U1"));
    }
}