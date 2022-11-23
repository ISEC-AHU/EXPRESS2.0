package com.example.utils;


import com.example.bean.Car;
import com.example.bean.Drone;
import com.example.bean.Order;
import com.example.service.*;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/22 16:39
 * @Description:
 * @FileName: RoutePlanning
 */
@Component
public class RoutePlanUtils {

    private static int[] minDis;
    public static RoutePlanUtils routePlanUtils;

    @PostConstruct
    public void init() {
        routePlanUtils = this;
    }

    @Resource
    DroneStationService droneStationService;

    @Resource
    CarStationService carStationService;

    @Resource
    CarToCustomerService carToCustomerService;

    @Resource
    DroneService droneService;

    @Resource
    CarService carService;

    /**
     * @return List<String> : Returns the sites through which the shortest path passes.
     * The last bit of the array is the distance.  : [W1, D7, C3, 2836]
     * @Description: Dijkstra
     * @author pwz
     * @date 2022/9/22 20:11
     */
    public static List<String> getShortestPath(int source, int end) {
        int[][] matrix = GraphUtils.getMatrix();
        List<String> vertex = GraphUtils.getVertex();
        int[] shortest = new int[matrix.length];
        int[] visited = new int[matrix.length];
        String[] path = new String[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            path[i] = vertex.get(source) + ',' + vertex.get(i);
        }
        shortest[source] = 0;
        visited[source] = 1;
        for (int i = 1; i < matrix.length; i++) {
            int min = GraphUtils.maxDis;
            int index = -1;
            for (int j = 0; j < matrix.length; j++) {
                if (visited[j] == 0 && matrix[source][j] < min) {
                    min = matrix[source][j];
                    index = j;
                }
            }
            shortest[index] = min;
            visited[index] = 1;
            for (int m = 0; m < matrix.length; m++) {
                if (visited[m] == 0 && matrix[source][index] + matrix[index][m] < matrix[source][m]) {
                    matrix[source][m] = matrix[source][index] + matrix[index][m];
                    path[m] = path[index] + ',' + vertex.get(m);
                }
            }
            if (visited[end] == 1) break;
        }
        String[] split = path[end].split(",");
        List<String> pathAndDistance = new ArrayList<>(Arrays.asList(split));
        pathAndDistance.add(String.valueOf(shortest[end]));
        return pathAndDistance;
    }

    /**
     * @return List<String>
     * @Description: Select the corresponding path planning algorithm according to different optimization objectives
     * The return path passes through the site collection and time consumption
     * @author pwz
     * @date 2022/10/20 15:17
     */
    public static List<String> getRouteByObjective(Order order, String objective, int uavType, int ugvType) {
        Drone drone = routePlanUtils.droneService.getById(uavType);
        Car car = routePlanUtils.carService.getById(ugvType);
        int weigh = (int) (order.getWeight() * 1000);
        List<String> route = null;
        int[] timeAndEnergy = null;
        switch (objective) {
            case "distance":
                // Choose the nearest catStation
                route = getShortestDistanceRoute(order.getStartStation(), order.getConsignee());
                break;
            case "time":
                // ShortestTime
                route = getShortestTimeRoute(order.getStartStation(), order.getConsignee(), drone, car, weigh);
                break;
            case "energy":
                // ShortestEnergy
                route = getShortestEnergyRoute(order.getStartStation(), order.getConsignee(), drone, car, weigh);
                break;
            case "energyInTime":
                // energy consumption under deadline constrain
                route = getShortestEnergyRouteUnderTimeConstraint(order.getStartStation(),
                        order.getConsignee(), drone, car, order.getDeadline() * 60, weigh);
                break;
            default:
                break;
        }
        timeAndEnergy = routePlanUtils.getTimeAndEnergy(route, order.getConsignee(), drone, car, weigh);
        route.remove(route.size() - 1);
        route.add(String.valueOf(timeAndEnergy[0]));
        route.add(String.valueOf(timeAndEnergy[1]));
        return route;
    }

