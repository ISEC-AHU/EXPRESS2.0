package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.Path;
import com.example.mapper.CarStationMapper;
import com.example.mapper.DroneStationMapper;
import com.example.mapper.PathMapper;
import com.example.service.PathService;
import com.example.utils.GraphUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/16 15:39
 * @Description:
 * @FileName: PathServiceImpl
 */
@Service
public class PathServiceImpl extends ServiceImpl<PathMapper, Path> implements PathService {


    @Resource
    PathMapper pathMapper;

    @Resource
    DroneStationMapper droneStationMapper;

    @Resource
    CarStationMapper carStationMapper;

    @Override
    public List<List<Double>> getPathByOrderId(int orderId) {

        QueryWrapper<Path> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId).orderByAsc("station_number");
        List<Path> paths = pathMapper.selectList(queryWrapper);
//        System.out.println(paths);
        List<List<Double>> pathLists = new ArrayList<>();
        for (Path path : paths) {
            double longitude, latitude;
            if (path.getDid() != 0) {
                longitude = droneStationMapper.selectById(path.getDid()).getLongitude();
                latitude = droneStationMapper.selectById(path.getDid()).getLatitude();
            } else {
                longitude = carStationMapper.selectById(path.getCid()).getLongitude();
                latitude = carStationMapper.selectById(path.getCid()).getLatitude();
            }
            ArrayList<Double> list = new ArrayList<>();
            list.add(longitude);
            list.add(latitude);
            pathLists.add(list);
        }
        return pathLists;
    }

    @Override
    public void insertPaths(List<String> stationNames, int orderId) {
        int stationNumber = 1;
        for (String stationName : stationNames) {
            int did = 0, cid = 0;
            if (stationName.charAt(0) == 'D' || stationName.charAt(0) == 'W') {
                // The order returned starts at 0, and the valid path in the path table starts at 1
                did = GraphUtils.getSequenceByName(stationName) + 1;
            } else {
                cid = Integer.valueOf(stationName.substring(1));
            }
            pathMapper.insert(new Path(orderId, did, cid, stationNumber++));
        }
    }

    /**
     * @Description: Search path Site name
     * @author pwz
     * @date 2022/10/21 14:29
     */
    @Override
    public List<String> getPathStationsByOrderId(int orderId) {
        QueryWrapper<Path> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId).orderByAsc("station_number");
        List<Path> paths = pathMapper.selectList(queryWrapper);
        ArrayList<String> res = new ArrayList<>();
        for (Path path : paths) {
            if (path.getDid() == 0) {
                res.add("C" + path.getCid());
            } else if (path.getDid() == 1) {
                res.add("W1");
            } else {
                res.add("D" + (path.getDid() - 1));
            }
        }
        return res;
    }
}