package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.CarStation;
import com.example.bean.DroneStation;
import com.example.bean.StationNetMap;
import com.example.mapper.CarStationMapper;
import com.example.mapper.DroneStationMapper;
import com.example.mapper.StationNetMapMapper;
import com.example.service.StationNetMapService;
import com.example.utils.GraphUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/19 17:01
 * @Description:
 * @FileName: Station
 */
@Service
public class StationNetMapServiceImpl extends ServiceImpl<StationNetMapMapper, StationNetMap>
        implements StationNetMapService {

    @Resource
    StationNetMapMapper stationNetMapMapper;

    @Resource
    DroneStationMapper droneStationMapper;

    @Resource
    CarStationMapper carStationMapper;

    /**
     * @Description: Gets a collection of paths to the map for foreground display
     * @author pwz
     * @date 2022/9/20 11:30
     * @return java.util.List<java.util.List<java.util.List<java.lang.Double>>>
     */
    @Override
    public List<List<List<Double>>> getAllStationPath() {

        List<StationNetMap> stationNetMaps = stationNetMapMapper.selectList(null);
//        System.out.println(stationNetMaps);
        List<List<List<Double>>> allPath = new ArrayList<>();

        for (StationNetMap stationNetMap : stationNetMaps) {
            List<List<Double>> onePath = new ArrayList<>();
            int start = stationNetMap.getStart();
            double longitude, latitude;
            DroneStation droneStation = droneStationMapper.selectById(start);
            longitude = droneStation.getLongitude();
            latitude = droneStation.getLatitude();
            ArrayList<Double> startStation = new ArrayList<>();
            startStation.add(longitude);
            startStation.add(latitude);
            onePath.add(startStation);
            if (stationNetMap.getEndDid() != 0) {
                DroneStation droneStation1 = droneStationMapper.selectById(stationNetMap.getEndDid());
                longitude = droneStation1.getLongitude();
                latitude = droneStation1.getLatitude();
            } else {
                CarStation carStation = carStationMapper.selectById(stationNetMap.getEndCid());
                longitude = carStation.getLongitude();
                latitude = carStation.getLatitude();
            }
            ArrayList<Double> endStation = new ArrayList<>();
            endStation.add(longitude);
            endStation.add(latitude);
            onePath.add(endStation);
            allPath.add(onePath);
        }

        return allPath;
    }

    /**
     * @Description: Generate the adjacency matrix
     * @author pwz
     * @date 2022/9/23 9:49
     * @return int[][]
     */
    public int[][] getMatrix() {
        int droneCount = Math.toIntExact(droneStationMapper.selectCount(null));
        int carCount = Math.toIntExact(carStationMapper.selectCount(null));
        int[][] matrix = new int[droneCount + carCount][droneCount + carCount];
        for (int[] i : matrix) {
            Arrays.fill(i, GraphUtils.maxDis);
        }
        List<StationNetMap> stationNetMaps = stationNetMapMapper.selectList(null);
        for (StationNetMap stationNetMap : stationNetMaps) {
            int x = stationNetMap.getStart() - 1;
            int y = stationNetMap.getEndDid() == 0
                    ? stationNetMap.getEndCid() - 1 + droneCount
                    : stationNetMap.getEndDid() - 1;
            matrix[x][y] = matrix[y][x] = stationNetMap.getDistance();
        }
        return matrix;
    }

    @Override
    public int getDistance(String start, String end) {
        int left = Integer.parseInt(start.substring(1)) + 1;
        int right = Integer.parseInt(end.substring(1));
        HashMap<String, Object> map = new HashMap<>();
        map.put("start", left);
        map.put("end_cid", right);
        StationNetMap stationNetMap = stationNetMapMapper.selectByMap(map).get(0);
        return stationNetMap.getDistance();
    }
}
