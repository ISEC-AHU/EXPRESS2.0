package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.bean.Order;
import com.example.core.SecurityService;
import com.example.service.OrderService;
import com.example.service.PathService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author: pwz
 * @create: 2022/9/23 19:35
 * @Description:
 * @FileName: SafetyController
 */
@Controller
public class SafetyController {

    @Resource
    OrderService orderService;

    @Resource
    PathService pathService;

    @Resource
    SecurityService securityService;

    // Privacy protection or not
    @GetMapping(value = "/safe/{id}")
    public String safeOrder(@PathVariable("id")Integer id,
                            @RequestParam(value = "type", required = false, defaultValue = "0")int type) {
        Order order = orderService.getById(id);
        double[] location = {order.getDesLongitude(), order.getDesLatitude()};
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        Order newOrder = securityService.getSafetyLocation(type, order, location);
        orderService.update(newOrder, updateWrapper);
        return "redirect:/safety?id=" + id;
    }
}