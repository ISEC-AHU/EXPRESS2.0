package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.bean.WorkflowFile;
import com.example.mapper.WorkflowFileMapper;
import com.example.service.WorkflowFileService;
import org.springframework.stereotype.Service;

/**
 * @author: pwz
 * @create: 2022/11/9 15:02
 * @Description:
 * @FileName: WorkflowFileServiceImpl
 */
@Service
public class WorkflowFileServiceImpl extends ServiceImpl<WorkflowFileMapper, WorkflowFile>
        implements WorkflowFileService {
}