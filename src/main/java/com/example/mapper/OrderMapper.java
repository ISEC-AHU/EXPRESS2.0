package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bean.Order;
import org.springframework.stereotype.Repository;

/**
 * @author: pwz
 * @create: 2022/9/13 11:58
 * @Description:
 * @FileName: OrderMapper
 */
@Repository
public interface OrderMapper extends BaseMapper<Order> {

}
