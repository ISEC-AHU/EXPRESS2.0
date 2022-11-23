package com.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/22 11:03
 * @Description:
 * @FileName: Customer
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Customer {

    private Integer id;
    private String name;
    private Double longitude;
    private Double latitude;
}