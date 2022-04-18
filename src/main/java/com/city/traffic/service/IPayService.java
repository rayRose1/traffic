package com.city.traffic.service;

import com.city.traffic.entity.MallPayInfo;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;

import java.math.BigDecimal;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:16
 */
public interface IPayService {

    /**
     * 创建发起支付
     * @return
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);
    /**
     * 异步通知
     */
    String asyncNotify(String notifyData);

    /**
     * 通过订单号查询支付记录 用于前端的轮询
     * @param orderId
     * @return
     */
    MallPayInfo queryByOrderId (String orderId);
}
