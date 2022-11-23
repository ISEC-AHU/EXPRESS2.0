package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.Customer;
import com.example.mapper.CustomerMapper;
import com.example.service.CustomerService;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: pwz
 * @create: 2022/9/22 11:07
 * @Description:
 * @FileName: CustomerServiceImpl
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
        implements CustomerService {

    @Resource
    CustomerMapper customerMapper;

    @Override
    public List<String> getNames() {
        List<Customer> customers = customerMapper.selectList(null);
        List<String> list = new ArrayList<>();
        for (Customer customer : customers) {
            list.add(customer.getName());
        }
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(list);
        return jsonArray;
    }

    @Override
    public List<Double> getLocationByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        List<Customer> customer = customerMapper.selectByMap(map);
        List<Double> list = new ArrayList<>();
        list.add(customer.get(0).getLongitude());
        list.add(customer.get(0).getLatitude());
        return list;
    }

    @Override
    public List<List<Double>> getLongitudesAndLatitudes() {
        List<Customer> customers = customerMapper.selectList(null);
        JSONArray jsonArray = new JSONArray();
        for (Customer customer : customers) {
            List<Double> list = new ArrayList<>();
            list.add(customer.getLongitude());
            list.add(customer.getLatitude());
            jsonArray.add(list);
        }
        return jsonArray;
    }
}