    /**
     * @return List<String>
     * @Description: Choose the nearest catStation
     * @author pwz
     * @date 2022/9/26 17:14
     */
    public static List<String> getShortestDistanceRoute(String startStation, String consignee) {
        int source = GraphUtils.getSequenceByName(startStation);
        int end = routePlanUtils.carToCustomerService.getShortestCarStationNum(consignee);
        List<String> path = RoutePlanUtils.getShortestPath(source, end);
        return path;
    }

    /**
     * @return
     * @Description: ShortestTime
     * @author pwz
     * @date 2022/10/13 16:52
     */
    public static List<String> getShortestTimeRoute(String startStation
            , String consignee, Drone drone, Car car, int weigh) {
        int source = GraphUtils.getSequenceByName(startStation);
        List<Integer> ends = routePlanUtils.carToCustomerService.getAllCarStationByCustomerName(consignee);
        for (int i = 0; i < ends.size(); i++) {
            ends.set(i, (int) (ends.get(i) + routePlanUtils.droneStationService.count() - 1));
        }
        int totalTime = Integer.MAX_VALUE;
        List<String> res = new ArrayList<>();
        for (int i = 0; i < ends.size(); i++) {
            List<String> path = RoutePlanUtils.getShortestPath(source, ends.get(i));
            int[] timeAndEnergy = routePlanUtils.getTimeAndEnergy(path, consignee, drone, car, weigh);
            if (timeAndEnergy[0] < totalTime) {
                totalTime = timeAndEnergy[0];
                res.clear();
                res.addAll(path);
            }
            path.remove(path.size() - 1);
        }
        return res;
    }

    /**
     * @return List<List < Double>>
     * @Description: ShortestEnergy
     * @author pwz
     * @date 2022/10/13 16:53
     */
    public static List<String> getShortestEnergyRoute(String startStation
            , String consignee, Drone drone, Car car, int weigh) {
        int source = GraphUtils.getSequenceByName(startStation);
        List<Integer> ends = routePlanUtils.carToCustomerService.getAllCarStationByCustomerName(consignee);
        for (int i = 0; i < ends.size(); i++) {
            ends.set(i, (int) (ends.get(i) + routePlanUtils.droneStationService.count() - 1));
        }
        int totalEnergy = Integer.MAX_VALUE;
        List<String> res = new ArrayList<>();
        for (int i = 0; i < ends.size(); i++) {
            List<String> path = RoutePlanUtils.getShortestPath(source, ends.get(i));
            int[] timeAndEnergy = routePlanUtils.getTimeAndEnergy(path, consignee, drone, car, weigh);
            if (timeAndEnergy[1] < totalEnergy) {
                totalEnergy = timeAndEnergy[1];
                res.clear();
                res.addAll(path);
            }
            path.remove(path.size() - 1);
        }
        return res;
    }

    /**
     * @return List<List < Double>>
     * @Description: energy consumption under deadline constrain
     * @author pwz
     * @date 2022/10/13 16:53
     */
    public static List<String> getShortestEnergyRouteUnderTimeConstraint(String startStation
            , String consignee, Drone drone, Car car, int time, int weigh) {
        int source = GraphUtils.getSequenceByName(startStation);
        List<Integer> ends = routePlanUtils.carToCustomerService.getAllCarStationByCustomerName(consignee);
        for (int i = 0; i < ends.size(); i++) {
            ends.set(i, (int) (ends.get(i) + routePlanUtils.droneStationService.count() - 1));
        }
        int totalEnergy = Integer.MAX_VALUE;
        List<String> res = new ArrayList<>();
        for (int i = 0; i < ends.size(); i++) {
            List<String> path = RoutePlanUtils.getShortestPath(source, ends.get(i));
            int[] timeAndEnergy = routePlanUtils.getTimeAndEnergy(path, consignee, drone, car, weigh);
            if (timeAndEnergy[1] < totalEnergy && timeAndEnergy[0] < time) {
                totalEnergy = timeAndEnergy[1];
                res.clear();
                res.addAll(path);
            }
            path.remove(path.size() - 1);
        }
        if (res.size() == 0) {
            return null;
        }
        return res;
    }

