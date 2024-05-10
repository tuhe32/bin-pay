package io.github.tuhe32.bin.pay;

import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.wx.WxPayI;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘斌
 * @date 2024/4/30 10:33
 */
@Slf4j
public class WechatTest {

    private WxPayService wxPayService;
    private WxPayI wxPayI;

    public static void main(String[] args) {

    }

    public void testPagePay() {
        try {
            // 如果只有一个mchId，可以不用切换
            wxPayService.switchoverTo("mchId");
            String pageRedirectionData = wxPayI.nativePay("sn", BigDecimal.valueOf(0.01), "测试支付");
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
            wxPayService.switchoverTo("mchId");
            WxPayOrderQueryV3Result query = wxPayI.query("sn");
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
            wxPayService.switchoverTo("mchId");
            WxPayRefundV3Result response = wxPayI.refund("sn", BigDecimal.valueOf(0.01), "outRequestNo");
            // 没有异常即代表-退款成功
            log.info("退款成功");
        } catch (PayException e) {
            // 处理退款失败
            log.error("退款失败：{}", e.getMessage());
        }
    }

    public void testNotify(HttpServletRequest request) {
        try {
            WxPayNotifyV3Result.DecryptNotifyResult notify = wxPayI.notify("notifyData");
            // 没有异常即代表-异步回调成功
            log.info("异步回调成功");
        } catch (PayException e) {
            // 处理异步回调失败
            log.error("异步回调失败：{}", e.getMessage());
        }
    }

    //    @Bean
//    @ConditionalOnMissingBean(WxPayService.class)
    public WxPayService wxPayService() {
        WxPayService payService = new WxPayServiceImpl();
        //查询微信支付配置
        List<PayChannelWechat> payChannelWechatList = new ArrayList<>();
        PayChannelWechat payChannelWechat = new PayChannelWechat();
        payChannelWechat.setAppid("nullAppId");
        payChannelWechat.setMchid("nullMchId");
        payChannelWechat.setApiV3Key("nullKey");
        payChannelWechat.setPrivateKeyPath("nullKeyPath");
        payChannelWechat.setPrivateCertPath("nullCertPath");
        payChannelWechat.setNotifyUrl("nullNotifyUrl");
        payChannelWechatList.add(payChannelWechat);
        if (payChannelWechatList != null && payChannelWechatList.size() > 0) {
            payService.setMultiConfig(
                    payChannelWechatList.stream()
                            .map(WechatTest::createNewConfig)
                            .collect(Collectors.toMap(WxPayConfig::getMchId, a -> a, (o, n) -> o)));
        } else {
            PayChannelWechat defaultChannel = new PayChannelWechat();
            defaultChannel.setAppid("nullAppId");
            defaultChannel.setMchid("nullMchId");
            defaultChannel.setApiV3Key("nullKey");
            defaultChannel.setPrivateKeyPath("nullKeyPath");
            defaultChannel.setPrivateCertPath("nullCertPath");
            defaultChannel.setNotifyUrl("nullNotifyUrl");
            payService.setConfig(createNewConfig(defaultChannel));
        }
        return payService;
    }

    /**
     * 进行商户配置切换
     */
    public void switchoverConfig(String mchId, PayChannelWechat payChannelWechat) {
        if (!wxPayService.switchover(mchId)) {
            log.info("未找到对应mchId=[{}]的配置，重新查询数据库", mchId);
            if (payChannelWechat != null) {
                log.info("从数据库加载对应mchId=[{}]的配置", mchId);
                wxPayService.addConfig(payChannelWechat.getMchid(), createNewConfig(payChannelWechat));
            } else {
                throw new RuntimeException("未找到对应mchId=[{}]的配置");
            }
        }
    }

    private static WxPayConfig createNewConfig(PayChannelWechat payChannelWechat) {
        WxPayConfig config = new WxPayConfig();
        // 以下参数必填
        config.setAppId(StringUtils.trimToNull(payChannelWechat.getAppid()));
        config.setMchId(StringUtils.trimToNull(payChannelWechat.getMchid()));
        config.setApiV3Key(payChannelWechat.getApiV3Key());
        config.setPrivateKeyPath(payChannelWechat.getPrivateKeyPath());
        config.setPrivateCertPath(payChannelWechat.getPrivateCertPath());
        config.setNotifyUrl(payChannelWechat.getNotifyUrl());
        return config;
    }

    @Data
    public class PayChannelWechat {
        private String appid;
        private String mchid;
        private String apiV3Key;
        private String privateKeyPath;
        private String privateCertPath;
        private String notifyUrl;
    }
}
