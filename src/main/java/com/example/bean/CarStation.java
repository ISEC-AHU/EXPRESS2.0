package com.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/15 18:43
 * @Description:
 * @FileName: CarStation
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarStation {

    private Integer id;
    private String name;
    private double longitude;
    private double latitude;
    private boolean bigCarStatus;
    private int bigCarId;
    private boolean middleCarStatus;
    private int middleCarId;
    private boolean smallCarStatus;
    private int smallCarId;
}