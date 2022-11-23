package com.example.test;

import com.example.utils.GraphUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author: pwz
 * @create: 2022/9/26 16:13
 * @Description:
 * @FileName: testGraph
 */
@SpringBootTest
public class GraphUtilsTest {

    @Test
    public void testGetSequenceByName() {
        System.out.println(GraphUtils.getSequenceByName("C3"));
    }

    @Test
    public void testGetMatrix() {
        int[][] matrix = GraphUtils.getMatrix();
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                System.out.print(anInt + ", ");
            }
            System.out.println();
        }
    }

    @Test
    public void testStationNamesToLocations() {
        System.out.println(GraphUtils.stationNamesToLocations(Arrays.asList("W1", "D1", "C1", "U1")));
    }

    @Test
    public void testGetVertex() {
        System.out.println(GraphUtils.getVertex());
        for (String vertex : GraphUtils.getVertex()) {
            System.out.print("\"" + vertex + "\",");
        }
    }
}