package com.example.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/10/13 19:27
 * @Description:
 * @FileName: Car
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int speed;
    private int noLoadPower;
    private int maxPower;
    private int maxLoad;
    private int battery;
}