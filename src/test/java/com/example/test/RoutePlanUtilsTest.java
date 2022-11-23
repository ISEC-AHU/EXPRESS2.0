package com.example.test;

/**
 * @author: pwz
 * @create: 2022/9/26 16:20
 * @Description:
 * @FileName: testRoutePlanning
 */

import com.example.service.CarToCustomerService;
import com.example.utils.GraphUtils;
import com.example.utils.RoutePlanUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class RoutePlanUtilsTest {

    @Resource
    CarToCustomerService carToCustomerService;

    @Test
    public void testGetShortestPath() {
        int source = GraphUtils.getSequenceByName("W1");
        System.out.println(source);
        int end = carToCustomerService.getShortestCarStationNum("U1");
        System.out.println(end);
        List<String> paths = RoutePlanUtils.getShortestPath(source, end);
        System.out.println(paths);
    }

    @Test
    public void testGetShortestPaths() {
        System.out.println(RoutePlanUtils.getShortestPath(0, 22));
        System.out.println(RoutePlanUtils.getShortestPath(0, 22));
    }

    @Test
    public void testGetShortestStationName() {
//        System.out.println(RoutePlanning.getShortestStationName("W1", "U1"));
    }
}