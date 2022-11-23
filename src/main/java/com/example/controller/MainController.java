package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.bean.Order;
import com.example.service.OrderService;
import com.example.service.PathService;
import com.example.service.WareService;
import com.example.utils.GraphUtils;
import com.example.utils.PageUtils;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/3 15:29
 * @Description:
 * @FileName: MainController
 */
@Controller
public class MainController {

    @Resource
    OrderService orderService;

    @Resource
    WareService wareService;

    @Resource
    PathService pathService;

    // paging query
    @GetMapping("/orders")
    public String toOrders(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model) {
        Page<Order> orderPage = new Page<>(pn, 8);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Order> page = orderService.page(orderPage, wrapper);
        int[] pageNav = PageUtils.pageNav(pn, (int) page.getPages(), 5);
        model.addAttribute("orders", page);
        model.addAttribute("navNums", pageNav);
        return "orders";
    }

    // getMapById
    @GetMapping(value = "/main/{id}")
    public String getMapById(@PathVariable("id")Integer id, Model model) {
        List<Order> all = orderService.getAll();
        for (Order order : all) {
            List<String> lastAndNext = orderService.getLastAndNext(order.getRoute());
            order.setLastStation(lastAndNext.get(0));
            order.setNextStation(lastAndNext.get(1));
        }
        model.addAttribute("allOrders", all);

        HashMap<String, Object> info = orderService.getInfo(id);
        model.addAttribute("info", info);
        List<Order> activeOrders = orderService.getActiveOrders();
        model.addAttribute("activeOrdersNum", activeOrders.size());
        Order order = orderService.getById(id);
        List<List<Double>> path = pathService.getPathByOrderId(order.getOrderId());
        model.addAttribute("pathLists", path);
        JSONArray stationInfo = new JSONArray();
        List<String> stations = pathService.getPathStationsByOrderId(order.getOrderId());
        stations.add(order.getConsignee());
        List<List<Double>> stationLocations = GraphUtils.stationNamesToLocations(stations);
        stationInfo.add(stations);
        stationInfo.add(stationLocations);
        model.addAttribute("stationInfo", stationInfo);
        return "active_order";
    }

    // to security management
    @GetMapping("/safety")
    public String toSafety(@RequestParam(value = "id", required = false, defaultValue = "0")int id, Model model) {
        int maxId = id;
        if (id == 0) {
            maxId = orderService.getMaxId();
        }
        List<Order> all = orderService.getAll();
        model.addAttribute("allOrders", all);
        Order order = orderService.getById(maxId);
        List<List<Double>> path = pathService.getPathByOrderId(order.getOrderId());
        model.addAttribute("pathLists", path);
        JSONArray desLocation = new JSONArray();
        desLocation.add(order.getPrivacyLongitude());
        desLocation.add(order.getPrivacyLatitude());
        model.addAttribute("desLocation", desLocation);
        return "safe_order";
    }

    // to ai management
    @GetMapping("/ai")
    public String toAi(Model model) {
        List<Order> activeOrders = orderService.getActiveOrders();
        model.addAttribute("activeOrders", activeOrders);
        return "ai_page";
    }

}