    /**
     * @return List<String>  ：[W1, D7, C3]
     * @Description: Returns a collection of sites through which the shortest path passes
     * @author pwz
     * @date 2022/9/27 14:44
     */
    public static List<String> getShortestStationName(String startStation, String consignee) {
        int source = GraphUtils.getSequenceByName(startStation);
        int end = routePlanUtils.carToCustomerService.getShortestCarStationNum(consignee);
        List<String> stations = RoutePlanUtils.getShortestPath(source, end);
        stations.remove(stations.size() - 1);
        return stations;
    }

    /**
     * @description: Uniformly convert the result set of path planning into a coordinate set
     * @author: pwz
     * @date: 2022/10/13 22:47
     * @return: java.util.List<java.lang.Double>
     **/
    public List<List<Double>> stationNameToRouteLocation(List<String> stations, int[] timeAndEnergy) {
        JSONArray jsonArray = new JSONArray();
        if (stations == null) {
            jsonArray.add(timeAndEnergy);
            return jsonArray;
        }
        jsonArray.add(stations);
        List<List<Double>> list = new ArrayList<>();
        for (int i = 0; i < stations.size(); i++) {
            char[] chars = stations.get(i).toCharArray();
            List<Double> location;
            if (chars[0] == 'W' || chars[0] == 'D') {
                location = routePlanUtils.droneStationService.getLocationByName(stations.get(i));
            } else {
                location = routePlanUtils.carStationService.getLocationByName(stations.get(i));
            }
            list.add(location);
        }
        jsonArray.add(list);
        jsonArray.add(timeAndEnergy);
        return jsonArray;
    }

    /**
     * @return int[]
     * @Description: Find the total time and total energy consumption
     * @author pwz
     * @date 2022/10/14 12:08
     */
    public static int[] getTimeAndEnergy(List<String> path, String consignee, Drone drone, Car car, int weigh) {
        int preDistance = Integer.parseInt(path.get(path.size() - 1));
        String driveSource = path.get(path.size() - 2);
        int dTime = preDistance / drone.getSpeed();
        int carRouteDistance = GuideRoutePlanUtils.getDistanceOfPlanFromGuide(driveSource, consignee);
        int cTime = carRouteDistance / car.getSpeed();
        int realDronePower = getDronePower(drone, weigh);
        int dEnergy = dTime * realDronePower;
        int realCarPower = getCarPower(car, weigh);
        int cEnergy = cTime * realCarPower;
        int totalTime = dTime + cTime;
        int totalEnergy = cEnergy + dEnergy;
        return new int[]{totalTime, totalEnergy};
    }

    // Uav real-time power consumption
    private static int getDronePower(Drone drone, int weight) {
        return drone.getNoLoadPower()
                + (drone.getMaxPower() - drone.getNoLoadPower()) * weight / drone.getMaxLoad();
    }

    // Real-time power consumption of unmanned vehicles
    private static int getCarPower(Car car, int weight) {
        return car.getNoLoadPower()
                + (car.getMaxPower() - car.getNoLoadPower()) * weight / car.getMaxLoad();
    }

    /**
     * @return int
     * @Description: Load judgment and prompt information
     * @author pwz
     * @date 2022/10/17 15:43
     */
    public static int loadJudge(Drone drone, Car car, int weigh) {
        if (weigh > drone.getMaxLoad()) {
            System.out.println("The weight of the goods exceeds the maximum payload of the UAV/UAV. Please reselect the type of UAV/UAV.");
            return -1;
        } else if (weigh > car.getMaxLoad()) {
            System.out.println("The weight of the goods exceeds the maximum payload of the UAV/UAV. Please reselect the type of UAV/UAV.");
            return -2;
        } else {
            return 1;
        }
    }

    /**
     * @return String
     * @Description: Converts the result of path planning to a string
     * @author pwz
     * @date 2022/10/20 15:43
     */
    public static String pathListToString(List<String> route, String destination) {
        String res = "";
        for (String s : route) {
            res = res + s + "->";
        }
        res += destination;
        return res;
    }
}