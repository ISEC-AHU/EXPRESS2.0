package com.example.test;

import com.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: pwz
 * @create: 2022/9/27 15:26
 * @Description:
 * @FileName: OrderService
 */
@SpringBootTest
public class OrderServiceTest {

    @Resource
    OrderService orderService;

    @Test
    public void testGetActiveOrders() {
        System.out.println(orderService.getActiveOrders());
    }
}