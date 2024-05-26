package io.github.tuhe32.bin.pay;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import io.github.tuhe32.bin.pay.common.PayBroker;
import io.github.tuhe32.bin.pay.common.PayOptional;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.wx.WxPay;
import io.github.tuhe32.bin.pay.wx.WxPayI;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author 刘斌
 * @date 2024/4/30 10:33
 */
@Slf4j
public class WechatTest {

    private static final WxPayI wxPayI;

    static {
        wxPayI = init();
    }

    // 正常不能通过静态方式初始化。需要创建一个WxPayI的bean，注入到容器中
    public static WxPayI init() {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(createNewConfig());
        return new WxPay(payService);
    }

    public void testPay() {
        try {
            // 如果只有一个mchId，可以不用切换
//            wxPayI.switchoverTo("mchId");
            // 扫码支付
            String pageRedirectionData = wxPayI.nativePay("订单号", BigDecimal.ONE, "测试支付");
            // JSAPI支付
            WxPayUnifiedOrderV3Result.JsapiResult jsapiPay = wxPayI.jsapiPay("订单号", BigDecimal.ONE, "测试支付", "openId");
            // H5支付
            String h5Pay = wxPayI.h5Pay("订单号", BigDecimal.ONE, "测试支付", "127.0.0.1");
            // APP支付
            WxPayUnifiedOrderV3Result.AppResult appPay = wxPayI.appPay("订单号", BigDecimal.ONE, "测试支付");
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }

    public void testQuery() {
        try {
            // 如果只有一个mchId，可以不用切换
//            wxPayI.switchoverTo("mchId");
            WxPayOrderQueryV3Result query = wxPayI.query("订单号");
            // 没有异常即代表-查询成功
            log.info("查询成功");
        } catch (PayException e) {
            // 处理查询失败
            log.error("查询失败：{}", e.getMessage());
        }
    }

    public void testRefund() {
        try {
            // 如果只有一个mchId，可以不用切换
//            wxPayI.switchoverTo("mchId");
            WxPayRefundV3Result response = wxPayI.refund("订单号", BigDecimal.ONE, "退款单号");
            // 没有异常即代表-退款成功
            log.info("退款成功");
        } catch (PayException e) {
            // 处理退款失败
            log.error("退款失败：{}", e.getMessage());
        }
    }

    public void testNotify(String notifyData) {
        try {
            // 【@RequestBody String notifyData】， 从body里获取数据
            WxPayNotifyV3Result.DecryptNotifyResult notify = wxPayI.notify(notifyData);
            // 没有异常即代表-异步回调成功
            log.info("异步回调成功");
        } catch (PayException e) {
            // 处理异步回调失败
            log.error("异步回调失败：{}", e.getMessage());
        }
    }

    public void testPay2() {
        String orderSn = "订单号";
        BigDecimal orderAmount = BigDecimal.ONE;
        String subject = "测试支付";
        // 不喜欢try-catch的可以用以下两种方式实现成功和失败，所有的方法都可以这样封装
        PayBroker.broker(() -> wxPayI.nativePay(orderSn, orderAmount, subject), result -> {
            System.out.println(result);
        }, e -> {
            log.error("查询失败：{}", e.getMessage());
        });

        PayOptional.of(() -> wxPayI.nativePay(orderSn, orderAmount, subject))
                .success(result -> {
                    System.out.println(result);
                }).fail(e -> {
                    log.error(e.getMessage());
                }).get();
    }

    private static WxPayConfig createNewConfig() {
        WxPayConfig config = new WxPayConfig();
        // 以下参数必填
        config.setAppId("appId");
        config.setMchId("mchId");
        config.setApiV3Key("apiV3Key");
        config.setPrivateKeyPath("privateKeyPath");
        config.setPrivateCertPath("privateCertPath");
        config.setNotifyUrl("notifyUrl");
        return config;
    }

    /**
     * 进行商户配置切换
     */
    public void switchoverConfig(String mchId) {
        if (!wxPayI.switchover(mchId)) {
            log.info("未找到对应mchId=[{}]的配置，重新查询数据库", mchId);
            // 获取数据库查询结果，判断是否为null
            if (mchId != null) {
                log.info("从数据库加载对应mchId=[{}]的配置", mchId);
                wxPayI.addConfig(mchId, createNewConfig());
            } else {
                throw new RuntimeException("未找到对应mchId=[{}]的配置");
            }
        }
    }
}
