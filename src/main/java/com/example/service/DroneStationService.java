package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.DroneStation;

import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/15 18:55
 * @Description:
 * @FileName: DroneStationService
 */
public interface DroneStationService extends IService<DroneStation> {

    List<String> getNames();

    List<List<Double>> getLongitudesAndLatitudes();

    List<Double> getLocationByName(String name);
}
