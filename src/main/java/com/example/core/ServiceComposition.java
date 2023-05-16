package com.example.core;

import com.example.bean.Car;
import com.example.bean.Drone;
import com.example.bean.Order;
import com.example.service.CarService;
import com.example.service.CarToCustomerService;
import com.example.service.DroneService;
import com.example.utils.GraphUtils;
import com.example.utils.GuideRoutePlanUtils;
import com.example.utils.OpenFaasUtils;
import com.example.utils.RoutePlanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/11/9 15:54
 * @Description: ServiceComposition
 * @FileName: ServiceComposition
 */
@Component
public class ServiceComposition {

    @Resource
    RoutePlanUtils routePlanUtils;

    @Resource
    DroneService droneService;

    @Resource
    CarService carService;

    @Resource
    CarToCustomerService carToCustomerService;

    /**
     * @return List<List < Double>>
     * @Description: ServiceComposition
     * @author pwz
     * @date 2022/10/13 16:46
     */
    public List<List<Double>> selectStrategyByObjective(
            Order order, String objective) {
        // 1：figured out the time energy consumption 0：not
        int f = 0;
        // environment flag 0：local service  1：openfaas service
        int environmentFlag = 0;
        List<List<Double>> res;
        List<String> route = null;
        String path;
        int[] timeAndEnergy;
        int weigh = (int) (order.getWeight() * 1000);
        int uavType = 2, ugvType = 2;
        Drone drone = droneService.getById(uavType);
        Car car = carService.getById(ugvType);
        // Whether the load is bearable
        int flag = routePlanUtils.loadJudge(drone, car, weigh);
        if (flag != 1) {
            res = routePlanUtils.stationNameToRouteLocation(null, new int[]{flag, flag});
        } else {
            switch (objective) {
                case "distance":        // Choose the nearest catStation
                    if (environmentFlag == 0) {
                        route = routePlanUtils.getShortestDistanceRoute(order.getStartStation(), order.getConsignee());
                    } else {
                        int source = GraphUtils.getSequenceByName(order.getStartStation());
                        int end = carToCustomerService.getShortestCarStationNum(order.getConsignee());
                        path = OpenFaasUtils.getShortestDistancePath1(source, end);
                        String[] split1 = path.split("->|,");
                        route = new ArrayList<>(Arrays.asList(split1));
                    }
                    break;
                case "time":           // ShortestTime
                    if (environmentFlag == 0) {
                        route = routePlanUtils.getShortestTimeRoute(order.getStartStation(), order.getConsignee(),
                                drone, car, weigh);
                    } else {
                        String carToUserDistance1 = GuideRoutePlanUtils.getCarToUserDistance(
                                carToCustomerService.getAllCarStationNameByCustomerName(
                                        order.getConsignee()), order.getConsignee());
                        f = 1;
                        path = OpenFaasUtils.getShortestTimePath1(order.getStartStation(), order.getConsignee()
                                , uavType - 1, ugvType - 1, weigh, carToUserDistance1);
                        String[] split2 = path.split(",");
                        route = new ArrayList<>(Arrays.asList(split2));
                    }
                    break;
                case "energy":          // ShortestEnergy
                    if (environmentFlag == 0) {
                        route = routePlanUtils.getShortestEnergyRoute(order.getStartStation(), order.getConsignee(),
                                drone, car, weigh);
                    } else {
                        String carToUserDistance2 = GuideRoutePlanUtils.getCarToUserDistance(
                                carToCustomerService.getAllCarStationNameByCustomerName(
                                        order.getConsignee()), order.getConsignee());
                        f = 1;
                        path = OpenFaasUtils.getShortestEnergyPath1(order.getStartStation(), order.getConsignee()
                                , uavType - 1, ugvType - 1, weigh, carToUserDistance2);
                        String[] split3 = path.split(",");
                        route = new ArrayList<>(Arrays.asList(split3));
                    }
                    break;
                case "energyInTime":      // energy consumption under deadline constrain
                    if (environmentFlag == 0) {
                        route = routePlanUtils.getShortestEnergyRouteUnderTimeConstraint(order.getStartStation(),
                                order.getConsignee(), drone, car, order.getDeadline() * 60, weigh);
                        if (route == null) {
                            System.out.println("All service composition plan cannot satisfy the deadline constraint, please modify the order deadline constraint.");
                            return null;
                        }
                    } else {
                        String carToUserDistance3 = GuideRoutePlanUtils.getCarToUserDistance(
                                carToCustomerService.getAllCarStationNameByCustomerName(
                                        order.getConsignee()), order.getConsignee());
                        f = 1;
                        path = OpenFaasUtils.getShortestEnergyInTimePath1(order.getStartStation(), order.getConsignee(),
                                uavType - 1, ugvType - 1, order.getDeadline(), weigh, carToUserDistance3);
                        if (path == null) return null;
                        String[] split4 = path.split(",");
                        route = new ArrayList<>(Arrays.asList(split4));
                    }
                    break;
            }
            if (f == 1) {
                timeAndEnergy = new int[]{Integer.parseInt(route.get(route.size() - 2)),
                        Integer.parseInt(route.get(route.size() - 1))};
                route.remove(route.size() - 1);
            } else {
                timeAndEnergy = routePlanUtils.getTimeAndEnergy(route, order.getConsignee(), drone, car, weigh);
            }
            route.remove(route.size() - 1);
            System.out.println("The optimal service composition plan has been generated：" + route + ", " +
                    "Estimated delivery time：" + timeAndEnergy[0] + "s, Estimated Delivery Energy Consumption："
                    + timeAndEnergy[1] + "j");
            res = routePlanUtils.stationNameToRouteLocation(route, timeAndEnergy);
        }
        return res;
    }
}