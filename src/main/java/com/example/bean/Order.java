package com.example.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/13 11:37
 * @Description:
 * @FileName: Order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("order_record")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int model;
    private String startStation;
    private double desLongitude;
    private double desLatitude;
    private double privacyLongitude;
    private double privacyLatitude;
    private int orderId;
    private String consignee;
    private int length;
    private int width;
    private double weight;
    private String goods;
    private int deadline;  //   /s
    private int status; // Default 0: unshipped 1: in transit 2: timed out 3: completed
    private String info;
    private boolean privacyStatus; //The privacy protection status is set to 1
    private String route;
    private int time;
    private int energy;
    @TableField(exist = false)
    private String lastStation;
    @TableField(exist = false)
    private String nextStation;
}