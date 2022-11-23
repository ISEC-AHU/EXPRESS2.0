package com.example.core;

import com.example.bean.Order;
import com.example.utils.OpenFaasUtils;
import com.example.utils.SecurityAlgorithmUtils;
import org.springframework.stereotype.Component;

/**
 * @author: pwz
 * @create: 2022/11/11 13:25
 * @Description:
 * @FileName: SecurityService
 */
@Component
public class SecurityService {

    public static Order getSafetyLocation(int type, Order order, double[] location) {
        // environment flag 0：local service  1：openfaas service
        int environmentFlag = 1;
        if (type == 0) {
            // close protection
            order.setPrivacyLongitude(location[0]);
            order.setPrivacyLatitude(location[1]);
            order.setPrivacyStatus(false);
        } else {
            double[] enLocation = null;
            if (type == 1) {
                // geoDp()
                enLocation = environmentFlag == 0 ?
                        SecurityAlgorithmUtils.geoDp(location) : OpenFaasUtils.geoDp(location);
            } else if (type == 2) {
                // geoDpEnhance()
                enLocation = environmentFlag == 0 ?
                        SecurityAlgorithmUtils.geoDpEnhance(location) : OpenFaasUtils.geoDpEnhance(location);
            } else if (type == 3) {
                // other method
            }
            order.setPrivacyLongitude(enLocation[0]);
            order.setPrivacyLatitude(enLocation[1]);
            order.setPrivacyStatus(true);
        }
        return order;
    }
}