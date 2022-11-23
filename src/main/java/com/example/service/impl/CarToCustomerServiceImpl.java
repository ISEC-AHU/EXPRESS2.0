package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.CarStation;
import com.example.bean.CarToCustomer;
import com.example.mapper.CarStationMapper;
import com.example.mapper.CarToCustomerMapper;
import com.example.mapper.CustomerMapper;
import com.example.service.CarToCustomerService;
import com.example.service.DroneStationService;
import com.example.utils.GraphUtils;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: pwz
 * @create: 2022/9/22 11:12
 * @Description:
 * @FileName: CarToCustomerServiceImpl
 */
@Service
public class CarToCustomerServiceImpl extends ServiceImpl<CarToCustomerMapper, CarToCustomer>
        implements CarToCustomerService {

    @Resource
    CarToCustomerMapper carToCustomerMapper;

    @Resource
    CarStationMapper carStationMapper;

    @Resource
    CustomerMapper customerMapper;

    @Resource
    DroneStationService droneStationService;

    /**
     * @Description: Query the set of routes from all unmanned station points to the user
     * @author pwz
     * @date 2022/9/27 14:31
     * @return List<List<List<Double>>>
     */
    @Override
    public List<List<List<Double>>> getAllCarToCustomerPath() {
        List<CarToCustomer> paths = carToCustomerMapper.selectList(null);
        JSONArray jsonArray = new JSONArray();
        for (CarToCustomer path : paths) {
            List<List<Double>> lists = new ArrayList<>();
            List<Double> startList = new ArrayList<>();
            startList.add(carStationMapper.selectById(path.getStart()).getLongitude());
            startList.add(carStationMapper.selectById(path.getStart()).getLatitude());
            List<Double> endList = new ArrayList<>();
            endList.add(customerMapper.selectById(path.getEnd()).getLongitude());
            endList.add(customerMapper.selectById(path.getEnd()).getLatitude());
            lists.add(startList);
            lists.add(endList);
            jsonArray.add(lists);
        }
        return jsonArray;
    }

    /**
     * @Description: Query the nearest unmanned station by user name
     * @author pwz
     * @date 2022/9/26 16:17
     * @param customerName
     * @return int ：The order of the unmanned stations in the map starts from 0
     */
    @Override
    public int getShortestCarStationNum(String customerName) {
        String customerId = customerName.substring(1);
//        char customerId = customerName.toCharArray()[1];
        Map<String, Object> map = new HashMap<>();
        map.put("end", customerId);
        List<CarToCustomer> paths = carToCustomerMapper.selectByMap(map);
        int carNum = 0, minDistance = GraphUtils.maxDis;
        for (CarToCustomer path : paths) {
            if (path.getDistance() < minDistance) {
                carNum = path.getStart();
                minDistance = path.getDistance();
            }
        }
        return (int) (carNum + droneStationService.count() - 1);
    }

    /**
     * @Description: Queries the collection of all stations that can reach the specified user based on the user name
     * @author pwz
     * @date 2022/10/13 18:58
     * @param customerName
     * @return int[]
     */
    @Override
    public List<Integer> getAllCarStationByCustomerName(String customerName) {
        String customerId = customerName.substring(1);
        Map<String, Object> map = new HashMap<>();
        map.put("end", customerId);
        List<CarToCustomer> paths = carToCustomerMapper.selectByMap(map);
        List<Integer> list = new ArrayList<>();
        for (CarToCustomer path : paths) {
            list.add(path.getStart());
        }
        return list;
    }

    /**
     * @description: Queries the names of all stations that can reach the specified user based on the user name
     * @author: pwz
     * @date: 2022/11/8 17:43
     * @param: [customerName]
     * @return: java.util.List<java.lang.String>
     **/
    @Override
    public List<String> getAllCarStationNameByCustomerName(String customerName) {
        String customerId = customerName.substring(1);
        Map<String, Object> map = new HashMap<>();
        map.put("end", customerId);
        List<CarToCustomer> paths = carToCustomerMapper.selectByMap(map);
        List<String> list = new ArrayList<>();
        for (CarToCustomer path : paths) {
            CarStation carStation = carStationMapper.selectById(path.getStart());
            list.add(carStation.getName());
        }
        return list;
    }
}