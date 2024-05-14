package io.github.tuhe32.bin.pay.ums.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author 刘斌
 * @date 2024/5/14 11:50
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class UmsPayScanQrRefundResponse {

    public static UmsPayScanQrRefundResponse of(Map<String, String> map) {
        UmsPayScanQrRefundResponse umsPayScanQrRefundResponse = new UmsPayScanQrRefundResponse();
        umsPayScanQrRefundResponse.setTransactionTime(map.get("transactionTime"));
        umsPayScanQrRefundResponse.setTransactionDateWithYear(map.get("transactionDateWithYear"));
        umsPayScanQrRefundResponse.setSettlementDateWithYear(map.get("settlementDateWithYear"));
        umsPayScanQrRefundResponse.setThirdPartyName(map.get("thirdPartyName"));
        umsPayScanQrRefundResponse.setRefundInvoiceAmount(map.get("refundInvoiceAmount"));
        umsPayScanQrRefundResponse.setTransactionAmount(map.get("transactionAmount") == null ? null : Integer.valueOf(map.get("transactionAmount")));
        umsPayScanQrRefundResponse.setOriInfo(map.get("oriInfo"));
        return umsPayScanQrRefundResponse;
    }

    /**
     * 交易时间
     */
    private String transactionTime;
    /**
     * 交易日期
     */
    private String transactionDateWithYear;
    /**
     * 结算日期
     */
    private String settlementDateWithYear;
    /**
     * 第三方名称
     */
    private String thirdPartyName;
    /**
     * 实际退款金额
     */
    private String refundInvoiceAmount;
    /**
     * 交易金额
     */
    private Integer transactionAmount;
    /**
     * 原文信息
     */
    private String oriInfo;
}
