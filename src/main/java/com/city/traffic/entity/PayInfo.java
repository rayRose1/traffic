package com.city.traffic.entity;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInfo {

    private String orderId;

    @DecimalMin("0.01")
    private double orderAmount;

    private BestPayTypeEnum bestPayTypeEnum;


    @Override
    public String toString() {
        return "PayInfo{" +
                "orderId='" + orderId + '\'' +
                ", orderAmount=" + orderAmount +
                ", bestPayTypeEnum=" + bestPayTypeEnum +
                '}';
    }
}
