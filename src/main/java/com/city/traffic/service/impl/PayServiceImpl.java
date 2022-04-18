package com.city.traffic.service.impl;

import com.city.traffic.entity.MallPayInfo;
import com.city.traffic.entity.MallPayInfoExample;
import com.city.traffic.enums.PayPlatformEnum;
import com.city.traffic.mapper.MallPayInfoMapper;
import com.city.traffic.service.IPayService;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:23
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {


    @Autowired
    private BestPayService bestPayService;
    @Autowired
    private MallPayInfoMapper mallPayInfoMapper;
    /**
     * 创建发起支付
     *  @param orderId
     * @param amount
     * @return
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount,BestPayTypeEnum bestPayTypeEnum) {
        //发起支付的时候 数据要写入数据库中
        MallPayInfo mallPayInfo = new MallPayInfo(Long.valueOf(orderId), PayPlatformEnum.getBestPayPlatTypeEnum(bestPayTypeEnum).getCode(), OrderStatusEnum.NOTPAY.name(),amount);
        mallPayInfoMapper.insertSelective(mallPayInfo);
        PayRequest request = new PayRequest();
        request.setOrderName("2630799-最好的支付sdk");
        request.setOrderId(orderId);
        request.setOrderAmount(amount.doubleValue());
        request.setPayTypeEnum(bestPayTypeEnum);
        PayResponse payResponse= bestPayService.pay(request);
        log.info("发起支付 response={}",payResponse);
        return payResponse;
    }

    /**
     * 异步通知
     */
    @Override
    public String asyncNotify(String notifyData) {
        MallPayInfoExample mallPayInfoExample= new MallPayInfoExample();
        MallPayInfoExample.Criteria criteria = mallPayInfoExample.createCriteria();
        //1.签名校验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 payResponse:{}",payResponse);
        //2.金额校验（从数据库中查订单）
        criteria.andOrderNoEqualTo(Long.valueOf(payResponse.getOrderId()));
        List<MallPayInfo> mallPayInfo= mallPayInfoMapper.selectByExample(mallPayInfoExample);
        //3.修改订单的支付状态
        //如果没有记录说明出了问题 通过短信 或 钉钉 微信等平台告警
        if (mallPayInfo == null){
            //发起告警
            throw  new RuntimeException("通过OrderNo查询到的记录为空orderId:"+payResponse.getOrderId());
        }
        //如果订单状态不是已支付  校验金额
        if(!mallPayInfo.get(0).getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())){
            //如果金额相同 修改订单状态
            if(mallPayInfo.get(0).getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount()))==0){
                MallPayInfo mallPayInfoDB = mallPayInfo.get(0);
                mallPayInfoDB .setUpdateTime(null);
                mallPayInfoDB .setId(null);
                mallPayInfoDB .setPlatformStatus(OrderStatusEnum.SUCCESS.name());
                criteria.andOrderNoEqualTo(Long.valueOf(payResponse.getOrderId()));
                mallPayInfoMapper.updateByExampleSelective(mallPayInfoDB ,mallPayInfoExample);
            }
            //如果金额不同，报警
            //发起告警
            throw  new RuntimeException("通过OrderNo发起的支付金额有问题 orderId:"+payResponse.getOrderId());
        }
        //TODO pay发送MQ消息 mall接受mq消息

        //4.告诉支付平台不要再重复通知了
        if (payResponse.getPayPlatformEnum()== BestPayPlatformEnum.WX){
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        }else if(payResponse.getPayPlatformEnum()== BestPayPlatformEnum.ALIPAY){
            return "success";
        }
        throw  new RuntimeException("异步通知中错误的支付平台");
    }

    /**
     * 通过订单号查询支付记录 用于前端的轮询
     * @param orderId
     * @return
     */
    @Override
    public MallPayInfo queryByOrderId(String orderId) {
        if (!ObjectUtils.isEmpty(orderId)){
            MallPayInfoExample mallPayInfoExample= new MallPayInfoExample();
            MallPayInfoExample.Criteria criteria = mallPayInfoExample.createCriteria();
            criteria.andOrderNoEqualTo(Long.valueOf(orderId));
            log.info("查询信息", mallPayInfoMapper.selectByExample(mallPayInfoExample).get(0).toString());
            return mallPayInfoMapper.selectByExample(mallPayInfoExample).get(0);
        }
        return new MallPayInfo();
    }
}
