package io.github.tuhe32.bin.pay;

import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.github.tuhe32.bin.pay.ali.AliPayService;
import io.github.tuhe32.bin.pay.ali.AliPayServiceImpl;
import io.github.tuhe32.bin.pay.ali.config.AliPayConfig;
import io.github.tuhe32.bin.pay.ali.domain.AliPayNotify;
import io.github.tuhe32.bin.pay.ali.domain.AliPayQuery;
import io.github.tuhe32.bin.pay.common.PayBroker;
import io.github.tuhe32.bin.pay.common.PayOptional;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 刘斌
 * @date 2024/4/27 16:40
 */
@Slf4j
public class AlipayTest {

    private static final AliPayService aliPayService;

    static {
        aliPayService = initAlipayConfig();
    }

    public static AliPayService initAlipayConfig() {
        AliPayService aliPayService = new AliPayServiceImpl();
        AliPayConfig aliPayConfig = new AliPayConfig("app_id", "your private_key", "alipay_public_key");
        aliPayConfig.setNotifyUrl("notifyUrl");
        aliPayService.setConfig(aliPayConfig);
        return aliPayService;
    }

    // 仅用来展示使用方式，正常应该创建一个AliPayService的bean，注入到容器中
    // 调任何方法只要没有异常就是成功，有异常就是失败
    public static void testPay() {
        try {
            // 如果有多个支付宝appId，需要切换
//            aliPayService.switchoverTo("appId");
            // 电脑网站支付
            String pageRedirectionData = aliPayService.pagePay("订单号", BigDecimal.valueOf(1), "测试支付");
            // 移动网页支付
            String mobileRedirectionData = aliPayService.mobileWapPay("订单号", BigDecimal.valueOf(1), "测试支付");
            // APP支付
            String orderStr = aliPayService.appPay("订单号", BigDecimal.valueOf(1), "测试支付");
            // 付款码支付（收银员使用扫码设备读取用户手机支付宝“付款码”获取设备（如扫码枪）读取用户手机支付宝的付款码信息后，将二维码或条码信息通过本接口上送至支付宝发起支付。）
            AlipayTradePayResponse codeResponse = aliPayService.codePay("订单号", BigDecimal.valueOf(1), "测试支付", "付款码");
            // 扫码支付（收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。）
            AlipayTradePrecreateResponse qrResponse = aliPayService.qrPay("订单号", BigDecimal.valueOf(1), "测试支付");
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

    public static void testQuery() {
        try {
            // 如果有多个支付宝appId，需要切换
//            aliPayService.switchoverTo("appId");
            AliPayQuery query = aliPayService.query("订单号");
            // 没有异常即代表-查询成功
            log.info("查询成功");
        } catch (PayException e) {
            // 处理查询失败
            log.error("查询失败：{}", e.getMessage());
        }
    }

    public static void testRefund() {
        try {
            // 如果有多个支付宝appId，需要切换
//            aliPayService.switchoverTo("appId");
            AlipayTradeRefundResponse response = aliPayService.refund("订单号", BigDecimal.valueOf(1));
            // 没有异常即代表-退款成功
            log.info("退款成功");
        } catch (PayException e) {
            // 处理退款失败
            log.error("退款失败：{}", e.getMessage());
        }
    }

    public static void testNotify(HttpServletRequest request) {
        try {
            AliPayNotify notify = aliPayService.notify(request);
            // 没有异常即代表-异步回调成功
            log.info("异步回调成功");
        } catch (PayException e) {
            // 处理异步回调失败
            log.error("异步回调失败：{}", e.getMessage());
        }
    }

    public void testRefund2() {
        String orderSn = "订单号";
        BigDecimal orderAmount = BigDecimal.ONE;
        // 不喜欢try-catch的可以用以下两种方式实现成功和失败，所有的方法都可以这样封装
        PayBroker.broker(() -> aliPayService.refund(orderSn, orderAmount), result -> {
            System.out.println(result.getOutTradeNo());
        }, e -> {
            log.error("查询失败：{}", e.getMessage());
        });

        PayOptional.of(() -> aliPayService.refund(orderSn, orderAmount))
                .success(result -> {
                    System.out.println(result);
                }).fail(e -> {
                    log.error(e.getMessage());
                }).get();
    }
}
