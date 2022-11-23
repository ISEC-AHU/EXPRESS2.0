package com.example.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/22 11:08
 * @Description: The route from the unmanned station to the user
 * @FileName: CarToCustomer
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarToCustomer {

    private Integer id;
    private int start;
    private int end;
    private int distance;
}