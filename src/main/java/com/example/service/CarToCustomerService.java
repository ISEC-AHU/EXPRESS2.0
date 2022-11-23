package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.CarToCustomer;

import java.util.List;


/**
 * @author: pwz
 * @create: 2022/9/22 11:12
 * @Description:
 * @FileName: CarToCustomerService
 */
public interface CarToCustomerService extends IService<CarToCustomer> {


    List<List<List<Double>>> getAllCarToCustomerPath();

    int getShortestCarStationNum(String customerName);

    List<Integer> getAllCarStationByCustomerName(String customerName);

    List<String> getAllCarStationNameByCustomerName(String customerName);
}