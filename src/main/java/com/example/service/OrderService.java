package com.example.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.bean.Order;

import java.util.HashMap;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/13 12:44
 * @Description:
 * @FileName: OrderService
 */
public interface OrderService extends IService<Order> {

//    @Cacheable(value = "order")
    List<Order> getActiveOrders();

    int getMaxId();

//    @CacheEvict(value = "order", allEntries = true)
    boolean save(Order order);

//    @CacheEvict(value = "order", allEntries = true)
    default boolean update(Order entity, Wrapper<Order> updateWrapper) {
        return SqlHelper.retBool(this.getBaseMapper().update(entity, updateWrapper));
    }

    List<Order> getAll();

    HashMap<String, Object> getInfo(int id);

    List<String> getLastAndNext(String route);
}
