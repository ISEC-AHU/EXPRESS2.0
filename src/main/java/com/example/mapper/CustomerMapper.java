package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.bean.Customer;
import org.springframework.stereotype.Repository;

/**
 * @author: pwz
 * @create: 2022/9/22 11:05
 * @Description:
 * @FileName: CustomerMapper
 */
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {
}