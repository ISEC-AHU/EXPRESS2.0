package com.example.utils;


import com.example.bean.StationNetMap;
import com.example.service.CarStationService;
import com.example.service.CustomerService;
import com.example.service.DroneStationService;
import com.example.service.StationNetMapService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/20 9:44
 * @Description:
 * @FileName: Graph
 */
@Component
public class GraphUtils {

    public static final int maxDis = 10000;
    private static List<String> vertex;
    private static List<List<StationNetMap>> edges;

    public static GraphUtils graphUtils;

    @PostConstruct
    public void init() {
        graphUtils = this;
    }

    @Resource
    StationNetMapService netMapService;

    @Resource
    DroneStationService droneStationService;

    @Resource
    CarStationService carStationService;

    @Resource
    CustomerService customerService;

    /**
     * @Description: Find the set of vertices (sites) of the graph, corresponding to the set of edges W1..D1..C1..
     * @author pwz
     * @date 2022/9/22 19:39
     * @return java.lang.String[]
     */
    public static List<String> getVertex() {
        List<String> droneStationNames = graphUtils.droneStationService.getNames();
        List<String> carStationNames = graphUtils.carStationService.getNames();
        vertex = new ArrayList<>();
        vertex.addAll(droneStationNames);
        vertex.addAll(carStationNames);
        return vertex;
    }

    /**
     * @Description: Get the adjacency matrix of the map
     * @author pwz
     * @date 2022/9/23 10:31
     * @return int[][] : W1..D1..C1..
     */
    public static int[][] getMatrix() {
        int[][] matrix = graphUtils.netMapService.getMatrix();
        return matrix;
    }

    /**
     * @Description: Returns the order in the matrix according to the site name W1:0、D1:1...
     * @author pwz
     * @date 2022/9/26 16:01
     * @param name
     * @return int
     */
    public static int getSequenceByName(String name) {
        return GraphUtils.getVertex().indexOf(name);
    }

    /**
     * @Description: Returns the site coordinate collection based on the site name collection
     * @author pwz
     * @date 2022/10/21 15:00
     */
    public static List<List<Double>> stationNamesToLocations(List<String> stations) {
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            char[] chars = stations.get(i).toCharArray();
            List<Double> location;
            if (chars[0] == 'W' || chars[0] == 'D') {
                location = graphUtils.droneStationService.getLocationByName(stations.get(i));
            } else if (chars[0] == 'C'){
                location = graphUtils.carStationService.getLocationByName(stations.get(i));
            } else {
                location = graphUtils.customerService.getLocationByName(stations.get(i));
            }
            list.add(location);
        }
        return list;
    }
}