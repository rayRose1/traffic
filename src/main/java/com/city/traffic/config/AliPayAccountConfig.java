package com.city.traffic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝支付配置
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:07
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayAccountConfig {

    private String appId;

    private String privateKey;

    private String publicKey;

    private String notifyUrl;

    private String returnUrl;
}
