package com.example.controller;

import com.example.bean.Order;
import com.example.service.*;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/1 16:14
 * @Description:
 * @FileName: IndexController
 */
@Controller
public class LoginController {

    @Resource
    WareService wareService;

    @Resource
    CarStationService carStationService;

    @Resource
    OrderService orderService;

    @Resource
    DroneStationService droneStationService;

    @Resource
    PathService pathService;

    @Resource
    StationNetMapService stationNetMapService;

    @Resource
    CarToCustomerService carToCustomerService;

    @Resource
    CustomerService customerService;

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String main() {
        return "redirect:/main.html";
    }

    @GetMapping("/main.html")
    public String mainPage(Model model) {
        List<Order> all = orderService.getAll();
        for (Order order : all) {
            List<String> lastAndNext = orderService.getLastAndNext(order.getRoute());
            order.setLastStation(lastAndNext.get(0));
            order.setNextStation(lastAndNext.get(1));
        }
//        Query the current active order
        List<Order> activeOrders = orderService.getActiveOrders();
//        Query all site names
        List<String> droneStationNames = droneStationService.getNames();
        List<String> carStationNames = carStationService.getNames();
        JSONArray stationName = new JSONArray();
        stationName.addAll(droneStationNames);
        stationName.addAll(carStationNames);

//        Query all site coordinates
        List<List<Double>> droneStationLongitudesAndLatitudes = droneStationService.getLongitudesAndLatitudes();
        List<List<Double>> carStationLongitudesAndLatitudes = carStationService.getLongitudesAndLatitudes();
        JSONArray stationLongitudesAndLatitude = new JSONArray();
        stationLongitudesAndLatitude.addAll(droneStationLongitudesAndLatitudes);
        stationLongitudesAndLatitude.addAll(carStationLongitudesAndLatitudes);

//        Query the path between sites
        List<List<List<Double>>> path = stationNetMapService.getAllStationPath();

//        Query user coordinates and names
        List<String> customerNames = customerService.getNames();
        List<List<Double>> customerLongitudesAndLatitudes = customerService.getLongitudesAndLatitudes();

//        Query the path from the unmanned station to the user
        List<List<List<Double>>> allCarToCustomerPath = carToCustomerService.getAllCarToCustomerPath();

        model.addAttribute("allOrders", all);
        model.addAttribute("activeOrdersNum", activeOrders.size());
        model.addAttribute("stationNames", stationName);
        model.addAttribute("stationLongitudesAndLatitudes", stationLongitudesAndLatitude);
        model.addAttribute("pathLists", path);
        model.addAttribute("customerNames", customerNames);
        model.addAttribute("customerLongitudesAndLatitudes", customerLongitudesAndLatitudes);
        model.addAttribute("allCarToCustomerPath", allCarToCustomerPath);

        return "main";
    }



}