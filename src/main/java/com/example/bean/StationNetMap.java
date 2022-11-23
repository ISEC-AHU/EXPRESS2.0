package com.example.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/19 11:16
 * @Description:
 * @FileName: tMap
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "t_map")
public class StationNetMap {

    private Integer id;
    private int start;
    private int endDid;
    private int endCid;
    private int distance;
}