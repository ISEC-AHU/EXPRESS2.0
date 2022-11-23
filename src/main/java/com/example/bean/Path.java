package com.example.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/9/16 15:37
 * @Description:
 * @FileName: Path
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Path {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int orderId;
    private int did;
    private int cid;
    private int stationNumber;

    public Path(int orderId, int did, int cid, int stationNumber) {
        this.setOrderId(orderId);
        this.setDid(did);
        this.setCid(cid);
        this.setStationNumber(stationNumber);
    }
}