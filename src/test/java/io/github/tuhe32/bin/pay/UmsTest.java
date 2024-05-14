package io.github.tuhe32.bin.pay;

import io.github.tuhe32.bin.pay.common.exception.PayException;
import io.github.tuhe32.bin.pay.ums.UmsPayService;
import io.github.tuhe32.bin.pay.ums.UmsPayServiceImpl;
import io.github.tuhe32.bin.pay.ums.config.UmsPayConfig;
import io.github.tuhe32.bin.pay.ums.domain.UmsPayMiniRequest;
import io.github.tuhe32.bin.pay.ums.domain.UmsPayMiniResponse;
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

    public static void main(String[] args) {
    }

    public void testMiniPay() {
        try {
            UmsPayMiniResponse response = umsPayService.miniPay(new UmsPayMiniRequest()
                    .setTotalAmount(BigDecimal.valueOf(0.01))
                    .setSubOpenId("subOpenId"));
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }


    public static UmsPayService init() {
        UmsPayService umsPayService = new UmsPayServiceImpl();
        UmsPayConfig umsPayConfig = new UmsPayConfig("appId", "appKey", "mid", "tid", "sysCodePrefix", "md5Key");
        umsPayConfig.setNotifyUrl("notifyUrl");
        umsPayConfig.setSubAppId("subAppId");
        umsPayService.setConfig(umsPayConfig);
        return umsPayService;
    }
}
