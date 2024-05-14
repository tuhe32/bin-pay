package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/13 22:12
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayScanQrPayResponse {

    public static UmsPayScanQrPayResponse of(Map<String, String> map) {
        UmsPayScanQrPayResponse umsPayScanQrPayResponse = new UmsPayScanQrPayResponse();
        umsPayScanQrPayResponse.setTransactionAmount(map.get("transactionAmount"));
        umsPayScanQrPayResponse.setAmount(map.get("amount"));
        umsPayScanQrPayResponse.setOrderId(map.get("orderId"));
        umsPayScanQrPayResponse.setThirdPartyOrderId(map.get("thirdPartyOrderId"));
        umsPayScanQrPayResponse.setThirdPartyName(map.get("thirdPartyName"));
        umsPayScanQrPayResponse.setTransactionDateWithYear(map.get("transactionDateWithYear"));
        umsPayScanQrPayResponse.setTransactionTime(map.get("transactionTime"));
        return umsPayScanQrPayResponse;
    }


    /**
     * 订单金额
     */
    private String transactionAmount;
    /**
     * 实际支付金额
     */
    private String amount;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 第三方订单号
     */
    private String thirdPartyOrderId;

    /**
     * 第三方名称
     */
    private String thirdPartyName;

    /**
     * 交易日期（yyyyMMdd）
     */
    private String transactionDateWithYear;

    /**
     * 交易时间（HHmmss）
     */
    private String transactionTime;

}
