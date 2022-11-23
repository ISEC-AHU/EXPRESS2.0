package com.example.test;

import com.example.service.CarToCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: pwz
 * @create: 2022/9/26 15:48
 * @Description:
 * @FileName: testCarToCustomerService
 */
@SpringBootTest
public class CarToCustomerServiceTest {


    @Resource
    CarToCustomerService carToCustomerService;

    @Test
    public void testGetShortestCarStationNum() {
        int u1 = carToCustomerService.getShortestCarStationNum("U1");
        System.out.println(u1);
    }
}