package com.city.traffic.web;

import com.city.traffic.common.CommonResp;
import com.city.traffic.entity.MallPayInfo;
import com.city.traffic.entity.PayInfo;
import com.city.traffic.service.IPayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:28
 */
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private IPayService iPayService;


    @PostMapping("/create")
    @ResponseBody
    public CommonResp create(@Valid @RequestBody PayInfo payInfo){
        CommonResp commonResp= new CommonResp();
        log.info("PayInfo",payInfo.toString());
        PayResponse payResponse= iPayService.create(payInfo.getOrderId(), BigDecimal.valueOf(payInfo.getOrderAmount()),payInfo.getBestPayTypeEnum());
        commonResp.setContent(payResponse);
        return commonResp;
    }
    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData){
        return iPayService.asyncNotify(notifyData);
    }
    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @GetMapping("/queryByOrderId/{orderId}")
    @ResponseBody
    public CommonResp queryByOrderId(@PathVariable String orderId){
        log.info("开始查询orderID ：OrderId{}的支付记录",orderId);
        CommonResp commonResp = new CommonResp();
        MallPayInfo mallPayInfo = iPayService.queryByOrderId(orderId);
        commonResp.setContent(mallPayInfo);
        return commonResp;
    }
}
