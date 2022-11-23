package com.example.test;

import com.example.service.PathService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: pwz
 * @create: 2022/9/27 15:05
 * @Description:
 * @FileName: PathServiceTest
 */
@SpringBootTest
public class PathServiceTest {

    @Resource
    PathService pathService;

    @Test
    public void testInsertPaths() {
        List<String> list = new ArrayList<>();
        list.add("W1");
        list.add("D7");
        list.add("C3");
        pathService.insertPaths(list, 12345);
    }

    @Test
    public void testGetPathStationsByOrderId() {
        System.out.println(pathService.getPathStationsByOrderId(11111));
    }

}