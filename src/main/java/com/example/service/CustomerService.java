package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/22 11:06
 * @Description:
 * @FileName: CustomerService
 */
public interface CustomerService extends IService<Customer> {

    List<String> getNames();

    List<Double> getLocationByName(String name);

    List<List<Double>> getLongitudesAndLatitudes();
}
