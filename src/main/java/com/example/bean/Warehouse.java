package com.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/15 18:37
 * @Description:
 * @FileName: Warehouse
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Warehouse {

    private Integer id;
    private String name;
    private double longitude;
    private double latitude;
}