package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.bean.StationNetMap;

import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/19 16:56
 * @Description:
 * @FileName: StationNetMapService
 */
public interface StationNetMapService extends IService<StationNetMap> {

//    @Cacheable(value = "stationNetMap")
    List<List<List<Double>>> getAllStationPath();

    int[][] getMatrix();

    int getDistance(String start, String end);
}
