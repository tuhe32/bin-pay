package io.github.tuhe32.bin.pay;

import io.github.tuhe32.bin.pay.common.PayBroker;
import io.github.tuhe32.bin.pay.common.PayOptional;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.ums.UmsPayService;
import io.github.tuhe32.bin.pay.ums.UmsPayServiceImpl;
import io.github.tuhe32.bin.pay.ums.config.UmsPayConfig;
import io.github.tuhe32.bin.pay.ums.domain.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 刘斌
 * @date 2024/5/13 15:12
 */
@Slf4j
public class UmsTest {

    private static final UmsPayService umsPayService;

    static {
        umsPayService = init();
    }

    // 正常不能通过静态方式初始化。需要创建一个UmsPayService的bean，注入到容器中
    public static UmsPayService init() {
        UmsPayService umsPayService = new UmsPayServiceImpl();
        UmsPayConfig umsPayConfig = new UmsPayConfig("appId", "appKey", "mid", "tid", "sysCodePrefix", "md5Key");
        umsPayConfig.setNotifyUrl("notifyUrl");
        umsPayConfig.setSubAppId("subAppId");
        umsPayService.setConfig(umsPayConfig);
        return umsPayService;
    }

    public void testPay() {
        try {
            // 小程序支付
            UmsPayMiniResponse miniPay = umsPayService.miniPay(new UmsPayMiniRequest()
                    .setTotalAmount(BigDecimal.ONE).setSubOpenId("subOpenId").setPayType("WXPAY"));
            // APP支付
            UmsPayAppResponse appPay = umsPayService.appPay(new UmsPayAppRequest()
                    .setTotalAmount(BigDecimal.ONE).setPayType("WXPAY"));
            // JS支付，微信公众号等一般用这个，不用下面的H5支付
            UmsPayJsResponse jsPay = umsPayService.jsPay(new UmsPayMiniRequest()
                    .setTotalAmount(BigDecimal.ONE).setSubOpenId("subOpenId").setPayType("WXPAY"));
            // 扫码支付
            UmsPayScanQrPayResponse scanQrPay = umsPayService.scanQrPay(new UmsPayScanQrPayRequest()
                    .setTransactionAmount(BigDecimal.ONE).setPayCode("支付码"));
            // H5支付，由浏览器直接跳转，不需要应答报文
            String h5Pay = umsPayService.h5Pay(new UmsPayMiniRequest()
                    .setTotalAmount(BigDecimal.ONE).setSubOpenId("subOpenId").setPayType("WXPAY"));
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
//            umsPayService.switchoverTo("cusId");
            // 小程序支付查询
            UmsPayQueryResponse miniQuery = umsPayService.miniQuery(new UmsPayQueryRequest()
                    .setMerOrderId("订单号"));
            // JS支付查询
            UmsPayQueryResponse jsQuery = umsPayService.jsQuery(new UmsPayQueryRequest()
                    .setMerOrderId("订单号"));
            // APP支付查询
            UmsPayQueryResponse appQuery = umsPayService.appQuery(new UmsPayQueryRequest()
                    .setMerOrderId("订单号").setPayType("WXPAY"));
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
//            umsPayService.switchoverTo("cusId");
            // 小程序或H5,JS退款
            UmsPayRefundResponse miniRefund = umsPayService.miniRefund(new UmsPayRefundRequest()
                    .setRefundAmount(BigDecimal.ONE).setMerOrderId("订单号").setRefundOrderId("退款单号"));
            // APP退款
            UmsPayRefundResponse appRefund = umsPayService.appRefund(new UmsPayRefundRequest()
                    .setRefundAmount(BigDecimal.ONE).setMerOrderId("订单号").setRefundOrderId("退款单号"));
            // 扫码支付退款
            UmsPayScanQrRefundResponse scanQrRefund = umsPayService.scanQrRefund(new UmsPayScanQrRefundRequest()
                    .setTransactionAmount(BigDecimal.ONE).setMerchantOrderId("订单号").setRefundRequestId("退款单号"));
            // 没有异常即代表-退款成功
            log.info("退款成功");
        } catch (PayException e) {
            // 处理退款失败
            log.error("退款失败：{}", e.getMessage());
        }
    }

    public static void testNotify(HttpServletRequest request) {
        try {
            UmsPayNotify notify = umsPayService.notify(request);
            // 没有异常即代表-异步回调成功
            log.info("异步回调成功");
        } catch (PayException e) {
            // 处理异步回调失败
            log.error("异步回调失败：{}", e.getMessage());
        }
    }

    public static void testQuery2() {
        String orderSn = "订单号";
        // 不喜欢try-catch的可以用以下两种方式实现成功和失败，所有的方法都可以这样封装
        PayBroker.broker(() -> umsPayService.miniQuery(new UmsPayQueryRequest().setMerOrderId(orderSn)), query -> {
            String targetOrderId = query.getTargetOrderId();
        }, e -> {
            log.error("查询失败：{}", e.getMessage());
        });

        PayOptional.of(() -> umsPayService.miniQuery(new UmsPayQueryRequest().setMerOrderId(orderSn)))
                .success(query -> {
                    System.out.println(query.getMerOrderId());
                }).fail(e -> {
                    log.error(e.getMessage());
                }).get();
    }
}
