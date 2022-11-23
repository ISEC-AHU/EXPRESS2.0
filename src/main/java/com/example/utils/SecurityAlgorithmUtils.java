package com.example.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author: pwz
 * @create: 2022/9/23 11:16
 * @Description:
 * @FileName: SecurityAlgorithmUtils
 */
@Component
public class SecurityAlgorithmUtils {

    /**
     *
     * @paper： Geo-Indistinguishability: Differential Privacy for Location-Based Systems
     * @Author ZZ
     * @param location
     * @return location_dp
     */
    public static double[] geoDp(double[] location){
        double theta ;
        double p;
        double radius;
        double eps = 10.0;
        double[] location_dp = new double[2];
        Random r = new Random();
        theta = r.nextDouble()*2*Math.PI;
        p = r.nextDouble();
        radius = 1 / (1 - (1 + Math.exp(1) * p)*Math.exp(-eps*p));
        location_dp[0] = location[0] + radius * Math.cos(theta) * 0.001;
        location_dp[1] = location[1] + radius * Math.sin(theta) * 0.001;
        return location_dp;
    }

    /**
     *
     * @paper： Geo-Indistinguishability: Differential Privacy for Location-Based Systems
     * @Author ZZ
     * @param location
     * @return location_dp
     */
    public static double[] geoDpEnhance(double[] location){
        double theta ;
        double p;
        double radius;
        double eps = 10.0;
        double[] location_dp = new double[2];
        Random r = new Random();
        theta = r.nextDouble()*2*Math.PI;
        p = r.nextDouble();
        while (true) {
            radius = 1 / (1 - (1 + Math.exp(1) * p)*Math.exp(-eps*p));
            if (radius < 5) break;
        }
        location_dp[0] = location[0] + radius * Math.cos(theta) * 0.001;
        location_dp[1] = location[1] + radius * Math.sin(theta) * 0.001;
        return location_dp;
    }
}