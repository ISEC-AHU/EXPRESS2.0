package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bean.Drone;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: pwz
 * @create: 2022/10/13 19:16
 * @Description:
 * @FileName: DroneMapper
 */
@Mapper
public interface DroneMapper extends BaseMapper<Drone> {
}
