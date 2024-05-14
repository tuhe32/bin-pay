package io.github.tuhe32.bin.pay;

import io.github.tuhe32.bin.pay.allin.AllinPayService;
import io.github.tuhe32.bin.pay.allin.AllinPayServiceImpl;
import io.github.tuhe32.bin.pay.allin.config.AllinPayConfig;
import io.github.tuhe32.bin.pay.allin.domain.AllinPayUnitOrderRequest;
import io.github.tuhe32.bin.pay.allin.util.PaymentChecker;
import io.github.tuhe32.bin.pay.common.exception.PayException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/9 16:58
 */
@Slf4j
public class AllinPayTest {

    private static final AllinPayService allinPayService;
    private static int count = 0;

    static {
        allinPayService = init();
    }

    public static void main(String[] args) {
        PaymentChecker checker = new PaymentChecker(AllinPayTest::query, new HashMap<>());
        boolean result = checker.startCheckingPayment();
        System.out.println(result);
    }

    public static boolean query(Map<String, String> sn) throws PayException {
        int i = count++;
        System.out.println(i);
        if (i == 10) {
            return true;
        }
        return false;
    }

    public static AllinPayService init() {
        AllinPayService allinPayService = new AllinPayServiceImpl();
        AllinPayConfig allinPayConfig = new AllinPayConfig("app_id", "your private_key", "alipay_public_key");
        allinPayConfig.setNotifyUrl("notifyUrl");
        allinPayService.setConfig(allinPayConfig);
        return allinPayService;
    }

    public static void testUnitOrderPay() {
        try {
            // 如果只有一个支付宝appId，可以不用切换
            allinPayService.switchoverTo("appId");
            String payInfo = allinPayService.unitOrderPay(new AllinPayUnitOrderRequest()
                    .setReqSn("sn").setTrxAmt(BigDecimal.valueOf(0.01)).setPayType("W02").setSubOpenId("openId"));
            // 没有异常即代表-支付成功
            log.info("支付成功");
        } catch (PayException e) {
            // 处理支付失败
            log.error("支付失败：{}", e.getMessage());
        }
    }
}
