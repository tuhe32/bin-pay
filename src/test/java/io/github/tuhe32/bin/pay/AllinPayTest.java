package io.github.tuhe32.bin.pay;

import io.github.tuhe32.bin.pay.allin.AllinPayService;
import io.github.tuhe32.bin.pay.allin.AllinPayServiceImpl;
import io.github.tuhe32.bin.pay.allin.config.AllinPayConfig;
import io.github.tuhe32.bin.pay.allin.domain.*;
import io.github.tuhe32.bin.pay.common.PayBroker;
import io.github.tuhe32.bin.pay.common.PayOptional;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 刘斌
 * @date 2024/5/9 16:58
 */
@Slf4j
public class AllinPayTest {

    private static final AllinPayService allinPayService;

    static {
        allinPayService = init();
    }

    // 正常不能通过静态方式初始化。需要创建一个AllinPayService的bean，注入到容器中
    public static AllinPayService init() {
        AllinPayService allinPayService = new AllinPayServiceImpl();
        AllinPayConfig allinPayConfig = new AllinPayConfig("app_id", "your private_key", "alipay_public_key");
        allinPayConfig.setNotifyUrl("notifyUrl");
        allinPayService.setConfig(allinPayConfig);
        return allinPayService;
    }

    public static void testPay() {
        try {
            // 如果只有一个商户号，可以不用切换
            allinPayService.switchoverTo("cusId");
            // 统一收银台支付，微信W06，支付宝A02
            AllinPayExtraData extraData = allinPayService.checkoutPay(new AllinPayCheckoutRequest()
                    .setReqSn("订单号").setTrxAmt(BigDecimal.valueOf(1)).setPayType("W06").setBody("测试支付"));
            // H5收银台支付，返回给前端重定向地址
            String redirectUrl = allinPayService.h5CheckoutPay(new AllinPayH5CheckoutRequest()
                    .setReqSn("订单号").setTrxAmt(BigDecimal.valueOf(1)).setReturl("https://baidu.com").setBody("测试支付"));
            // 统一支付
            String payInfo = allinPayService.unitOrderPay(new AllinPayUnitOrderRequest()
                    .setReqSn("订单号").setTrxAmt(BigDecimal.valueOf(1)).setPayType("W02").setSubOpenId("openId"));
            // 统一扫码支付
            Boolean bol = allinPayService.scanQrPay(new AllinPayScanQrPayRequest()
                    .setReqSn("订单号").setTrxAmt(BigDecimal.valueOf(1)).setAuthCode("付款码")
                    .setTermInfo(new AllinPayScanQrPayRequest.AllinPayTermInfo().setTermNo("8位终端号").setLatitude("纬度").setLongitude("经度")));
            // 终端信息采集接口（使用【扫码支付】前必须调用终端采集）
            allinPayService.addTerm(new AllinPayAddTermRequest()
                    .setTermNo("8位终端号").setOperation("00").setTermState("00").setTermAddress("上海市-上海市-浦东新区-五星路101号5楼"));
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

    public static void testQuery() {
        try {
            // 如果有多个商户号，需要切换
//            allinPayService.switchoverTo("cusId");
            AllinPayQuery query = allinPayService.query(new AllinPayQueryRequest().setReqSn("订单号"));
            // 没有异常即代表-查询成功
            log.info("查询成功");
        } catch (PayException e) {
            // 处理查询失败
            log.error("查询失败：{}", e.getMessage());
        }
    }

    public static void testRefund() {
        try {
            // 如果有多个商户号，需要切换
//            aliPayService.switchoverTo("appId");
            allinPayService.handleRefund(new AllinPayRefundRequest()
                    .setReqSn("退款单号").setOldReqSn("原订单号").setTrxAmt(BigDecimal.valueOf(1))
                    .setOrderAmount(BigDecimal.valueOf(1)).setOrderDate(LocalDateTime.parse("2024-05-23T10:15:30")));
            // 没有异常即代表-退款成功
            log.info("退款成功");
        } catch (PayException e) {
            // 处理退款失败
            log.error("退款失败：{}", e.getMessage());
        }
    }

    public static void testNotify(HttpServletRequest request) {
        try {
            AllinPayNotify notify = allinPayService.notify(request);
            // 没有异常即代表-异步回调成功
            log.info("异步回调成功");
        } catch (PayException e) {
            // 处理异步回调失败
            log.error("异步回调失败：{}", e.getMessage());
        }
    }

    public static void testNotify2(HttpServletRequest request) {
        // 不喜欢try-catch的可以用以下两种方式实现成功和失败，所有的方法都可以这样封装
        PayBroker.broker(() -> allinPayService.notify(request), notify -> {
            String trxid = notify.getTrxid();
        }, e -> {
            log.error("异步回调失败：{}", e.getMessage());
        });

        PayOptional.of(() -> allinPayService.notify(request))
                .success(notify -> {
                    System.out.println(notify.getAppid());
                }).fail(e -> {
                    log.error(e.getMessage());
                }).get();
    }
}
