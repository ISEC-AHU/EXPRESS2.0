package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.Car;
import com.example.mapper.CarMapper;
import com.example.service.CarService;
import org.springframework.stereotype.Service;

/**
 * @author: pwz
 * @create: 2022/10/13 19:32
 * @Description:
 * @FileName: CarService
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
}