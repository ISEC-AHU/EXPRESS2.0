package com.example.controller;

import com.example.bean.Order;
import com.example.service.CustomerService;
import com.example.service.OrderService;
import com.example.service.PathService;
import com.example.utils.RoutePlanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/21 11:48
 * @Description:
 * @FileName: OrderController
 */
@Controller
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    PathService pathService;

    @Resource
    CustomerService customerService;

    // The path planner populates the order attribute to insert the database
    @RequestMapping("/insertOrder")
    public String insertOrder(Order order, int uavType, int ugvType, String objective) {
        List<String> route = RoutePlanUtils.getRouteByObjective(order, objective, uavType, ugvType);
        order.setTime(Integer.parseInt(route.get(route.size() - 2)) / 60);
        order.setEnergy(Integer.parseInt(route.get(route.size() - 1)) / 1000);
        route.remove(route.size() - 1);
        route.remove(route.size() - 1);
        //      insert path table
        pathService.insertPaths(route, order.getOrderId());
        //      set order.route
        List<String> stations = pathService.getPathStationsByOrderId(order.getOrderId());
        String string = RoutePlanUtils.pathListToString(stations, order.getConsignee());
        order.setRoute(string);
        List<Double> locationByName = customerService.getLocationByName(order.getConsignee());
        order.setDesLongitude(locationByName.get(0));
        order.setDesLatitude(locationByName.get(1));
        order.setPrivacyLongitude(locationByName.get(0));
        order.setPrivacyLatitude(locationByName.get(1));
        orderService.save(order);
        return "redirect:/orders";
    }


}