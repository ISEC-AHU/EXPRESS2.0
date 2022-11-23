package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.Drone;
import com.example.mapper.DroneMapper;
import com.example.service.DroneService;
import org.springframework.stereotype.Service;

/**
 * @author: pwz
 * @create: 2022/10/13 19:18
 * @Description:
 * @FileName: DroneServiceImpl
 */
@Service
public class DroneServiceImpl extends ServiceImpl<DroneMapper, Drone> implements DroneService {
}