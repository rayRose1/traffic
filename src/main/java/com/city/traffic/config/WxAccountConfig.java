package com.city.traffic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxAccountConfig {

    private String appId;

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String returnUrl;
}
