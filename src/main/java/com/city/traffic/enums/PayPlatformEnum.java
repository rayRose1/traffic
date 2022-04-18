package com.city.traffic.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:36
 */
@Getter
public enum PayPlatformEnum {

    //1-支付宝,2-微信
    ALIPAY(1),

    WX(2),
    ;
    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }
    public static PayPlatformEnum getBestPayPlatTypeEnum (BestPayTypeEnum bestPayTypeEnum){
        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())){
                return  payPlatformEnum;
            }
        }
        throw  new RuntimeException("错误的支付平台：" + bestPayTypeEnum);
    }
}
