package io.github.tuhe32.bin.pay;

import com.alipay.api.response.AlipayTradeRefundResponse;
import io.github.tuhe32.bin.pay.ali.AliPayService;
import io.github.tuhe32.bin.pay.ali.AliPayServiceImpl;
import io.github.tuhe32.bin.pay.ali.config.AliPayConfig;
import io.github.tuhe32.bin.pay.ali.domain.AliPayNotify;
import io.github.tuhe32.bin.pay.ali.domain.AliPayQuery;
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

    public static void main(String[] args) {
        // 仅用来展示使用方式，正常应该创建一个AliPayService的bean，注入到容器中
        // 调任何方法只要没有异常就是成功，有异常就是失败
    }

    public static AliPayService initAlipayConfig() {
        AliPayService aliPayService = new AliPayServiceImpl();
        AliPayConfig aliPayConfig = new AliPayConfig("app_id", "your private_key", "alipay_public_key");
        aliPayConfig.setNotifyUrl("notifyUrl");
        aliPayService.setConfig(aliPayConfig);
        return aliPayService;
    }

    public static void testPagePay() {
        try {
            // 如果只有一个支付宝appId，可以不用切换
            aliPayService.switchoverTo("appId");
            String pageRedirectionData = aliPayService.pagePay("sn", BigDecimal.valueOf(0.01), "测试支付");
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

    public static void testQuery() {
        try {
            // 如果只有一个支付宝appId，可以不用切换
            aliPayService.switchoverTo("appId");
            AliPayQuery query = aliPayService.query("sn");
            // 没有异常即代表-查询成功
            log.info("查询成功");
        } catch (PayException e) {
            // 处理查询失败
            log.error("查询失败：{}", e.getMessage());
        }
    }

    public static void testRefund() {
        try {
            // 如果只有一个支付宝appId，可以不用切换
            aliPayService.switchoverTo("appId");
            AlipayTradeRefundResponse response = aliPayService.refund("sn", BigDecimal.valueOf(0.01));
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
}
