package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.CarStation;

import java.util.List;


/**
 * @author: pwz
 * @create: 2022/9/15 18:56
 * @Description:
 * @FileName: CarStationService
 */
public interface CarStationService extends IService<CarStation> {

    CarStation selectById(int id);

    List<String> getNames();

    List<List<Double>> getLongitudesAndLatitudes();

    List<Double> getLocationByName(String name);

    List<String> getNameByIds(List<Integer> ids);
}
