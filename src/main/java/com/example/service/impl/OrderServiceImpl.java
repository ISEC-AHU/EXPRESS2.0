package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.bean.Drone;
import com.example.bean.Order;
import com.example.mapper.OrderMapper;
import com.example.service.DroneService;
import com.example.service.OrderService;
import com.example.service.StationNetMapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: pwz
 * @create: 2022/9/13 12:45
 * @Description:
 * @FileName: OrderServiceImpl
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Resource
    DroneService droneService;

    @Resource
    StationNetMapService stationNetMapService;

    @Resource
    OrderMapper orderMapper;

    @Override
    public List<Order> getActiveOrders() {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("status", "select status from order_record where status != 3");
        queryWrapper.orderByDesc("id");
        List<Order> list = orderMapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public int getMaxId() {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id").last("limit 1");
        Order one = orderMapper.selectOne(wrapper);
        return one.getId();
    }

    @Override
    public boolean save(Order order) {
        return SqlHelper.retBool(this.getBaseMapper().insert(order));
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = orderMapper.selectList(null);
        return orders;
    }

    @Override
    public HashMap<String, Object> getInfo(int id) {
        Order order = orderMapper.selectById(id);
        HashMap<String, Object> res = new HashMap<>();
        String[] split = order.getRoute().split("->");
        String lastStation = split[split.length - 3];
        String nextStation = split[split.length - 2];
        res.put("lastStation", lastStation);
        res.put("nextStation", nextStation);
        int distance = stationNetMapService.getDistance(lastStation, nextStation);
        res.put("distance", String.valueOf(distance));
        Random random = new Random();
        int index = random.nextInt(3) + 1;
        Drone drone = droneService.getById(index);
        res.put("drone", drone);
        res.put("order", order);
        return res;
    }

    @Override
    public List<String> getLastAndNext(String route) {
        String[] split = route.split("->");
        ArrayList<String> res = new ArrayList<>(Arrays.asList(split[split.length - 3], split[split.length - 2]));
        return res;
    }
}