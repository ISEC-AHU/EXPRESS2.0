package com.example.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: pwz
 * @create: 2022/11/9 14:56
 * @Description:
 * @FileName: WorkflowFile
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkflowFile {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private byte[] file;

    private int status;
}