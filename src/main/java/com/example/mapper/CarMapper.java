package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bean.Car;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: pwz
 * @create: 2022/10/13 19:30
 * @Description:
 * @FileName: CarMapper
 */
@Mapper
public interface CarMapper extends BaseMapper<Car> {
}
